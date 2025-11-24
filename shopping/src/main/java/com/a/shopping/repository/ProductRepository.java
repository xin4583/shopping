package com.a.shopping.repository;

import com.a.shopping.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAll(Pageable pageable);
    Page<Product> findByNameLike(Pageable pageable, String fuzzyName);
}
