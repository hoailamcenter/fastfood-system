package com.hcmute.fastfoodsystem.repository;

import com.hcmute.fastfoodsystem.model.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItems, Long> {
    @Query(value = "update cart_items set cart_id = null where cart_id = ?1", nativeQuery = true)
    void deleteCartItemById(Long cartId);
}
