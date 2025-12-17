package com.a.shopping.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity(name = "sales_statistics")
public class SalesStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "stat_date", nullable = false)
    private LocalDate statDate; // 统计日期（按天）
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product; // 关联商品（可为null，用于店铺维度统计）
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop; // 关联店铺（必选）
    @Column(name = "daily_amount", nullable = false)
    private BigDecimal dailyAmount = BigDecimal.ZERO; // 当日营业额
    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount = BigDecimal.ZERO; // 累计总营业额（截至当日）
}