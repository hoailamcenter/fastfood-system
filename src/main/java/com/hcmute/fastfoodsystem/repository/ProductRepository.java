package com.hcmute.fastfoodsystem.repository;

import com.hcmute.fastfoodsystem.model.Category;
import com.hcmute.fastfoodsystem.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByCategory(Category category);
}
