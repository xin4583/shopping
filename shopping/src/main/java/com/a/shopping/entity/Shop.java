package com.a.shopping.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity(name = "shop")
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user;
    private String name;
    @Lob
    @Basic(fetch = FetchType.LAZY)   // 查询商品列表时不加载照片
    @Column(name = "logo", nullable = true, columnDefinition = "LONGBLOB")
    private byte[] logo;
    private String description;//描述
    private Integer status=0;//0 未审核 / 1 营业 / 2 封禁
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "license_url", nullable = true, columnDefinition = "LONGBLOB")
    private byte[] licenseUrl;//营业执照
    private String idcardNo;//身份证号
    private LocalDateTime createTime;
    @PrePersist
    public void prePersist() {
        this.createTime = LocalDateTime.now();
    }
    // @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // private List<Product> products = new ArrayList<>();


}
