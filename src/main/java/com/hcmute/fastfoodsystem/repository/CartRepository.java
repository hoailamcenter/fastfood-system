package com.hcmute.fastfoodsystem.repository;

import com.hcmute.fastfoodsystem.model.Cart;
import com.hcmute.fastfoodsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
