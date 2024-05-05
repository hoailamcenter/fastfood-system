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
public class OrderDetailDto {
    private @NotNull double price;
    private @NotNull int quantity;
    private @NotNull long orderId;
    private @NotNull long productId;

}
