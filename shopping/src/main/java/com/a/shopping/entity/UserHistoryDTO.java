package com.a.shopping.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 浏览历史返回DTO
 */
@Data
public class UserHistoryDTO {
    private Long historyId;
    private Long productId;
    private String productName;
    private BigDecimal productPrice;
    @Lob
    @Column(columnDefinition = "longblob")
    private byte[] image;
    private LocalDateTime viewTime;
}