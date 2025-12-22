package com.a.shopping.repository;

import com.a.shopping.entity.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ShopRepository extends JpaRepository<Shop, Long> {
    Optional<Shop> findByUser_Id(Long userId);
    List<Shop> findByUserId(Long userId);
    @Query("SELECT s FROM shop s WHERE " +
            "(s.name LIKE :nameLike OR :nameLike IS NULL) AND " +
            "(s.status = :status OR :status IS NULL) AND " +
            "(s.createTime >= :startTime OR :startTime IS NULL) AND " +
            "(s.createTime <= :endTime OR :endTime IS NULL)")
    Page<Shop> findByCondition(
            @Param("nameLike") String nameLike,
            @Param("status") Integer status,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            Pageable pageable);

}
