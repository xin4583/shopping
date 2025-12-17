package com.a.shopping.repository;

import com.a.shopping.entity.SalesStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SalesStatisticsRepository extends JpaRepository<SalesStatistics, Long> {
    // 按日期、商品、店铺查询
    Optional<SalesStatistics> findByStatDateAndProductIdAndShopId(
            LocalDate statDate, Long productId, Long shopId);

    // 按日期、店铺查询（商品为null，即店铺维度）
    Optional<SalesStatistics> findByStatDateAndProductIsNullAndShopId(
            LocalDate statDate, Long shopId);

    // 查询商品历史总营业额（指定日期之前）
    @Query("SELECT SUM(s.totalAmount) FROM sales_statistics s " +
            "WHERE s.product.id = :productId AND s.shop.id = :shopId AND s.statDate < :date")
    Optional<BigDecimal> sumTotalByProductIdAndShopIdBeforeDate(
            @Param("productId") Long productId,
            @Param("shopId") Long shopId,
            @Param("date") LocalDate date);

    // 查询店铺历史总营业额（指定日期之前，商品为null）
    @Query("SELECT SUM(s.totalAmount) FROM sales_statistics s " +
            "WHERE s.shop.id = :shopId AND s.product IS NULL AND s.statDate < :date")
    Optional<BigDecimal> sumTotalByShopIdBeforeDateAndProductIsNull(
            @Param("shopId") Long shopId,
            @Param("date") LocalDate date);

    // 按店铺查询商品每日统计
    List<SalesStatistics> findByShopIdAndProductIsNotNullAndStatDateBetween(
            Long shopId, LocalDate start, LocalDate end);

    // 按店铺查询店铺每日统计
    List<SalesStatistics> findByShopIdAndProductIsNullAndStatDateBetween(
            Long shopId, LocalDate start, LocalDate end);
}