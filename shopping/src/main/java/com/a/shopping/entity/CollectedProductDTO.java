package com.a.shopping.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CollectedProductDTO {
    // 商品基本信息
    private Long productId;       // 商品ID
    private String productName;   // 商品名称
    private String subtitle;      // 副标题
    private BigDecimal price;     // 售价
    private Integer sales;        // 销量
    private Integer status;       // 状态（0-待审核，1-下架，2-上架）
    
    // 店铺精简信息（直接包含id和name，不单独创建DTO）
    private Long shopId;
    private String shopName;
    @Lob
    @Column(columnDefinition = "longblob")
    private byte[] image;
    // 从Product实体转换为DTO的构造方法
    public CollectedProductDTO(Product product) {
        // 商品信息
        this.productId = product.getId();
        this.productName = product.getName();
        this.subtitle = product.getSubtitle();
        this.price = product.getPrice();
        this.sales = product.getSales();
        this.status = product.getStatus();
        this.image = product.getImages().get(0).getImage();
        // 店铺信息（避免空指针）
        if (product.getShop() != null) {
            this.shopId = Long.valueOf(product.getShop().getId());
            this.shopName = product.getShop().getName();
        }
    }
}