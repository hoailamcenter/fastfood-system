package com.hcmute.fastfoodsystem.dto;


import com.hcmute.fastfoodsystem.model.User;
import lombok.*;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {
    private Long id;

    private User user;

    private double totalPrice;

    private int totalItems;

    private Set<CartItemDto> cartItems;

}
