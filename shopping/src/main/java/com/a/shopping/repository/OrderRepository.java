
package com.a.shopping.repository;

import com.a.shopping.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findByUserId(Long userId);
    List<Order> findByShopId(Long shopId);
    Optional<Order> findById(UUID orderId);
}
