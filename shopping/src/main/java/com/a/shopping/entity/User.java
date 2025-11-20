package com.a.shopping.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增主键
    private Long id;
    private String username; // 用户名
    @Column(unique = true,nullable = false) // 手机号唯一
    private String phone;
    private String email;
    @Column(nullable = false)
    private String password;
    @Lob
    @Column(columnDefinition = "longblob")
    private byte[] avatar;//头像
    // private Integer status; // 0-禁用，1-正常
    private Integer role=0; // 0-用户/商家，1-管理员
    @Column(name = "create_time",nullable = false)
    private LocalDateTime createTime; // 创建时间
    // 一个用户有多个地址
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserAddress> addresses = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductComment> comments = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Shop> shops = new ArrayList<>();
    @PrePersist
    public void prePersist() {
        this.createTime = LocalDateTime.now();
    }
}
