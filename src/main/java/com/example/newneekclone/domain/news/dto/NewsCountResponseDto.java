package com.example.newneekclone.domain.news.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NewsCountResponseDto {
    private int newsCount;
    private List<NewsResponseDto> newsList;

    public NewsCountResponseDto(int newsCount, List<NewsResponseDto> newsList) {
        this.newsCount = newsCount;
        this.newsList = newsList;
    }
}
