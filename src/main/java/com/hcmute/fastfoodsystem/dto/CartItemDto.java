package com.hcmute.fastfoodsystem.dto;

import com.hcmute.fastfoodsystem.model.Cart;
import com.hcmute.fastfoodsystem.model.CartItems;
import com.hcmute.fastfoodsystem.model.Product;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {
    private long id;

    private CartDto cart;

    private ProductDto product;

    private int quantity;

    private double unitPrice;

}
