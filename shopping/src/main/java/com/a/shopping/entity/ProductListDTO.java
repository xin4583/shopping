package com.a.shopping.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProductListDTO {
    private Long id;
    private Long shopId;
    private String shopName;
    private byte[] shopLogo;
    private Long categoryId;
    private String name;
    private String subtitle; // 副标题
    private BigDecimal price; // 售价
    private Integer stock; // 库存
    private Integer sales=0; // 销量
    private Integer status=0; // 0-待审核，1-下架,2-上架
    private Double score;
    private LocalDateTime createTime;
    // private List<ProductImage> images = new ArrayList<>();
    private byte[] productImg;
    public void prePersist() {
        this.createTime = LocalDateTime.now();
    }
    private List<ProductSku> skus = new ArrayList<>();

}
