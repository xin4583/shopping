package com.a.shopping.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CollectedProductDTO {
    private Long id;
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
    public CollectedProductDTO(UserCollection userCollection) {
        this.id = userCollection.getId(); // UserCollection.id 是收藏主键
        // 商品信息：从收藏关联的商品中获取（和原逻辑一致）
        Product product = userCollection.getProduct();
        this.productId = product.getId();
        this.productName = product.getName();
        this.subtitle = product.getSubtitle();
        this.price = product.getPrice();
        this.sales = product.getSales();
        this.status = product.getStatus();
        this.image = product.getImages() != null && !product.getImages().isEmpty()
                ? product.getImages().get(0).getImage()
                : null;
        // 店铺信息（和原逻辑一致）
        if (product.getShop() != null) {
            this.shopId = product.getShop().getId().longValue();
            this.shopName = product.getShop().getName();
        }
    }
}