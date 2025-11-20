package com.a.shopping.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
public class ShopCreateDTO {
    private Long userId;      // 店主用户ID
    private String name;         // 店铺名称
    private String description;  // 描述
    // @Pattern(regexp = "^[1-9]\\d{5}(18|19|20)\\d{2}(0[1-9]|1[0-2])\\d{2}\\d{3}[\\dX]$",
    //          message = "身份证号格式错误")
    private String idcardNo;     // 身份证号
}