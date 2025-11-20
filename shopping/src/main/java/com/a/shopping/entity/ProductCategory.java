package com.a.shopping.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@Entity(name = "product_category")
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "parent_id")

    private Long parentId; // 父分类ID（用于多级分类）
    private String name; // 分类名称
    // private String icon; // 图标URL

    // private Integer sort; // 排序权重

    // 关联：一个分类有多个子分类（自关联一对多）
    @OneToMany(mappedBy = "parentCategory", fetch = FetchType.LAZY)
    private List<ProductCategory> children = new ArrayList<>();

    // 关联：子分类属于一个父分类（多对一）
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", insertable = false, updatable = false) // 避免重复映射parent_id字段
    @JsonIgnore
    private ProductCategory parentCategory;

}