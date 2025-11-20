package com.a.shopping.controller;

import com.a.shopping.entity.Product;
import com.a.shopping.entity.ProductDTO;
import com.a.shopping.entity.Result;
import com.a.shopping.repository.ProductCategoryRepository;
import com.a.shopping.repository.ProductRepository;
import com.a.shopping.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ShopRepository shopRepository;
    @Autowired
    ProductCategoryRepository productCategoryRepository;
    @PostMapping("/add")
    public Result add(@RequestBody ProductDTO productDTO) {
        Product product = new Product();
        product.setShop(shopRepository.findById(productDTO.getShopId()).orElse(null));
        product.setCategory(productCategoryRepository.findById(productDTO.getCategoryId()).orElse(null));
        product.setName(productDTO.getName());
        product.setSubtitle(productDTO.getSubtitle());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());
        Product product1=productRepository.save(product);
        return product1==null?Result.fail("添加失败"):Result.suc();
    }
    @PostMapping("/update")
    public Result update(@RequestBody ProductDTO productDTO) {
        Product product = productRepository.findById(productDTO.getId()).orElse(null);
        if (product == null) {
            return Result.fail("商品不存在");
        }
        if (productDTO.getCategoryId() != null) {
            product.setCategory(productCategoryRepository.findById(productDTO.getCategoryId()).orElse(null));
        }
        if (productDTO.getPrice() != null) {
            product.setPrice(productDTO.getPrice());
        }
        if (productDTO.getStock() != null) {
            product.setStock(productDTO.getStock());
        }
        if (productDTO.getName() != null) {
            product.setName(productDTO.getName());
        }
        if (productDTO.getSubtitle() != null) {
            product.setSubtitle(productDTO.getSubtitle());
        }
        if (productDTO.getStatus() != null) {
            product.setStatus(productDTO.getStatus());
        }
        if (productDTO.getSales() != null) {
            product.setSales(productDTO.getSales());
        }
        productRepository.save(product);
        return Result.suc();
    }
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        productRepository.deleteById(id);
        return Result.suc();
    }
    @GetMapping("/list")
    public Result list() {
        return Result.suc(productRepository.findAll());
    }
}
