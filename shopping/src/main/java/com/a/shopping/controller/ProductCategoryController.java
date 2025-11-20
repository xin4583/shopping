package com.a.shopping.controller;

import com.a.shopping.entity.ProductCategory;
import com.a.shopping.entity.Result;
import com.a.shopping.entity.Shop;
import com.a.shopping.repository.ProductCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/productCategory")
public class ProductCategoryController {
    @Autowired
    ProductCategoryRepository productCategoryRepository;
    @PostMapping("/add")
    public Result add(@RequestBody ProductCategory productCategory) {
        ProductCategory productCategory1=productCategoryRepository.save(productCategory);
        return productCategory1==null?Result.fail("添加失败"):Result.suc();
    }
    @PostMapping("/update")
    public Result update(@RequestBody ProductCategory productCategory) {
        ProductCategory productCategory1=productCategoryRepository.save(productCategory);
        return productCategory1==null?Result.fail("更新失败"):Result.suc();
    }
    @DeleteMapping ("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        if (productCategoryRepository.findById(id).isEmpty()){
            return Result.fail("不存在该分类");
        }
        productCategoryRepository.deleteById(id);
        return Result.suc();
    }
    @GetMapping("/list/{parentId}")
    public Result list(@PathVariable Long parentId) {
        return Result.suc(productCategoryRepository.findByParentId(parentId));
    }

}
