package com.hcmute.fastfoodsystem.service.impl;


import com.hcmute.fastfoodsystem.dto.ProductDto;
import com.hcmute.fastfoodsystem.model.*;
import com.hcmute.fastfoodsystem.repository.CartItemRepository;
import com.hcmute.fastfoodsystem.repository.CartRepository;
import com.hcmute.fastfoodsystem.repository.ProductRepository;
import com.hcmute.fastfoodsystem.repository.UserRepository;
import com.hcmute.fastfoodsystem.service.CartService;
import com.hcmute.fastfoodsystem.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    CategoryService categoryService;


    @Override
    @Transactional
    public Cart addItemToCart(ProductDto productDto, int quantity, String username) {
        User user = userRepository.findByEmail(username);
        Cart shoppingCart = user.getCart();
        if (shoppingCart == null) {
            shoppingCart = new Cart();
        }
        Set<CartItems> cartItemList = shoppingCart.getCartItems();
        CartItems cartItem = find(cartItemList, productDto.getId());
        Product product = productRepository.findById(productDto.getId()).orElseThrow();
        double unitPrice = productDto.getPrice();
        int itemQuantity = 0;
        if (cartItemList == null) {
            cartItemList = new HashSet<>();
            cartItem = new CartItems();
            cartItem.setProduct(product);
            cartItem.setCart(shoppingCart);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(unitPrice);
            cartItem.setCart(shoppingCart);
            cartItemList.add(cartItem);
            cartItemRepository.save(cartItem);
        } else {
            if (cartItem == null) {
                cartItem = new CartItems();
                cartItem.setProduct(product);
                cartItem.setCart(shoppingCart);
                cartItem.setQuantity(quantity);
                cartItem.setUnitPrice(unitPrice);
                cartItem.setCart(shoppingCart);
                cartItemList.add(cartItem);
                cartItemRepository.save(cartItem);
            } else {
                itemQuantity = cartItem.getQuantity() + quantity;
                cartItem.setQuantity(itemQuantity);
                cartItemRepository.save(cartItem);
            }
        }
        shoppingCart.setCartItems(cartItemList);

        double totalPrice = totalPrice(shoppingCart.getCartItems());
        int totalItem = totalItem(shoppingCart.getCartItems());

        shoppingCart.setTotalPrice(totalPrice);
        shoppingCart.setTotalItems(totalItem);
        shoppingCart.setUser(user);

        return cartRepository.save(shoppingCart);
    }

    @Override
    @Transactional
    public Cart updateCart(ProductDto productDto, int quantity, String username) {
        User customer = userRepository.findByEmail(username);
        Cart shoppingCart = customer.getCart();
        Set<CartItems> cartItemList = shoppingCart.getCartItems();
        CartItems item = find(cartItemList, productDto.getId());
        item.setQuantity(quantity);
        cartItemRepository.save(item);
        shoppingCart.setCartItems(cartItemList);
        int totalItem = totalItem(cartItemList);
        double totalPrice = totalPrice(cartItemList);
        shoppingCart.setTotalPrice(totalPrice);
        shoppingCart.setTotalItems(totalItem);
        return cartRepository.save(shoppingCart);
    }

    @Override
    @Transactional
    public Cart removeItemFromCart(ProductDto productDto, String username) {
        User user = userRepository.findByEmail(username);
        Cart shoppingCart = user.getCart();
        Set<CartItems> cartItemList = shoppingCart.getCartItems();
        CartItems item = find(cartItemList, productDto.getId());
        cartItemList.remove(item);
        cartItemRepository.delete(item);
        double totalPrice = totalPrice(cartItemList);
        int totalItem = totalItem(cartItemList);
        shoppingCart.setCartItems(cartItemList);
        shoppingCart.setTotalPrice(totalPrice);
        shoppingCart.setTotalItems(totalItem);
        return cartRepository.save(shoppingCart);
    }

    @Override
    public void deleteCartById(Long id) {
        Cart shoppingCart = cartRepository.getReferenceById(id);
        if(!ObjectUtils.isEmpty(shoppingCart) && !ObjectUtils.isEmpty(shoppingCart.getCartItems())){
            cartItemRepository.deleteAll(shoppingCart.getCartItems());
        }
        shoppingCart.getCartItems().clear();
        shoppingCart.setTotalPrice(0);
        shoppingCart.setTotalItems(0);
        cartRepository.save(shoppingCart);
    }

    private CartItems find(Set<CartItems> cartItems, long productId) {
        if (cartItems == null) {
            return null;
        }
        CartItems cartItem = null;
        for (CartItems item : cartItems) {
            if (item.getProduct().getId() == productId) {
                cartItem = item;
            }
        }
        return cartItem;
    }

    private int totalItem(Set<CartItems> cartItemList) {
        int totalItem = 0;
        for (CartItems item : cartItemList) {
            totalItem += item.getQuantity();
        }
        return totalItem;
    }

    private double totalPrice(Set<CartItems> cartItemList) {
        double totalPrice = 0.0;
        for (CartItems item : cartItemList) {
            totalPrice += item.getUnitPrice() * item.getQuantity();
        }
        return totalPrice;
    }
}
