package com.a.shopping.controller;

import com.a.shopping.entity.Product;
import com.a.shopping.entity.Result;
import com.a.shopping.entity.User;
import com.a.shopping.entity.UserHistory;
import com.a.shopping.repository.ProductRepository;
import com.a.shopping.repository.UserAddressRepository;
import com.a.shopping.repository.UserHistoryRepository;
import com.a.shopping.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
        userHistory.setUser(user.get());
        userHistory.setProduct(product.get());
        userHistoryRepository.save(userHistory);
        return Result.suc();
    }
    @GetMapping("/list/{userId}")
    public Result getByUserId(@PathVariable Long userId) {
        // 检查用户是否存在
        if (!userRepository.existsById(userId)) {
            return Result.fail("用户不存在");
        }
        List<UserHistory> histories = userHistoryRepository.findByUserIdOrderByViewTimeDesc(userId);
        return Result.suc(histories);
    }
    @DeleteMapping("/delete/{userId}")
    public Result delete(@PathVariable Long userId) {
        if (userHistoryRepository.existsById(userId)) {
            userHistoryRepository.deleteById(userId);
            return Result.suc("删除成功");
        }
        return Result.fail("记录不存在");
    }
    @PostMapping ("/update")
    public Result update(@RequestBody UserHistory history) {
        return userHistoryRepository.findById(history.getId())
                .map(existing -> {
                    // 更新用户关联（如果提供且有效）
                    if (history.getUser() != null && history.getUser().getId() != null) {
                        Optional<User> user = userRepository.findById(history.getUser().getId());
                        user.ifPresent(existing::setUser);
                    }
                    // 更新商品关联（如果提供且有效）
                    if (history.getProduct() != null && history.getProduct().getId() != null) {
                        Optional<Product> product = productRepository.findById(history.getProduct().getId());
                        product.ifPresent(existing::setProduct);
                    }
                    // 自动更新浏览时间（通过@PreUpdate）
                    existing.setViewTime(LocalDateTime.now());
                    userHistoryRepository.save(existing);
                    return Result.suc();
                })
                .orElseGet(() -> Result.fail("记录不存在"));
    }
}
