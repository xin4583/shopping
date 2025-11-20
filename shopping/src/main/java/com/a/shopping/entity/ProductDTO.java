package com.a.shopping.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDTO {
    private Long id;
    private Long shopId;
    private Long categoryId;
    private String name;
    private String subtitle;
    private BigDecimal price;
    private Integer stock;
    private Integer sales;
    private Integer status;
}
