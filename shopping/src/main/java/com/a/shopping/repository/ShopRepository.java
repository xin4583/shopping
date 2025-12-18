package com.a.shopping.repository;

import com.a.shopping.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShopRepository extends JpaRepository<Shop, Long> {
    Optional<Shop> findByUser_Id(Long userId);
    List<Shop> findByUserId(Long userId);

}
