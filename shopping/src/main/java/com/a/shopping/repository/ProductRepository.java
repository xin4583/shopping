package com.a.shopping.repository;

import com.a.shopping.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAll(Pageable pageable);
    Page<Product> findByNameLike(Pageable pageable, String fuzzyName);
    @Query("SELECT p FROM product p LEFT JOIN FETCH p.images WHERE p.id = :productId")
    Product findProductWithFirstImage(@Param("productId") Long productId);
}
