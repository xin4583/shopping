package com.a.shopping.controller;

import com.a.shopping.entity.Product;
import com.a.shopping.entity.ProductImage;
import com.a.shopping.entity.Result;
import com.a.shopping.repository.ProductImageRepository;
import com.a.shopping.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/productImage")
public class ProductImageController {
    @Autowired
    private ProductImageRepository  productImageRepository;
    @Autowired
    private ProductRepository productRepository;
    @PostMapping("/add")
    public Result add(@RequestParam Long productId, @RequestParam MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return Result.fail("请选择图片文件");
            }
            Optional<Product> productOpt = productRepository.findById(productId);
            if (productOpt.isEmpty()) {
                return Result.fail("关联的商品不存在");
            }
            ProductImage productImage = new ProductImage();
            productImage.setImage(file.getBytes());  // 转换文件为字节数组
            productImage.setProduct(productOpt.get());
            productImageRepository.save(productImage);
            return Result.suc("图片上传成功");
        } catch (IOException e) {
            return Result.fail("文件处理失败：" + e.getMessage());
        }
    }
    @PostMapping("/update/{id}")
    public Result update(@PathVariable Long id, @RequestParam MultipartFile file) {
        try {
            Optional<ProductImage> imageOpt = productImageRepository.findById(id);
            if (imageOpt.isEmpty()) {
                return Result.fail("图片不存在");
            }
            if (file.isEmpty()) {
                return Result.fail("请选择新图片文件");
            }
            ProductImage image = imageOpt.get();
            image.setImage(file.getBytes());
            productImageRepository.save(image);
            return Result.suc("图片更新成功");
        } catch (IOException e) {
            return Result.fail("文件处理失败：" + e.getMessage());
        }
    }
    @GetMapping("/list/{productId}")
    public Result listByProductId(@PathVariable Integer productId) {
        if (productId == null || productId <= 0) {
            return Result.fail("商品ID不能为空或无效");
        }
        List<ProductImage> imageList = productImageRepository.findByProductId(productId);
        if (imageList.isEmpty()) {
            return Result.suc("该商品暂无图片", imageList); // 空列表但返回成功（避免前端处理错误）
        } else {
            return Result.suc("查询成功", imageList);
        }
    }
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        if (productImageRepository.existsById(id)) {
            productImageRepository.deleteById(id);
            return Result.suc("图片删除成功");
        } else {
            return Result.fail("图片不存在");
        }
    }
}
