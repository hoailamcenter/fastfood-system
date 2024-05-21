package com.hcmute.fastfoodsystem.service;

import com.hcmute.fastfoodsystem.dto.PlaceOrderDto;
import com.hcmute.fastfoodsystem.model.Cart;
import com.hcmute.fastfoodsystem.model.Order;

import java.util.List;

public interface OrderService {
    Order save(Cart shoppingCart, String payment);

    List<Order> findAll(String username);

    List<Order> findALlOrders();

    Order acceptOrder(Long id);

    void cancelOrder(Long id);
}
