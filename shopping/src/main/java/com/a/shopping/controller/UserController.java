package com.a.shopping.controller;

import com.a.shopping.entity.Result;
import com.a.shopping.entity.User;
import com.a.shopping.entity.UserAddress;
import com.a.shopping.entity.UserDTO;
import com.a.shopping.repository.UserRepository;
import com.a.shopping.service.RegisterService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RegisterService registerService;
    @PostMapping("/register")
    public Result addUser(@RequestPart("avatar") MultipartFile avatar,
                          @RequestPart("user") UserDTO dto) throws IOException {
        if (avatar.getSize() > 5 * 1024 * 1024) {
            return  Result.fail("头像不能超过5MB");
        }
        if (!avatar.getContentType().startsWith("image/")) {
            return   Result.fail("只能上传图片");
        }
        return registerService.register(dto, avatar);
    }
    @GetMapping("/list/{id}")
    public Result list(@PathVariable Long id){
        Map<String,Object> user =  userRepository.findBaseInfoById(id);
        if (user==null) {
            return Result.fail("用户不存在");
        }
        return Result.suc(user);
    }
    @GetMapping("/{id}/avatar")
    public void avatar(@PathVariable Long id, HttpServletResponse resp) throws IOException {
        byte[] bytes = registerService.getAvatar(id);
        if (bytes == null || bytes.length == 0) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        resp.setContentType(MediaType.IMAGE_JPEG_VALUE);
        resp.getOutputStream().write(bytes);
    }
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id){
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return Result.fail("用户不存在");
        }
        userRepository.deleteById(id);
        return Result.suc();
    }
    @PostMapping("/update/{id}")
    public Result update(@PathVariable Long id, @RequestBody UserDTO dto){
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return Result.fail("用户不存在");
        }
        user.setUsername(dto.getUsername());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        User saveuser=userRepository.save(user);
        return saveuser!=user?Result.fail("更新失败"):Result.suc();
    }
    @PostMapping("/update/avatar/{id}")
    public Result updateAvatar(@PathVariable Long id, @RequestPart("avatar") MultipartFile avatar) throws IOException {
        if (avatar.getSize() > 5 * 1024 * 1024) {
            return  Result.fail("头像不能超过5MB");
        }
        if (!avatar.getContentType().startsWith("image/")) {
            return   Result.fail("只能上传图片");
        }
        User user = userRepository.findById(
                id).orElse(null);
        if (user == null) {
            return Result.fail("用户不存在");
        }
        user.setAvatar(avatar.getBytes());
        User saveuser=userRepository.save(user);
        return saveuser!=user?Result.fail("更新失败"):Result.suc();
    }
    // @GetMapping("/list/address/{id}")
    // public Result listAddress(@PathVariable Long id){
    //     Optional<User> userOpt = userRepository.findById(id);
    //     if (userOpt.isEmpty()) {
    //         return Result.fail("用户不存在");
    //     }
    //
    //     List<UserAddress> addresses = userOpt.get().getAddresses();
    //     List<Map<String, Object>> result = addresses.stream().map(addr -> {
    //         Map<String, Object> map = new HashMap<>();
    //         map.put("id", addr.getId());
    //         map.put("receiver", addr.getReceiver());
    //         map.put("phone", addr.getPhone());
    //         map.put("province", addr.getProvince());
    //         map.put("city", addr.getCity());
    //         map.put("detailAddress", addr.getDetailAddress());
    //         map.put("isDefault", addr.getIsDefault());
    //         return map;
    //     }).collect(Collectors.toList());
    //
    //     return Result.suc(result);
    // }
}
