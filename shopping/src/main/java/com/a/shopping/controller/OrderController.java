package com.a.shopping.controller;

import com.a.shopping.entity.Order;
import com.a.shopping.entity.ProductSku;
import com.a.shopping.entity.Result;
import com.a.shopping.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ShopRepository shopRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductSkuRepository productSkuRepository;
    @PostMapping("/add")
    public Result addOrder(@RequestBody Order order) {
        if (order.getUser() == null || order.getUser().getId() == null
                || order.getShop() == null || order.getShop().getId() == null
                || order.getProduct() == null || order.getProduct().getId() == null
                || order.getSku() == null || order.getSku().getId() == null) {
            return Result.fail("关联信息不完整（用户/店铺/商品/SKU ID不能为空）");
        }
        Order savedOrder = orderRepository.save(order);
        return Result.suc("订单创建成功", savedOrder);
    }
    @GetMapping("/list/{userId}")
    public Result getOrdersByUserId(@PathVariable Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return Result.suc(orders);
    }
    @GetMapping("/list/{shopId}")
    public Result getOrdersByShopId(@PathVariable Integer shopId) {
        List<Order> orders = orderRepository.findByShopId(shopId);
        return Result.suc(orders, orders.size());
    }
    @DeleteMapping("delete/{id}")
    public Result deleteOrder(@PathVariable UUID id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            return Result.suc("订单删除成功");
        } else {
            return Result.fail("订单不存在");
        }
    }
    @PostMapping("/update")
    public Result updateOrder(@RequestBody Order orderUpdate) {
        if (orderRepository.existsById(orderUpdate.getId())) {
            orderRepository.save(orderUpdate);
            return Result.suc("订单更新成功");
        }
        return Result.fail("订单不存在");
    }
}
