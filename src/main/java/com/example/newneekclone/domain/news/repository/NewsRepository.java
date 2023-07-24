package com.example.newneekclone.domain.news.repository;


import com.example.newneekclone.domain.news.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    boolean existsByTitleOrUrl(String title, String link);
    boolean existsByContent(String content);

    List<News> findAllByOrderByDateDesc();

    List<News> findByCategory(String category);

}
