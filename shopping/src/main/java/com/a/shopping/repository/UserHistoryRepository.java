package com.a.shopping.repository;

import com.a.shopping.entity.UserHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserHistoryRepository extends JpaRepository<UserHistory, Long> {
    List<UserHistory> findByUserIdOrderByViewTimeDesc(Long userId);
    Optional<UserHistory> findByUserIdAndProductId(Long userId, Long productId);
}
