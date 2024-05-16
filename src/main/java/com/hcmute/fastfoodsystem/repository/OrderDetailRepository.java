package com.hcmute.fastfoodsystem.repository;

import com.hcmute.fastfoodsystem.model.Order;
import com.hcmute.fastfoodsystem.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}
