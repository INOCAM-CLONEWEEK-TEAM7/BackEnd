package com.example.newneekclone.domain.news.service;

import com.example.newneekclone.domain.news.dto.NewsResponseDto;
import com.example.newneekclone.domain.news.entity.News;
import com.example.newneekclone.domain.news.repository.NewsRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class NewsServiceImpl implements NewsService{
    private final NewsRepository newsRepository;

    // 전체 뉴스 조회
    @Transactional(readOnly = true)
    @Override
    public List<NewsResponseDto> getNews() {
        List<News> news = newsRepository.findAllByOrderByCreatedDateDesc();

        List<NewsResponseDto> response = news.stream()
                .map(NewsResponseDto::new)
                .collect(Collectors.toList());
        return response;
    }

    // 상세 뉴스 조회
    // 좋아요 누르기
    // 카테고리별 조회
    // 검색
}
