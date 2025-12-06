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
    @Query(value = "SELECT p FROM product p " +
            "JOIN FETCH p.shop s " +          // 加载店铺（shopId/shopName/shopLogo）
            "JOIN FETCH p.category c " +      // 加载分类（取categoryId）
            "LEFT JOIN FETCH p.images i " +   // 左连接加载图片（允许无图商品）
            // "LEFT JOIN FETCH p.skus sk " +    // 左连接加载SKU（允许无SKU商品）
            "WHERE p.name LIKE :fuzzyName",
            countQuery = "SELECT COUNT(p) FROM product p WHERE p.name LIKE :fuzzyName") // 单独计数查询（避免关联影响分页总数）
    Page<Product> findByNameLikeWithRelations(@Param("fuzzyName") String fuzzyName, Pageable pageable);
    @Query(value = "SELECT p FROM product p " +
            "JOIN FETCH p.shop s " +
            "JOIN FETCH p.category c " +
            "LEFT JOIN FETCH p.images i " +
            "WHERE p.shop.id = :shopId",
            countQuery = "SELECT COUNT(p) FROM product p WHERE p.shop.id = :shopId")
    Page<Product> findByShopIdWithRelations(@Param("shopId") Long shopId, Pageable pageable);
}
