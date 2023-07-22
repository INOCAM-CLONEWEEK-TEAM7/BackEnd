package com.example.newneekclone.domain.news.service;

import com.example.newneekclone.domain.news.dto.NewsResponseDto;

import java.util.List;


public interface NewsService {

    // 전체 뉴스 조회
    List<NewsResponseDto> getNews();

    // 상세 뉴스 조회
    // 좋아요 누르기
    // 카테고리별 조회
    // 검색
}
