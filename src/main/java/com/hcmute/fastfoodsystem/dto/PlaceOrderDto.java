package com.hcmute.fastfoodsystem.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlaceOrderDto {
    private long id;
    private @NotNull long userId;
    private @NotNull double totalPrice;
}
