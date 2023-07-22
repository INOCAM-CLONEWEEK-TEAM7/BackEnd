package com.example.newneekclone.domain.news.controller;

import com.example.newneekclone.domain.news.dto.NewsOneResponsDto;
import com.example.newneekclone.domain.news.dto.NewsResponseDto;
import com.example.newneekclone.domain.news.service.NewsService;
import com.example.newneekclone.global.enums.SuccessCode;
import com.example.newneekclone.global.responsedto.ApiResponse;
import com.example.newneekclone.global.security.UserDetailsImpl;
import com.example.newneekclone.global.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class NewsController {
    private final NewsService newsService;
    // 전체 뉴스 조회
    @GetMapping("/news")
    public ApiResponse<?> getNews(){
        List<NewsResponseDto> data = newsService.getNews();
        return ResponseUtils.ok(data);
    }

    // 상세 뉴스 조회
    @GetMapping("/news/{newsId}")
    public ApiResponse<?> getNewsOne(@PathVariable("newsId") Long newsId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        Long userId = userDetails.getUser().getId();
        log.info("userId={}", userId);

        NewsOneResponsDto data = newsService.getNewsOne(newsId, userId);
        return ResponseUtils.ok(data);
    }

    // 좋아요 누르기
    @PostMapping("/news/{newsId}/like")
    public ApiResponse<?> likeNews(@PathVariable("newsId") Long newsId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        SuccessCode successCode = newsService.likeNews(newsId,userDetails.getUser().getId());
//        SuccessCode successCode = newsService.likeNews(newsId,1L);
        return ResponseUtils.ok(successCode);
    }

    // 카테고리별 조회
    @GetMapping("/news/category")
    public ApiResponse<?> getCategory(@RequestParam String tag){
        log.info("category={}", tag);
        List<NewsResponseDto> data = newsService.getCategory(tag);
        return ResponseUtils.ok(data);
    }

    // 검색
    @GetMapping("/news/search")
    public ApiResponse<?> getSearch(@RequestParam String q){
        log.info("q={}", q);
        List<NewsResponseDto> data = newsService.getSearch(q);
        return ResponseUtils.ok(data);
    }
}
