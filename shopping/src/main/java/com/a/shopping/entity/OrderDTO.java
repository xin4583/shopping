package com.a.shopping.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
@Data
public class OrderDTO {
    private UUID id;
    private Long userId;
    private Long shopId;
    private Long productId;
    private Long skuId;
    private UserAddress address;
    private String productName;
    @Lob
    @Column(columnDefinition = "longblob")
    private byte[] productImage;
    private String price;
    private Integer quantity;
    private BigDecimal totalAmount;
    private BigDecimal payAmount;
    private Integer status = 0;
    private LocalDateTime createTime;
    private LocalDateTime payTime;
    private LocalDateTime deliverTime;
    private LocalDateTime receiveTime;
}