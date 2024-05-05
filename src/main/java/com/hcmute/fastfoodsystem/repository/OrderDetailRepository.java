package com.hcmute.fastfoodsystem.repository;

import com.hcmute.fastfoodsystem.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}
