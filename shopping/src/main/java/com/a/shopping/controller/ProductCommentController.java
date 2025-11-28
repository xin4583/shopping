package com.a.shopping.controller;

import com.a.shopping.entity.ProductComment;
import com.a.shopping.entity.ProductCommentDTO;
import com.a.shopping.entity.Result;
import com.a.shopping.repository.ProductCommentRepository;
import com.a.shopping.repository.ProductRepository;
import com.a.shopping.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productComment")
public class ProductCommentController {
    @Autowired
    ProductCommentRepository productCommentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;
    @PostMapping("/add")
    public Result add(@RequestBody ProductCommentDTO productCommentDTO) {
        ProductComment productComment = new ProductComment();
        productComment.setContent(productCommentDTO.getContent());
        productComment.setProduct(productRepository.findById(productCommentDTO.getProductId()).get());
        productComment.setUser(userRepository.findById(productCommentDTO.getUserId()).get());
        productComment.setScore(productCommentDTO.getScore());
        ProductComment productComment1=productCommentRepository.save(productComment);
        return productComment1!=null?Result.suc():Result.fail();
    }
    @GetMapping("/list/{productId}")
    public Result list(@PathVariable Long productId) {
        List<ProductComment> productComments = productCommentRepository.findByProductId(productId);
        ProductCommentDTO[] productCommentDTOS = new ProductCommentDTO[productComments.size()];
        for (int i = 0; i < productComments.size(); i++) {
            ProductCommentDTO productCommentDTO = new ProductCommentDTO();
            productCommentDTO.setId(productComments.get(i).getId());
            productCommentDTO.setUserImage(userRepository.findById(productComments.get(i).getUser().getId()).get().getAvatar());
            productCommentDTO.setContent(productComments.get(i).getContent());
            productCommentDTO.setCreateTime(productComments.get(i).getCreateTime());
            productCommentDTO.setScore(productComments.get(i).getScore());
            productCommentDTO.setProductId(productComments.get(i).getProduct().getId());
            productCommentDTO.setUserId(productComments.get(i).getUser().getId());
            productCommentDTO.setUsername(userRepository.findById(productComments.get(i).getUser().getId()).get().getUsername());
            productCommentDTOS[i] = productCommentDTO;
        }
        return Result.suc(productCommentDTOS);
    }
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        productCommentRepository.deleteById(id);
        return productCommentRepository.existsById(id)?Result.fail():Result.suc();
    }

}
