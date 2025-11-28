package com.a.shopping.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
@Getter
@Setter
@Entity(name = "user_history")
public class UserHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 一个用户有多个浏览历史记录
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
    @Column(name = "view_time")
    private LocalDateTime viewTime; // 浏览时间（每次浏览更新为当前时间）
    @PrePersist
    public void prePersist() {
        this.viewTime = LocalDateTime.now();
    }
    @PreUpdate
    public void preUpdate() {
        this.viewTime = LocalDateTime.now(); // 再次浏览时更新时间
    }
}
