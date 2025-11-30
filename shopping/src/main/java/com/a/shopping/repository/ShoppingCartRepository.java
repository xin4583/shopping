package com.a.shopping.repository;

import com.a.shopping.entity.ShoppingCart;
import com.a.shopping.entity.UserCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    @Query("SELECT uc FROM shopping_cart uc " +
            "JOIN FETCH uc.product p " +  // 加载商品
            "JOIN FETCH p.shop s "        +  // 加载商品所属店铺
            "WHERE uc.user.id = :userId")
    List<ShoppingCart> findByUserId(@Param("userId") Long userId);
}
