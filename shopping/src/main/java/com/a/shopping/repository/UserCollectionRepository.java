package com.a.shopping.repository;

import com.a.shopping.entity.UserCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserCollectionRepository extends JpaRepository<UserCollection, Long> {
    @Query("SELECT uc FROM user_collection uc " +
            "JOIN FETCH uc.product p " +  // 加载商品
            "JOIN FETCH p.shop s "        +  // 加载商品所属店铺
            "WHERE uc.user.id = :userId")
    List<UserCollection>findByUserId(@Param("userId") Long userId);
}
