package com.a.shopping.repository;

import com.a.shopping.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Map;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByPhone(String phone);
    @Query("SELECT new map(u.id as id, u.username as username, u.phone as phone, " +
            "u.email as email, u.role as role, u.createTime as createTime) " +
            "FROM user u WHERE u.id = :id")
    Map<String, Object> findBaseInfoById(@Param("id") Long id);

}
