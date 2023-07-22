package com.example.newneekclone.domain.news.service;

import com.example.newneekclone.domain.news.dto.NewsOneResponsDto;
import com.example.newneekclone.domain.news.dto.NewsResponseDto;
import com.example.newneekclone.domain.news.entity.News;
import com.example.newneekclone.domain.news.entity.NewsLike;
import com.example.newneekclone.domain.news.exception.NewsNotFoundException;
import com.example.newneekclone.domain.news.repository.NewsLikeRepository;
import com.example.newneekclone.domain.news.repository.NewsRepository;
import com.example.newneekclone.domain.user.entity.User;
import com.example.newneekclone.domain.user.exception.UserNotFoundException;
import com.example.newneekclone.domain.user.repository.UserRepository;
import com.example.newneekclone.global.enums.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.newneekclone.global.enums.ErrorCode.*;
import static com.example.newneekclone.global.enums.SuccessCode.*;

@RequiredArgsConstructor
@Service
public class NewsService {

    private final NewsRepository newsRepository;
    private final NewsLikeRepository newsLikeRepository;
    private final UserRepository userRepository;

    // 전체 뉴스 조회
    @Transactional(readOnly = true)
    public List<NewsResponseDto> getNews() {
        List<News> news = newsRepository.findAllByOrderByCreatedDateDesc();

        List<NewsResponseDto> response = news.stream()
                .map(NewsResponseDto::new)
                .collect(Collectors.toList());
        return response;
    }

    // 상세 뉴스 조회
    @Transactional(readOnly = true)
    public NewsOneResponsDto getNewsOne(Long newsId){
        News news = newsRepository.findById(newsId).orElseThrow(
                () -> new NewsNotFoundException(NOT_FOUND_DATA)
        );

        // 뉴스 좋아요 수
        news.setLikeCount(newsLikeRepository.countByNewsId(newsId));

        NewsOneResponsDto responseDto = new NewsOneResponsDto(news);
        return responseDto;
    }

    // 좋아요 누르기
    public SuccessCode likeNews(Long newsId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(NOT_FOUND_CLIENT)
        );
        News news = newsRepository.findById(newsId).orElseThrow(
                () -> new NewsNotFoundException(NOT_FOUND_DATA)
        );

        Optional<NewsLike> newsLike = newsLikeRepository.findByNewsIdAndUserId(newsId, userId);
        if(newsLike.isPresent()){
            newsLikeRepository.delete(newsLike.get());
            return LIKE_CANCEL;
        }else{
            newsLikeRepository.save(new NewsLike(user, news));
            return LIKE_SUCCESS;
        }
    }

    // 카테고리별 조회
    @Transactional(readOnly = true)
    public List<NewsResponseDto> getCategory(String category) {
        List<News> news = newsRepository.findByCategory(category);

        List<NewsResponseDto> response = news.stream()
                .map(NewsResponseDto::new)
                .collect(Collectors.toList());
        return response;
    }

    // 검색
}
