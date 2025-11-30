package com.a.shopping.controller;

import com.a.shopping.entity.*;
import com.a.shopping.repository.ProductRepository;
import com.a.shopping.repository.ShoppingCartRepository;
import com.a.shopping.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
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
    @PostMapping("/add")
    public Result add(@RequestBody ShoppingCart shoppingCart) {
        ShoppingCart shoppingCart1 = new ShoppingCart();
        shoppingCart1.setUser(shoppingCart.getUser());
        shoppingCart1.setProduct(shoppingCart.getProduct());
        shoppingCartRepository.save(shoppingCart1);
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

}
