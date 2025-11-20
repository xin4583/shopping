package com.a.shopping.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//商品收藏
@Getter
@Setter
@Entity(name = "user_collection")
public class UserCollection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 多个收藏属于一个用户（多对一）
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user;

    // 收藏的商品（多对一）
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime; // 收藏时间


    @PrePersist
    public void prePersist() {
        this.createTime = LocalDateTime.now();
    }
}
