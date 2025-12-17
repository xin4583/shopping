package com.a.shopping.controller;

import com.a.shopping.configure.QueryPageParam;
import com.a.shopping.entity.*;
import com.a.shopping.repository.ProductCategoryRepository;
import com.a.shopping.repository.ProductImageRepository;
import com.a.shopping.repository.ProductRepository;
import com.a.shopping.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ShopRepository shopRepository;
    @Autowired
    ProductCategoryRepository productCategoryRepository;
    @Autowired
    ProductImageRepository productImageRepository;
    @PostMapping("/add")
    public Result add(@RequestPart("productDTO") ProductDTO productDTO,@RequestPart("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return Result.fail("请选择图片文件");
        }
        Product product = new Product();
        product.setShop(shopRepository.findById(productDTO.getShopId()).orElse(null));
        product.setCategory(productCategoryRepository.findById(productDTO.getCategoryId()).orElse(null));
        product.setName(productDTO.getName());
        product.setSubtitle(productDTO.getSubtitle());
        product.setPrice(productDTO.getPrice());
        Product product1=productRepository.save(product);
        ProductImage productImage = new ProductImage();
        productImage.setImage(file.getBytes());  // 转换文件为字节数组
        productImage.setProduct(product1);
        productImageRepository.save(productImage);
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
        if (productRepository.findById(id).orElse(null) == null) {
            return Result.fail("商品不存在");
        }
        List<ProductImage>  productImageS = productImageRepository.findByProductId(id);
        productImageRepository.deleteAll(productImageS);
        productRepository.deleteById(id);
        return Result.suc();
    }

    @GetMapping("/list1/{ID}")
    public Result list1(@PathVariable Long ID) {
        Product product = productRepository.findById(ID).orElse(null);
        ProductListDTO productDTO = new ProductListDTO();
        // 商品基础字段
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setSubtitle(product.getSubtitle());
        productDTO.setPrice(product.getPrice());
        productDTO.setStock(product.getStock());
        productDTO.setSales(product.getSales());
        productDTO.setStatus(product.getStatus());
        productDTO.setCreateTime(product.getCreateTime());
        // 店铺相关字段（shopId、shopName、shopLogo 从 product 的 shop 关联中获取）
        if (product.getShop() != null) {
            productDTO.setShopId(product.getShop().getId());       // 店铺ID
            productDTO.setShopName(product.getShop().getName());   // 店铺名称
            productDTO.setShopLogo(product.getShop().getLogo());   // 店铺logo（二进制）
        }
        productDTO.setCategoryId(product.getCategory().getId());
        productDTO.setProductImg(product.getImages().get(0).getImage());
        productDTO.setSkus(product.getSkus());
        return Result.suc(productDTO);
    }
    @PostMapping("/ListPage")
    public Page<ProductListDTO> searchPage(@RequestBody QueryPageParam queryPageParam) {
        // 1. 处理查询参数
        String name = (String) queryPageParam.getParam().get("name");
        String fuzzyName = "%" + (name == null ? "" : name) + "%"; // 兼容关键词为null的情况
        // 2. 构建分页参数
        Pageable pageable = PageRequest.of(
                queryPageParam.getPageNum() - 1,
                queryPageParam.getPageSize(),
                Sort.Direction.ASC, "id"
        );
        Integer status = (Integer) queryPageParam.getParam().get("status");
        // 3. 执行关联分页查询（避免懒加载）
        Page<Product> productPage = productRepository.findByNameLikeAndStatusWithRelations(fuzzyName, status, pageable);
        // 4. 将 Page<Product> 转换为 Page<ProductListDTO>（核心映射逻辑）
        Page<ProductListDTO> productListDTOPage = productPage.map(product -> {
            ProductListDTO dto = new ProductListDTO();

            // 商品基础字段（与list1一致）
            dto.setId(product.getId());
            dto.setName(product.getName());
            dto.setSubtitle(product.getSubtitle());
            dto.setPrice(product.getPrice());
            dto.setStock(product.getStock());
            dto.setSales(product.getSales());
            dto.setStatus(product.getStatus());
            dto.setCreateTime(product.getCreateTime());
            // 店铺相关字段（与list1一致）
            if (product.getShop() != null) {
                dto.setShopId(product.getShop().getId());
                dto.setShopName(product.getShop().getName());
                dto.setShopLogo(product.getShop().getLogo()); // 二进制logo
            }
            // 分类ID（与list1一致，取category的id）
            dto.setCategoryId(product.getCategory() != null ? product.getCategory().getId() : null);

            // 商品主图（取第一张图片的image字段，与list1一致）
            if (product.getImages() != null && !product.getImages().isEmpty()) {
                dto.setProductImg(product.getImages().get(0).getImage()); // 第一张图的二进制数据
            }
            // SKU列表（与list1一致，直接赋值）
            dto.setSkus(product.getSkus() != null ? product.getSkus() : new ArrayList<>());
            return dto;
        });
        // 5. 返回DTO分页结果（格式与list1完全一致）
        return productListDTOPage;
    }
    @PostMapping("/listByShop/{shopId}")
    public Page<ProductListDTO> listByShopId(
            @PathVariable Long shopId,
            @RequestBody QueryPageParam queryPageParam) {

        // 1. 验证店铺是否存在
        if (!shopRepository.existsById(shopId)) {
            throw new RuntimeException("店铺不存在"); // 实际项目中建议使用自定义异常并全局处理
        }

        // 2. 构建分页参数（排序方式保持与ListPage接口一致）
        Pageable pageable = PageRequest.of(
                queryPageParam.getPageNum() - 1,
                queryPageParam.getPageSize(),
                Sort.Direction.ASC, "id"
        );

        // 3. 执行分页查询（关联查询避免懒加载问题）
        Page<Product> productPage = productRepository.findByShopIdWithRelations(shopId, pageable);

        // 4. 转换为ProductListDTO（复用已有的映射逻辑）
        return productPage.map(product -> {
            ProductListDTO dto = new ProductListDTO();
            // 商品基础字段
            dto.setId(product.getId());
            dto.setName(product.getName());
            dto.setSubtitle(product.getSubtitle());
            dto.setPrice(product.getPrice());
            dto.setStock(product.getStock());
            dto.setSales(product.getSales());
            dto.setStatus(product.getStatus());
            dto.setCreateTime(product.getCreateTime());
            // 店铺相关字段
            if (product.getShop() != null) {
                dto.setShopId(product.getShop().getId());
                dto.setShopName(product.getShop().getName());
                dto.setShopLogo(product.getShop().getLogo());
            }
            // 分类ID
            dto.setCategoryId(product.getCategory() != null ? product.getCategory().getId() : null);
            // 商品主图
            if (product.getImages() != null && !product.getImages().isEmpty()) {
                dto.setProductImg(product.getImages().get(0).getImage());
            }
            // SKU列表
            dto.setSkus(product.getSkus() != null ? product.getSkus() : new ArrayList<>());
            return dto;
        });
    }
}
