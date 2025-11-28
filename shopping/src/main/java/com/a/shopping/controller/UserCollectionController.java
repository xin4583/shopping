package com.a.shopping.controller;

import com.a.shopping.entity.CollectedProductDTO;
import com.a.shopping.entity.Result;
import com.a.shopping.entity.UserCollection;
import com.a.shopping.repository.ProductRepository;
import com.a.shopping.repository.UserAddressRepository;
import com.a.shopping.repository.UserCollectionRepository;
import com.a.shopping.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/collection")
public class UserCollectionController {
    @Autowired
    UserCollectionRepository userCollectionRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;

    @PostMapping("/add")
    public Result add(@RequestBody UserCollection userCollection) {
        // UserCollection userCollection1 = new UserCollection();
        // userCollection1.setUser(userRepository.findById(userCollection.getUserId()).orElse(null));
        // userCollection1.setProduct(productRepository.findById(userCollection.getProductId()).orElse(null));
        // UserCollection userCollection1=userCollectionRepository.save(userCollection);
        // return userCollection1==null?Result.fail("添加失败"):Result.suc();
        UserCollection userCollection1 = new UserCollection();
        userCollection1.setUser(userCollection.getUser());
        userCollection1.setProduct(userCollection.getProduct());
        userCollectionRepository.save(userCollection1);
        return Result.suc();
    }
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable long id) {
        userCollectionRepository.deleteById(id);
        return Result.suc();
    }
    @GetMapping("/list/{userId}")
    public Result list(@PathVariable long userId) {
        List<UserCollection> collections = userCollectionRepository.findByUserId(userId);
        List<CollectedProductDTO> dtoList = collections.stream()
                .map(CollectedProductDTO::new) // 调用新增的构造方法，自动获取收藏ID
                .collect(Collectors.toList());
        return Result.suc(dtoList);
    }

}
