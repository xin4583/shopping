package com.a.shopping.repository;

import com.a.shopping.entity.ProductComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductCommentRepository extends JpaRepository<ProductComment, Long> {
    List<ProductComment> findByProductId(Long productId);
}
