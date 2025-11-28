
package com.a.shopping.repository;

import com.a.shopping.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findByUserIdAndStatus(Long userId, Integer status);
    List<Order> findByShopId(Integer shopId);
}
