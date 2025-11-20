package com.a.shopping.repository;

import com.a.shopping.entity.ProductSku;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface ProductSkuRepository extends JpaRepository<ProductSku, Long> {
    List<ProductSku> findAllByProductId(Long productId);
}
