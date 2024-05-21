package com.hcmute.fastfoodsystem.service;

import com.hcmute.fastfoodsystem.dto.CartDto;
import com.hcmute.fastfoodsystem.dto.ProductDto;
import com.hcmute.fastfoodsystem.model.Cart;

public interface CartService {
    Cart addItemToCart(ProductDto productDto, int quantity, String username);

    Cart updateCart(ProductDto productDto, int quantity, String username);

    Cart removeItemFromCart(ProductDto productDto, String username);

    void deleteCartById(Long id);

}
