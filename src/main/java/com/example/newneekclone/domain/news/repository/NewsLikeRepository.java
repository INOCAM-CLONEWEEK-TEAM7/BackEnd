package com.example.newneekclone.domain.news.repository;

import com.example.newneekclone.domain.news.entity.NewsLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NewsLikeRepository extends JpaRepository<NewsLike, Long> {
    Optional<NewsLike> findByNewsIdAndUserId(Long newsId, Long userId);
    int countByNewsId(Long newsId);
}
