package com.a.shopping.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
public class SalesStatisticsDTO {
    private Long id;
    private LocalDate statDate;
    private Long productId;
    private String productName;
    private Long  shopId;
    private BigDecimal dailyAmount;
    private BigDecimal totalAmount;
}
