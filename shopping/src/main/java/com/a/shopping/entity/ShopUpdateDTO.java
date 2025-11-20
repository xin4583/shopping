package com.a.shopping.entity;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ShopUpdateDTO {
    private Long shopId;
    private String name;
    private String description;
    // @Pattern(regexp = "^[1-9]\\d{5}(18|19|20)\\d{2}(0[1-9]|1[0-2])\\d{2}\\d{3}[\\dX]$",
    //          message = "身份证号格式错误")
    private String idcardNo; // 可选更新
    private Integer status; // 可选更新
}