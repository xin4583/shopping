package com.a.shopping.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
@Getter
@Setter
public class UserDTO {
    private String username;
    private String phone;
    private String email;
    private String password;
    private String confirmPassword;
}
