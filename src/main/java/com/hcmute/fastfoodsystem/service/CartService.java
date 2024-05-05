package com.hcmute.fastfoodsystem.service;

import com.hcmute.fastfoodsystem.dto.CartDto;
import com.hcmute.fastfoodsystem.dto.ProductDto;
import com.hcmute.fastfoodsystem.model.Cart;

public interface CartService {
    Cart addItemToCart(ProductDto productDto, int quantity, String username);

    Cart updateCart(ProductDto productDto, int quantity, String username);

    Cart removeItemFromCart(ProductDto productDto, String username);

    CartDto addItemToCartSession(CartDto cartDto, ProductDto productDto, int quantity);

    CartDto updateCartSession(CartDto cartDto, ProductDto productDto, int quantity);

    CartDto removeItemFromCartSession(CartDto cartDto, ProductDto productDto, int quantity);

    Cart combineCart(CartDto cartDto, Cart cart);


    void deleteCartById(Long id);

    Cart getCart(String username);

    Cart saveCart(Cart cart);

}
