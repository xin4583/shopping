package com.a.shopping.controller;

import com.a.shopping.entity.Result;
import com.a.shopping.entity.UserAddress;
import com.a.shopping.repository.UserAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/useraddress")
public class UserAddressController {
    @Autowired
    UserAddressRepository userAddressRepository;
    @PostMapping("/add")
    public Result add(@RequestBody UserAddress userAddress) {
        UserAddress userAddress1=userAddressRepository.findByUserIdAndIsDefault(userAddress.getUser().getId(),true);
        if(userAddress.getIsDefault()==true&&userAddress1!=null){
            userAddress1.setIsDefault(false);
        }
        UserAddress address= userAddressRepository.save(userAddress);
        return address==null?Result.fail("添加失败"):Result.suc();
    }
    @PostMapping("/update")
    public Result update(@RequestBody UserAddress userAddress) {
        UserAddress userAddress1=userAddressRepository.findByUserIdAndIsDefault(userAddress.getUser().getId(),true);
        if(userAddress.getIsDefault()==true&&userAddress1!=null){
            userAddress1.setIsDefault(false);
        }
        if (userAddressRepository.findById(userAddress.getId())==null) return Result.fail("更新失败");
        UserAddress addr=userAddressRepository.findById(userAddress.getId()).get();
        addr.setReceiver(userAddress.getReceiver());
        addr.setPhone(userAddress.getPhone());
        addr.setProvince(userAddress.getProvince());
        addr.setCity(userAddress.getCity());
        addr.setDetailAddress(userAddress.getDetailAddress());
        addr.setIsDefault(userAddress.getIsDefault());
        // UserAddress address=userAddressRepository.save(userAddress);
        return Result.suc();
    }
    @DeleteMapping ("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        userAddressRepository.deleteById(id);
        return userAddressRepository.existsById(id)?Result.fail("删除失败"):Result.suc();
    }
    @GetMapping("/list/{userid}")
    public Result list(@PathVariable Long userid) {
        return Result.suc(userAddressRepository.findAddressByUserId(userid));
    }
}
