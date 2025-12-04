package com.a.shopping.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class ShoppingCartDTO {
    private Long id;
    private Long productId;       // 商品ID
    private String productName;   // 商品名称
    private String subtitle;      // 副标题
    private BigDecimal price;     // 售价
    private Integer sales;        // 销量
    private Integer status;       // 状态（0-待审核，1-下架，2-上架）
    private Long shopId;
    private String shopName;
    private Long skuId;         // 规格ID
    private String specs;       // 规格 JSON
    private Integer quantity;
    @Lob
    @Column(columnDefinition = "longblob")
    private byte[] image;
    public ShoppingCartDTO(ShoppingCart shoppingCart) {
        this.id = shoppingCart.getId();
        Product product = shoppingCart.getProduct();
        this.quantity = shoppingCart.getQuantity();
        this.productId = product.getId();
        this.productName = product.getName();
        this.subtitle = product.getSubtitle();
        this.price = product.getPrice();
        this.sales = product.getSales();
        this.status = product.getStatus();
        this.image = product.getImages() != null && !product.getImages().isEmpty()
                ? product.getImages().get(0).getImage()
                : null;
        if (product.getShop() != null) {
            this.shopId = product.getShop().getId().longValue();
            this.shopName = product.getShop().getName();
        }
        ProductSku s = shoppingCart.getSku();
        this.skuId = s.getId();
        this.specs = s.getSpecs();
    }
}
