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
            "WHERE p.name LIKE :fuzzyName " +
            "AND (:status IS NULL OR p.status = :status)",
            countQuery = "SELECT COUNT(p) FROM product p WHERE p.name LIKE :fuzzyName AND (:status IS NULL OR p.status = :status)") // 计数查询同步新增状态条件
    Page<Product> findByNameLikeAndStatusWithRelations(
            @Param("fuzzyName") String fuzzyName,
            @Param("status") Integer status,
            Pageable pageable);
    @Query(value = "SELECT p FROM product p " +
            "JOIN FETCH p.shop s " +
            "JOIN FETCH p.category c " +
            "LEFT JOIN FETCH p.images i " +
            "WHERE p.shop.id = :shopId",
            countQuery = "SELECT COUNT(p) FROM product p WHERE p.shop.id = :shopId")
    Page<Product> findByShopIdWithRelations(@Param("shopId") Long shopId, Pageable pageable);
    @Query(value = "SELECT p FROM product p " +
            "JOIN FETCH p.shop s " +
            "JOIN FETCH p.category c " +
            "LEFT JOIN FETCH p.images i " +
            "WHERE (:productNameLike IS NULL OR p.name LIKE :productNameLike) " +
            "AND (:shopNameLike IS NULL OR s.name LIKE :shopNameLike) " +
            "AND (:categoryId IS NULL OR c.id = :categoryId) " +
            "AND (:status IS NULL OR p.status = :status)",
            countQuery = "SELECT COUNT(p) FROM product p " +
                    "JOIN p.shop s " +
                    "JOIN p.category c " +
                    "WHERE (:productNameLike IS NULL OR p.name LIKE :productNameLike) " +
                    "AND (:shopNameLike IS NULL OR s.name LIKE :shopNameLike) " +
                    "AND (:categoryId IS NULL OR c.id = :categoryId) " +
                    "AND (:status IS NULL OR p.status = :status)")
    Page<Product> findByMultiCondition(
            @Param("productNameLike") String productNameLike,
            @Param("shopNameLike") String shopNameLike,
            @Param("categoryId") Long categoryId,
            @Param("status") Integer status,
            Pageable pageable);
    @Query("SELECT p FROM product p " +
            "LEFT JOIN p.shop " +
            "LEFT JOIN p.images " +
            "WHERE (:name IS NULL OR p.name LIKE :name) " +
            "AND (:status IS NULL OR p.status = :status) " +
            "ORDER BY FUNCTION('RAND')") // 关键：移除 RAND 后的括号
    Page<Product> findRandomProducts(
            @Param("name") String name,
            @Param("status") Integer status,
            Pageable pageable);
}
