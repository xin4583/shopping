package com.a.shopping.repository;

import com.a.shopping.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {
    @Query("SELECT ua FROM user_address ua WHERE ua.user.id = :userId AND ua.isDefault = :isDefault")
    UserAddress findByUserIdAndIsDefault(Long userId, Boolean isDefault);

    List<UserAddress> findAddressByUserId(@Param("userId") Long userId);
}
