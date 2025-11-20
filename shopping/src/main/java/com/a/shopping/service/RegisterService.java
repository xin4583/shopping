package com.a.shopping.service;

import com.a.shopping.entity.Result;
import com.a.shopping.entity.User;
import com.a.shopping.entity.UserDTO;
import com.a.shopping.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class RegisterService {
    @Autowired
    private UserRepository userRepository;
    public Result register(UserDTO userDto, MultipartFile avatar) throws IOException {
        if (!userDto.getPassword().equals(userDto.getConfirmPassword())){
            return Result.fail("两次密码不一致");
        }
        if (userRepository.findByPhone(userDto.getPhone()) != null){
            return Result.fail("手机号已存在");
        }
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPhone(userDto.getPhone());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setAvatar(avatar.getBytes());
        userRepository.save(user);
        return Result.suc();
    }
    public byte[] getAvatar(Long userId) {
        return userRepository.findById(userId)
                .map(User::getAvatar)
                .orElse(null);
    }
}
