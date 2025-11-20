package com.a.shopping.entity;



import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "user_address")
public class UserAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 关联：多个地址属于一个用户（多对一）
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false) // 外键字段
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user;
    @Column(nullable = false)
    private String receiver;// 收件人
    @Column(nullable = false)
    private String phone;
    @Column(nullable = false)
    private String province;//省
    @Column(nullable = false)
    private String city;//市
    @Column(name = "detail_address",nullable = false)
    private String detailAddress;//具体地址
    @Column(name = "is_default")
    private Boolean isDefault; // 是否默认地址

}