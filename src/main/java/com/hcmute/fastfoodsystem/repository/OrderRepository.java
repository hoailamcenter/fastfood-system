package com.hcmute.fastfoodsystem.repository;

import com.hcmute.fastfoodsystem.model.Order;
import com.hcmute.fastfoodsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
