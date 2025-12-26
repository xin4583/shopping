package com.a.shopping.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 商品属于一个店铺（多对一）
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;
    // 关联：商品属于一个分类（多对一）
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private ProductCategory category;
    private String name; // 商品名称
    private String subtitle; // 副标题
    private BigDecimal price; // 售价
    private Integer stock=0; // 库存
    private Integer sales=0; // 销量
    private Double score;
    private Integer status=0; // 0-待审核，1-下架,2- 上架
    @Column(name = "create_time")
    private LocalDateTime createTime;
    // 一个商品有多个图片（一对多）
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductImage> images = new ArrayList<>();
    @PrePersist
    public void prePersist() {
        this.createTime = LocalDateTime.now();
    }
    // 一个商品有多个SKU（一对多）
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductSku> skus = new ArrayList<>();

}
