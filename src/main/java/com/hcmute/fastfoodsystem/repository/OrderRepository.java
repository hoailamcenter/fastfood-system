package com.hcmute.fastfoodsystem.repository;

import com.hcmute.fastfoodsystem.model.Order;
import com.hcmute.fastfoodsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUserOrderByOrderDateDesc(User user);
}
