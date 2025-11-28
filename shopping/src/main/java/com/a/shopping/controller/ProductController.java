package com.a.shopping.controller;

import com.a.shopping.configure.QueryPageParam;
import com.a.shopping.entity.Product;
import com.a.shopping.entity.ProductDTO;
import com.a.shopping.entity.Result;
import com.a.shopping.repository.ProductCategoryRepository;
import com.a.shopping.repository.ProductRepository;
import com.a.shopping.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public Page<Product> listPage(@RequestBody QueryPageParam queryPageParam) {
        Pageable pageable = PageRequest.of(
                queryPageParam.getPageNum() - 1,
                queryPageParam.getPageSize(),
                Sort.Direction.ASC, "id"
        ); 
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage;
    }
    @GetMapping("/ListPage")
    public Page<Product> searchPage(@RequestBody QueryPageParam queryPageParam) {
        // 从param中获取搜索关键词，拼接模糊查询符 %
        String fuzzyName = "%" + queryPageParam.getParam().get("name") + "%";

        // 构建分页参数（与上面一致：页码转换、每页大小、按id升序）
        Pageable pageable = PageRequest.of(
                queryPageParam.getPageNum() - 1,
                queryPageParam.getPageSize(),
                Sort.Direction.ASC, "id"
        );

        // 执行模糊搜索分页查询（注意：JPA方法参数顺序要与Repository定义一致）
        Page<Product> productPage = productRepository.findByNameLike(pageable, fuzzyName);
        return productPage;
    }
}
