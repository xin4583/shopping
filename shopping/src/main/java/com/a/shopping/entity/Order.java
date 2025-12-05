package com.a.shopping.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity(name = "order")
@DynamicInsert
@DynamicUpdate
public class Order {
    @Id
    private UUID id=UUID.randomUUID(); // 订单号（主键）
    // 关联：订单所属用户（多对一）
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 关联：订单所属店铺（多对一）
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    // 关联：商品规格（多对一，冗余skuSpecs避免规格变更影响订单）
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sku_id", nullable = false)
    private ProductSku sku;
    private String price;//单价
    private Integer quantity;//数量
    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount; // 订单总金额（非空）
    @Column(name = "pay_amount", nullable = false)
    private BigDecimal payAmount; // 实付金额（非空，扣除优惠券/折扣后）
    @Column(nullable = false)
    private Integer status = 1; // 订单状态：0-待支付，1-待发货，2-待收货，3-已完成，4-已取消（默认待支付）
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime; // 创建时间（禁止更新）
    @Column(name = "pay_time")
    private LocalDateTime payTime; // 支付时间
    @Column(name = "deliver_time")
    private LocalDateTime deliverTime; // 发货时间

    @Column(name = "receive_time")
    private LocalDateTime receiveTime; // 确认收货时间

    // 自动填充创建时间
    @PrePersist
    public void prePersist() {
        this.createTime = LocalDateTime.now();
    }
}