package com.a.shopping.controller;

import com.a.shopping.configure.QueryPageParam;
import com.a.shopping.entity.Result;
import com.a.shopping.entity.Shop;
import com.a.shopping.entity.ShopCreateDTO;
import com.a.shopping.entity.ShopUpdateDTO;
import com.a.shopping.repository.ShopRepository;
import com.a.shopping.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/shop")
public class ShopController {
    @Autowired
    ShopRepository shopRepository;
    @Autowired
    ShopService shopService;
    @PostMapping("/add")
    public Result add(@RequestPart("logo") MultipartFile logo,
                      @RequestPart("licenseUrl")MultipartFile licenseUrl,
                      @RequestPart("shopCreateDTO")ShopCreateDTO shopCreateDTO) throws IOException {
        return shopService.createShop(shopCreateDTO,logo,licenseUrl);
    }
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        Shop shop = shopRepository.findById(id).orElse(null);
        if (shop == null) {
            return Result.fail("店铺不存在");
        }
        shopRepository.delete(shop);
        return Result.suc();
    }
    @PostMapping("/update")
    public Result update(@RequestPart(value = "logo", required = false) MultipartFile logo,
                         @RequestPart(value = "licenseUrl" , required = false)MultipartFile licenseUrl,
                         @RequestPart("shopUpdateDTO")ShopUpdateDTO shopUpdateDTO) throws IOException {
        Shop shop = shopRepository.findById(shopUpdateDTO.getShopId()).orElse(null);
        if (shop == null) {
            return Result.fail("店铺不存在");
        }
        if (shopUpdateDTO.getName() != null) {
            shop.setName(shopUpdateDTO.getName());
        }
        if (shopUpdateDTO.getDescription() != null) {
            shop.setDescription(shopUpdateDTO.getDescription());
        }
        if (shopUpdateDTO.getIdcardNo() != null) {
            shop.setIdcardNo(shopUpdateDTO.getIdcardNo());
        }
        if (logo != null) {
            shop.setLogo(logo.getBytes());
        }
        if (licenseUrl != null){
            shop.setLicenseUrl(licenseUrl.getBytes());
        }
        if (shopUpdateDTO.getStatus() != null) {
            shop.setStatus(shopUpdateDTO.getStatus());
        }
        Shop shop1=shopRepository.save(shop);
        return shop1==null?Result.fail("更新失败"):Result.suc();
    }
    @GetMapping("/list/{userId}")
    public Result getShopsByUserId(@PathVariable Long userId) {
        // 根据用户ID查询店铺列表
        List<Shop> shops = shopRepository.findByUserId(userId);
        if (shops == null || shops.isEmpty()) {
            return Result.fail("该用户暂无店铺信息");
        }
        return Result.suc(shops);
    }
    @GetMapping("/listAll")
    public Result listAll(@RequestBody QueryPageParam queryPageParam) {
        // 构建分页参数
        Pageable pageable = PageRequest.of(
                queryPageParam.getPageNum() - 1,
                queryPageParam.getPageSize(),
                Sort.Direction.ASC, "id"  // 按ID升序排序
        );

        // 执行分页查询
        Page<Shop> shopPage = shopRepository.findAll(pageable);

        // 返回分页结果
        return Result.suc(shopPage.getContent(), shopPage.getTotalElements());
    }
    @GetMapping("/listByShopId/{shopId}")
    public Result getShopsByShopId(@PathVariable Long shopId) {
        // 根据用户ID查询店铺列表
        Optional<Shop> shops = shopRepository.findById(shopId);
        if (shops == null || shops.isEmpty()) {
            return Result.fail("招不到该店铺");
        }
        return Result.suc(shops);
    }
}

