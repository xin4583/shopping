package com.a.shopping.controller;

import com.a.shopping.entity.Result;
import com.a.shopping.entity.Shop;
import com.a.shopping.entity.ShopCreateDTO;
import com.a.shopping.entity.ShopUpdateDTO;
import com.a.shopping.repository.ShopRepository;
import com.a.shopping.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    @GetMapping("/list/{id}")
    public Result get(@PathVariable Long id) {
        Shop shop = shopRepository.findById(id).orElse(null);
        if (shop == null) {
            return Result.fail("店铺不存在");
        }
        return Result.suc(shop);
    }
}

