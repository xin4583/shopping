package com.a.shopping.service;

import com.a.shopping.entity.Result;
import com.a.shopping.entity.Shop;
import com.a.shopping.entity.ShopCreateDTO;
import com.a.shopping.entity.User;
import com.a.shopping.repository.ShopRepository;
import com.a.shopping.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ShopService {
    @Autowired
    private  ShopRepository shopRepository;
    @Autowired
    private  UserRepository userRepository;   // 需自行创建

    @Transactional
    public Result createShop(ShopCreateDTO dto, MultipartFile logo,MultipartFile licenseUrl) throws IOException {
        if (shopRepository.findByUser_Id(dto.getUserId()).orElse(null)!=null) {
            return Result.fail("用户已存在店铺");
        }
        Shop shop = new Shop();
        shop.setUser(userRepository.findById(dto.getUserId()).orElse(null));
        shop.setName(dto.getName());
        shop.setDescription(dto.getDescription());
        shop.setIdcardNo(dto.getIdcardNo());
        shop.setStatus(0);
        shop.setLogo(logo.getBytes());
        shop.setLicenseUrl(licenseUrl.getBytes());
        shopRepository.save(shop);
        return Result.suc();
    }

}
