package com.hcmute.fastfoodsystem.repository;

import com.hcmute.fastfoodsystem.model.Category;
import com.hcmute.fastfoodsystem.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByCategory(Category category);
    @Query("select p from Product p where p.productName like %?1%")
    List<Product> searchProducts(String keyword);
    List<Product> findByPriceBetween(int minPrice, int maxPrice);

}
