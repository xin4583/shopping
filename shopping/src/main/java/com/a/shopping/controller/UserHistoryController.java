package com.a.shopping.controller;

import com.a.shopping.entity.*;
import com.a.shopping.repository.ProductRepository;
import com.a.shopping.repository.UserAddressRepository;
import com.a.shopping.repository.UserHistoryRepository;
import com.a.shopping.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/userHistory")
public class UserHistoryController {
    @Autowired
    UserHistoryRepository userHistoryRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;
    @PostMapping("/add")
    public Result createUserHistory(@RequestBody UserHistory userHistory) {
        // 验证用户是否存在
        Optional<User> user = userRepository.findById(userHistory.getUser().getId());
        if (user.isEmpty()) {
            return Result.fail("用户不存在");
        }
        // 验证商品是否存在
        Optional<Product> product = productRepository.findById(userHistory.getProduct().getId());
        if (product.isEmpty()) {
            return Result.fail("商品不存在");
        }
        Optional<UserHistory> existHistory = userHistoryRepository.findByUserIdAndProductId(
                userHistory.getUser().getId(),
                userHistory.getProduct().getId()
        );

        if (existHistory.isPresent()) {
            // 如果记录存在，更新浏览时间为当前时间
            UserHistory historyToUpdate = existHistory.get();
            historyToUpdate.setViewTime(LocalDateTime.now()); // 手动更新时间
            userHistoryRepository.save(historyToUpdate); // 保存更新
        } else {
            // 如果记录不存在，创建新记录
            userHistory.setUser(user.get());
            userHistory.setProduct(product.get());
            userHistoryRepository.save(userHistory);
        }
        return Result.suc();
    }
    @GetMapping("/list/{userId}")
    public Result getByUserId(@PathVariable Long userId) {
        // 检查用户是否存在
        if (!userRepository.existsById(userId)) {
            return Result.fail("用户不存在");
        }
        List<UserHistory> histories = userHistoryRepository.findByUserIdOrderByViewTimeDesc(userId);
        List<UserHistoryDTO> historyDTOs = histories.stream()
                .map(history -> {
                    UserHistoryDTO dto = new UserHistoryDTO();
                    // 浏览历史相关字段
                    dto.setHistoryId(history.getId()); // 浏览历史ID
                    dto.setViewTime(history.getViewTime()); // 浏览时间（可选，可删除）
                    // 商品相关字段（需判空，避免NPE）
                    Product product = history.getProduct();
                    if (product != null) {
                        dto.setProductId(product.getId());
                        dto.setProductName(product.getName());
                        dto.setProductPrice(product.getPrice());
                        dto.setImage(product.getImages().get(0).getImage());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
        return Result.suc(historyDTOs);
    }
    @DeleteMapping("/delete/{Id}")
    public Result delete(@PathVariable Long Id) {
        if (userHistoryRepository.existsById(Id)) {
            userHistoryRepository.deleteById(Id);
            return Result.suc("删除成功");
        }
        return Result.fail("记录不存在");
    }
    // @PostMapping ("/update")
    // public Result update(@RequestBody UserHistory history) {
    //     return userHistoryRepository.findById(history.getId())
    //             .map(existing -> {
    //                 // 更新用户关联（如果提供且有效）
    //                 if (history.getUser() != null && history.getUser().getId() != null) {
    //                     Optional<User> user = userRepository.findById(history.getUser().getId());
    //                     user.ifPresent(existing::setUser);
    //                 }
    //                 // 更新商品关联（如果提供且有效）
    //                 if (history.getProduct() != null && history.getProduct().getId() != null) {
    //                     Optional<Product> product = productRepository.findById(history.getProduct().getId());
    //                     product.ifPresent(existing::setProduct);
    //                 }
    //                 // 自动更新浏览时间（通过@PreUpdate）
    //                 existing.setViewTime(LocalDateTime.now());
    //                 userHistoryRepository.save(existing);
    //                 return Result.suc();
    //             })
    //             .orElseGet(() -> Result.fail("记录不存在"));
    // }
}
