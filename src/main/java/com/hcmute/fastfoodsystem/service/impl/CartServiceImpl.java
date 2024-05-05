package com.hcmute.fastfoodsystem.service.impl;

import com.hcmute.fastfoodsystem.dto.CartDto;
import com.hcmute.fastfoodsystem.dto.CartItemDto;
import com.hcmute.fastfoodsystem.dto.ProductDto;
import com.hcmute.fastfoodsystem.exceptions.CartItemNotExistException;
import com.hcmute.fastfoodsystem.model.*;
import com.hcmute.fastfoodsystem.repository.CartItemRepository;
import com.hcmute.fastfoodsystem.repository.CartRepository;
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
        Product product = transfer(productDto);

        double unitPrice = productDto.getPrice();

        int itemQuantity = 0;
        if (cartItemList == null) {
            cartItemList = new HashSet<>();
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
        int itemQuantity = quantity;


        item.setQuantity(itemQuantity);
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
    public CartDto addItemToCartSession(CartDto cartDto, ProductDto productDto, int quantity) {
        CartItemDto cartItem = findInDTO(cartDto, productDto.getId());
        if (cartDto == null) {
            cartDto = new CartDto();
        }
        Set<CartItemDto> cartItemList = cartDto.getCartItems();
        double unitPrice = productDto.getPrice();
        int itemQuantity = 0;
        if (cartItemList == null) {
            cartItemList = new HashSet<>();
            if (cartItem == null) {
                cartItem = new CartItemDto();
                cartItem.setProduct(productDto);
                cartItem.setCart(cartDto);
                cartItem.setQuantity(quantity);
                cartItem.setUnitPrice(unitPrice);
                cartItemList.add(cartItem);
                System.out.println("add");
            } else {
                itemQuantity = cartItem.getQuantity() + quantity;
                cartItem.setQuantity(itemQuantity);
            }
        } else {
            if (cartItem == null) {
                cartItem = new CartItemDto();
                cartItem.setProduct(productDto);
                cartItem.setCart(cartDto);
                cartItem.setQuantity(quantity);
                cartItem.setUnitPrice(unitPrice);
                cartItemList.add(cartItem);
                System.out.println("add");
            } else {
                itemQuantity = cartItem.getQuantity() + quantity;
                cartItem.setQuantity(itemQuantity);
            }
        }
        System.out.println("here");
        cartDto.setCartItems(cartItemList);
        double totalPrice = totalPriceDto(cartItemList);
        int totalItem = totalItemDto(cartItemList);
        cartDto.setTotalPrice(totalPrice);
        cartDto.setTotalItems(totalItem);
        System.out.println(cartDto.getTotalItems());
        System.out.println(cartDto.getTotalPrice());
        System.out.println("success");
        return cartDto;
    }

    @Override
    public CartDto updateCartSession(CartDto cartDto, ProductDto productDto, int quantity) {
        Set<CartItemDto> cartItemList = cartDto.getCartItems();
        CartItemDto item = findInDTO(cartDto, productDto.getId());
        int itemQuantity = item.getQuantity() + quantity;
        int totalItem = totalItemDto(cartItemList);
        double totalPrice = totalPriceDto(cartItemList);
        item.setQuantity(itemQuantity);
        cartDto.setCartItems(cartItemList);
        cartDto.setTotalPrice(totalPrice);
        cartDto.setTotalItems(totalItem);
        System.out.println(cartDto.getTotalItems());
        return cartDto;
    }

    @Override
    public CartDto removeItemFromCartSession(CartDto cartDto, ProductDto productDto, int quantity) {
        Set<CartItemDto> cartItemList = cartDto.getCartItems();
        CartItemDto item = findInDTO(cartDto, productDto.getId());
        cartItemList.remove(item);
        double totalPrice = totalPriceDto(cartItemList);
        int totalItem = totalItemDto(cartItemList);
        cartDto.setCartItems(cartItemList);
        cartDto.setTotalPrice(totalPrice);
        cartDto.setTotalItems(totalItem);
        System.out.println(cartDto.getTotalItems());
        return cartDto;
    }

    @Override
    public Cart combineCart(CartDto cartDto, Cart cart) {
        if (cart == null) {
            cart = new Cart();
        }
        Set<CartItems> cartItems = cart.getCartItems();
        if (cartItems == null) {
            cartItems = new HashSet<>();
        }
        Set<CartItems> cartItemsDto = convertCartItem(cartDto.getCartItems(), cart);
        for (CartItems cartItem : cartItemsDto) {
            cartItems.add(cartItem);
        }
        double totalPrice = totalPrice(cartItems);
        int totalItems = totalItem(cartItems);
        cart.setTotalItems(totalItems);
        cart.setCartItems(cartItems);
        cart.setTotalPrice(totalPrice);
        return cart;
    }

    @Override
    public void deleteCartById(Long id) {
        Cart shoppingCart = cartRepository.getById(id);
        if(!ObjectUtils.isEmpty(shoppingCart) && !ObjectUtils.isEmpty(shoppingCart.getCartItems())){
            cartItemRepository.deleteAll(shoppingCart.getCartItems());
        }
        shoppingCart.getCartItems().clear();
        shoppingCart.setTotalPrice(0);
        shoppingCart.setTotalItems(0);
        cartRepository.save(shoppingCart);
    }

    @Override
    public Cart getCart(String username) {
        User user = userRepository.findByEmail(username);
        Cart cart = user.getCart();
        return cart;
    }

    @Override
    public Cart saveCart(Cart cart) {
        return cartRepository.save(cart);
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

    private CartItemDto findInDTO(CartDto shoppingCart, long productId) {
        if (shoppingCart == null) {
            return null;
        }
        CartItemDto cartItem = null;
        for (CartItemDto item : shoppingCart.getCartItems()) {
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

    private int totalItemDto(Set<CartItemDto> cartItemList) {
        int totalItem = 0;
        for (CartItemDto item : cartItemList) {
            totalItem += item.getQuantity();
        }
        return totalItem;
    }

    private double totalPriceDto(Set<CartItemDto> cartItemList) {
        double totalPrice = 0;
        for (CartItemDto item : cartItemList) {
            totalPrice += item.getUnitPrice() * item.getQuantity();
        }
        return totalPrice;
    }

    private Product transfer(ProductDto productDto) {
        Product product = new Product();
        product.setId(productDto.getId());
        product.setProductName(productDto.getProductName());
        product.setPrice(productDto.getPrice());
        product.setImage(productDto.getImage());
        product.setPrice(productDto.getPrice());
        Category category = categoryService.getCategoryByCategory(productDto.getCategory());
        product.setCategory(category);
        return product;
    }

    private Set<CartItems> convertCartItem(Set<CartItemDto> cartItemDtos, Cart cart) {
        Set<CartItems> cartItems = new HashSet<>();
        for (CartItemDto cartItemDto : cartItemDtos) {
            CartItems cartItem = new CartItems();
            cartItem.setQuantity(cartItemDto.getQuantity());
            cartItem.setProduct(transfer(cartItemDto.getProduct()));
            cartItem.setUnitPrice(cartItemDto.getUnitPrice());
            cartItem.setId(cartItemDto.getId());
            cartItem.setCart(cart);
            cartItems.add(cartItem);
        }
        return cartItems;
    }
}
