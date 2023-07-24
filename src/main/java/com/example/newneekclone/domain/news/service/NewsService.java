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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.newneekclone.global.enums.ErrorCode.NOT_FOUND_CLIENT;
import static com.example.newneekclone.global.enums.ErrorCode.NOT_FOUND_DATA;
import static com.example.newneekclone.global.enums.SuccessCode.LIKE_CANCEL;
import static com.example.newneekclone.global.enums.SuccessCode.LIKE_SUCCESS;

@RequiredArgsConstructor
@Service
public class NewsService {

    private final NewsRepository newsRepository;
    private final NewsLikeRepository newsLikeRepository;
    private final UserRepository userRepository;

    // 전체 뉴스 조회
    @Transactional(readOnly = true)
    public List<NewsResponseDto> getNews() {
        List<News> news = newsRepository.findAllByOrderByDateDesc();

        List<NewsResponseDto> response = news.stream()
                .map(NewsResponseDto::new)
                .collect(Collectors.toList());
        return response;
    }

    // 상세 뉴스 조회
    @Transactional(readOnly = true)
    public NewsOneResponsDto getNewsOne(Long newsId, Long userId){
        News news = newsRepository.findById(newsId).orElseThrow(
                () -> new NewsNotFoundException(NOT_FOUND_DATA)
        );

        // 뉴스 좋아요 수
        news.setLikeCount(newsLikeRepository.countByNewsId(newsId));

        // 사용자가 해당 기사 좋아요했는지 여부
        Optional<NewsLike> newsLike = newsLikeRepository.findByNewsIdAndUserId(newsId, userId);
        if(newsLike.isPresent()){
            news.setLikeCheck(true);
        }else news.setLikeCheck(false);

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
    @Transactional(readOnly = true)
    public List<NewsResponseDto> getSearch(String q) {
        List<News> news = newsRepository.findAllByOrderByDateDesc();
        List<News> searchNews = new ArrayList<News>();
        for(int i = 0; i < news.size(); i++){
            if(news.get(i).getContent().contains(q)){
                searchNews.add(news.get(i));
            }
        }
        if(searchNews.size()==0)
            throw new NewsNotFoundException(NOT_FOUND_DATA);
        List<NewsResponseDto> response = searchNews.stream()
                .map(NewsResponseDto::new)
                .collect(Collectors.toList());
        return response;
    }
}
