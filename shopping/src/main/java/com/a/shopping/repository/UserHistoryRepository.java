package com.a.shopping.repository;

import com.a.shopping.entity.UserHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserHistoryRepository extends JpaRepository<UserHistory, Long> {
    List<UserHistory> findByUserIdOrderByViewTimeDesc(Long userId);
}
