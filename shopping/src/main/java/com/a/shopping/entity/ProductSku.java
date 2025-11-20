package com.a.shopping.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;

@Data

@Entity(name = "product_sku")
@DynamicInsert
@DynamicUpdate
public class ProductSku {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 规格ID（主键）

    // 关联：规格所属商品（多对一）
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    private Product product;

    @Column(columnDefinition = "JSON", nullable = false)
    private String specs; // 规格信息（JSON格式，如{"颜色":"红色","尺寸":"XL"}）

    @Column(nullable = false)
    private BigDecimal price; // 规格单价（非空）

    @Column(nullable = false)
    private Integer stock = 0; // 规格库存（非空）

    @Column(name = "sku_code", unique = true, nullable = false)
    private String skuCode; // 规格唯一编码（非空，如"PROD123_RED_XL"）
}