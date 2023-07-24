package com.example.newneekclone.domain.news.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class NewsSearchResponseDto {
    private int newsCount;
    private List<NewsResponseDto> newsList;

    public NewsSearchResponseDto(int newsCount, List<NewsResponseDto> newsList) {
        this.newsCount = newsCount;
        this.newsList = newsList;
    }
}
