package com.hcmute.fastfoodsystem.service.impl;

import com.hcmute.fastfoodsystem.model.OrderDetail;
import com.hcmute.fastfoodsystem.repository.OrderDetailRepository;
import com.hcmute.fastfoodsystem.service.OrderDetailService;
import com.stripe.model.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class OrderDetailServiceImpl implements OrderDetailService {
    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Override
    public void addOrderedProducts(OrderDetail orderDetail) {
        orderDetailRepository.save(orderDetail);
    }
}
