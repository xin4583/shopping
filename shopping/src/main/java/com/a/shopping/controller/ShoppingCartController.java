package com.a.shopping.controller;

import com.a.shopping.entity.*;
import com.a.shopping.repository.ProductRepository;
import com.a.shopping.repository.ProductSkuRepository;
import com.a.shopping.repository.ShoppingCartRepository;
import com.a.shopping.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/shoppingcart")
public class ShoppingCartController {
    @Autowired
    ShoppingCartRepository shoppingCartRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    private ProductSkuRepository productSkuRepository;
    @PostMapping("/add")
    public Result add(@RequestBody ShoppingCart param) {
        if (param.getUser() == null || param.getProduct() == null
                || param.getSku() == null || param.getQuantity() == null || param.getQuantity() <= 0) {
            return Result.fail("参数不完整或数量非法");
        }
        ShoppingCart exist = shoppingCartRepository.findByUserIdAndSkuId(param.getUser().getId(), param.getSku().getId()).orElse(null);
        if (exist != null) {
            exist.setQuantity(exist.getQuantity() + param.getQuantity());
            shoppingCartRepository.save(exist);
            return Result.suc();
        }
        shoppingCartRepository.save(param);
        return Result.suc();
    }
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable long id) {
        shoppingCartRepository.deleteById(id);
        return Result.suc();
    }
    @GetMapping("/list/{userId}")
    public Result list(@PathVariable long userId) {
        List<ShoppingCart> shoppingCarts = shoppingCartRepository.findByUserId(userId);
        List<ShoppingCartDTO> dtoList = shoppingCarts.stream()
                .map(ShoppingCartDTO::new)
                .collect(Collectors.toList());
        return Result.suc(dtoList);
    }
    @PutMapping("/update")
    public Result update(@RequestBody ShoppingCart param) {
        Long cartId   = param.getId();
        Integer newQty = param.getQuantity();
        Long newSkuId = param.getSku() != null ? param.getSku().getId() : null;

        /* 1. 基本校验 */
        if (cartId == null) return Result.fail("购物车ID不能为空");
        ShoppingCart cart = shoppingCartRepository
                .findById(cartId)
                .orElse(null);
        if (cart == null) return Result.fail("记录不存在");

        /* 2. 改数量 */
        if (newQty != null && newQty > 0) {
            cart.setQuantity(newQty);
        }

        /* 3. 改规格（skuId 变化才处理） */
        if (newSkuId != null && !newSkuId.equals(cart.getSku().getId())) {
            /* 3.1 校验新规格存在 */
            ProductSku newSku = productSkuRepository
                    .findById(newSkuId)
                    .orElse(null);
            if (newSku == null) return Result.fail("新规格不存在");

            Long userId = cart.getUser().getId();
            Optional<ShoppingCart> sameSkuCart = shoppingCartRepository
                    .findByUserIdAndSkuId(userId, newSkuId);

            if (sameSkuCart.isPresent()) {
                /* 3.2 已存在 -> 合并数量后删除当前记录 */
                ShoppingCart exist = sameSkuCart.get();
                exist.setQuantity(exist.getQuantity() + cart.getQuantity());
                shoppingCartRepository.delete(cart);        // 删旧
                shoppingCartRepository.save(exist);         // 保存合并后
                return Result.suc();
            } else {
                cart.setSku(newSku);
            }
        }
        shoppingCartRepository.save(cart);
        return Result.suc();
    }
}
