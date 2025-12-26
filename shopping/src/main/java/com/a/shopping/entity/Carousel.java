package com.a.shopping.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import java.time.LocalDateTime;

@Data
@Entity(name = "carousel")
@DynamicInsert
@DynamicUpdate
public class Carousel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title; // 轮播图标题

    @Lob
    @Column(columnDefinition = "longblob", nullable = false)
    private byte[] image; // 轮播图图片

    private Integer sort; // 排序权重（数字越小越靠前）

    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @PrePersist
    public void prePersist() {
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updateTime = LocalDateTime.now();
    }
}