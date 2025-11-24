package com.a.shopping.service;

import com.a.shopping.entity.Result;
import com.a.shopping.entity.User;
import com.a.shopping.entity.UserDTO;
import com.a.shopping.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private UserRepository userRepository;
    public Result login(UserDTO loginDto) {
        User user = userRepository.findByPhone(loginDto.getPhone());
        if (user == null) {
            return Result.fail("手机号未注册");
        }


        if (!user.getPassword().equals(loginDto.getPassword())) {
            return Result.fail("密码错误");
        }
        return Result.suc(user);
    }
}