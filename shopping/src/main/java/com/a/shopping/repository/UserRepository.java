package com.a.shopping.repository;

import com.a.shopping.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Map;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByPhone(String phone);
    @Query("SELECT new map(u.id as id, u.username as username, u.phone as phone, " +
            "u.email as email, u.role as role, u.createTime as createTime) " +
            "FROM user u WHERE u.id = :id")
    Map<String, Object> findBaseInfoById(@Param("id") Long id);

    @Query("SELECT u FROM user u WHERE " +
            "(u.username LIKE CONCAT('%', :username, '%') OR :username IS NULL) AND " +
            "(u.phone = :phone OR :phone IS NULL) AND " +
            "(u.status = :status OR :status IS NULL) AND " +
            "(u.createTime >= :startTime OR :startTime IS NULL) AND " +
            "(u.createTime <= :endTime OR :endTime IS NULL)")
    Page<User> findByCondition(
            @Param("username") String username,
            @Param("phone") String phone,
            @Param("status") Integer status,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            Pageable pageable);
}
