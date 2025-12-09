package com.a.shopping.controller;

import com.a.shopping.entity.*;
import com.a.shopping.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

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
        Optional<ProductSku> productSku=productSkuRepository.findById(order.getSku().getId());
        productSku.get().setStock(productSku.get().getStock()-order.getQuantity());
        productSkuRepository.save(productSku.get());
        Optional<Product> product=productRepository.findById(order.getProduct().getId());
        product.get().setSales(product.get().getSales()+order.getQuantity());
        product.get().setStock(product.get().getStock()-order.getQuantity());
        productSkuRepository.save(productSku.get());
        order.setTotalAmount(new BigDecimal(order.getPrice()).multiply(BigDecimal.valueOf(order.getQuantity())));
        order.setPayAmount(new BigDecimal(order.getPrice()).multiply(BigDecimal.valueOf(order.getQuantity())));
        orderRepository.save(order);
        Map<String,Object> map=new HashMap<>();
        map.put("orderId",order.getId());
        map.put("payAmount",order.getPayAmount());
        return Result.suc("订单创建成功",map);
    }
    // 用户端获取订单列表
    @GetMapping("/list1/{userId}")
    public Result getOrdersByUserId(@PathVariable Long userId){
        List<Order> orders = orderRepository.findByUserId(userId);
        if (orders.isEmpty()){
            return Result.fail("没有找到订单");
        }
        List<OrderDTO> orderDTOs = new ArrayList<>(orders.size());
        for (Order order : orders) {
            OrderDTO dto = new OrderDTO();
            dto.setId(order.getId());
            dto.setUserId(order.getUser().getId());
            dto.setShopId(order.getUser().getId());
            dto.setProductId(order.getProduct().getId());
            dto.setSkuId(order.getSku().getId());
            dto.setPrice(order.getPrice());
            dto.setQuantity(order.getQuantity());
            dto.setTotalAmount(order.getTotalAmount());
            dto.setPayAmount(order.getPayAmount());
            dto.setStatus(order.getStatus());
            dto.setCreateTime(order.getCreateTime());
            dto.setPayTime(order.getPayTime());
            dto.setDeliverTime(order.getDeliverTime());
            dto.setReceiveTime(order.getReceiveTime());
            dto.setAddress(order.getUserAddress());
            Product product = productRepository.findProductWithFirstImage(order.getProduct().getId());
            if (product != null) {
                dto.setProductName(product.getName());
                if (!CollectionUtils.isEmpty(product.getImages())) {
                    ProductImage firstImage = product.getImages().get(0);
                    dto.setProductImage(firstImage.getImage());
                }
            }
            orderDTOs.add(dto);
        }
        return Result.suc(orderDTOs);
    }
    // 用户端获取各个状态订单数量
    @GetMapping("/list3/{userId}")
    public Result getOrdersSum(@PathVariable Long userId){
        List<Order> orders = orderRepository.findByUserId(userId);
        if (orders.isEmpty()){
            return Result.fail("没有找到订单");
        }
        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("unpaid", 0);
        resultMap.put("unship", 0);
        resultMap.put("unreceived", 0);
        resultMap.put("completed", 0);
        for (Order order : orders) {
            switch (order.getStatus()) {
                case 1:
                    resultMap.put("unpaid", resultMap.get("unpaid") + 1);
                    break;
                case 2:
                    resultMap.put("unship", resultMap.get("unship") + 1);
                    break;
                case 3:
                    resultMap.put("unreceived", resultMap.get("unreceived") + 1);
                    break;
                case 4:
                    resultMap.put("completed", resultMap.get("completed") + 1);
            }
        }
        return Result.suc(resultMap);
    }
    // 店铺端获取订单列表
    @GetMapping("/list2/{shopId}")
    public Result getOrdersByShopId(@PathVariable Integer shopId) {
        List<Order> orders = orderRepository.findByShopId(shopId);
        return Result.suc(orders, orders.size());
    }
    @PostMapping("/cancel/{id}")
    public Result cancelOrder(@PathVariable UUID id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            return Result.fail("订单不存在");
        }
        order.setStatus(5); // 设置为已取消状态
        Optional<ProductSku> productSku=productSkuRepository.findById(order.getSku().getId());
        productSku.get().setStock(productSku.get().getStock()+order.getQuantity());
        Optional<Product> product=productRepository.findById(order.getProduct().getId());
        product.get().setSales(product.get().getSales()-order.getQuantity());
        product.get().setStock(product.get().getStock()+order.getQuantity());
        orderRepository.save(order);
        productRepository.save(product.get());
        productSkuRepository.save(productSku.get());
        return Result.suc("退单成功");
    }
    @DeleteMapping("/delete/{id}")
    public Result deleteOrder(@PathVariable UUID id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            return Result.suc("订单删除成功");
        }
        return Result.fail("订单不存在");
    }
    @PostMapping("/update")
    public Result updateOrder(@RequestBody Order orderUpdate) {
        if (orderRepository.existsById(orderUpdate.getId())) {
            Order order = orderRepository.findById(orderUpdate.getId()).get();
            order.setStatus(orderUpdate.getStatus());
            switch (orderUpdate.getStatus()) {
                case 2:
                    order.setPayTime(LocalDateTime.now());
                    break;
                case 3:
                    order.setDeliverTime(LocalDateTime.now());
                    break;
                case 4:
                    order.setReceiveTime(LocalDateTime.now());
                    break;
            }
            orderRepository.save(order);
            return Result.suc("订单更新成功");
        }
        return Result.fail("订单不存在");
    }
}
