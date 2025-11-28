package com.a.shopping.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ProductCommentDTO {
    private Integer id;
    private String content;
    private byte[] userImage; // 商品图片
    private Long productId; // 商品ID
    private Long userId;    // 用户ID
    private Double score;
    private LocalDateTime createTime;
    private String username;
    // 可添加用户名、商品名等扩展字段
}