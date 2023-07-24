package com.example.newneekclone.domain.news.controller;

//import com.example.newneekclone.domain.news.daumCrawlingService;
import com.example.newneekclone.domain.news.daumCrawlingService;
import com.example.newneekclone.domain.news.dto.NewsResponseDto;
import com.example.newneekclone.domain.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class NewsController {
    private final NewsService newsService;
//    private final daumCrawlingService daumCrawlingService;
    private final daumCrawlingService daumCrawlingServicetow;
    // 전체 뉴스 조회
    @GetMapping("/news")
    public ResponseEntity<List<NewsResponseDto>> getNews(){
        List<NewsResponseDto> response = newsService.getNews();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    // 상세 뉴스 조회
    // 좋아요 누르기
    // 카테고리별 조회
    // 검색

    @PostMapping("/news")
    public NewsResponseDto crawling(){
        daumCrawlingServicetow.allCrwaling();
        return null;
    }
}
