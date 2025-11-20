package com.a.shopping.controller;

import com.a.shopping.entity.ProductSku;
import com.a.shopping.entity.Result;
import com.a.shopping.repository.ProductSkuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/productSku")
public class ProductSkuController {
    @Autowired
    ProductSkuRepository productSkuRepository;
    @PostMapping("/add")
    public Result addProductSku(@RequestBody ProductSku productSku) {
       productSkuRepository.save(productSku);
       return Result.suc();
    }
    @GetMapping("/list/{productId}")
    public Result list(@PathVariable Long productId) {
        return Result.suc(productSkuRepository.findAllByProductId(productId));
    }
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        productSkuRepository.deleteById(id);
        return Result.suc();
    }
    @PutMapping("/update")
    public Result update(@RequestBody ProductSku productSku) {
        if (productSkuRepository.findById(productSku.getId()).isEmpty()) {
            return Result.fail("规格不存在");
        }
        ProductSku productSku1 = productSkuRepository.findById(productSku.getId()).get();
        if (productSku.getProduct() != null){
        productSku1.setProduct(productSku.getProduct());}
        if (productSku.getPrice() != null){
        productSku1.setPrice(productSku.getPrice());}
        if (productSku.getStock() != null){
        productSku1.setStock(productSku.getStock());}
        if (productSku.getSpecs() != null){
        productSku1.setSpecs(productSku.getSpecs());}
        if (productSku.getSkuCode() != null){
        productSku1.setSkuCode(productSku.getSkuCode());}
        productSkuRepository.save(productSku);
        return Result.suc();
    }

}
