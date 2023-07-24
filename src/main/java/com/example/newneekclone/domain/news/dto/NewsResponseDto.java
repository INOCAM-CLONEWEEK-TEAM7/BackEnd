package com.example.newneekclone.domain.news.dto;

import com.example.newneekclone.domain.news.entity.News;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NewsResponseDto {
    private Long newsId;
    private String title;
    private String category;
    private LocalDateTime date;
    private String imageUrl;
    private String videoUrl;

    public NewsResponseDto(News news){
        this.newsId = news.getId();
        this.title = news.getTitle();
        this.category = news.getCategory();
        this.date = news.getDate();
        this.imageUrl = news.getImageUrl();
        this.videoUrl = news.getVideoUrl();
    }

    public NewsResponseDto(String title, String category, LocalDateTime localDateTime,  String imageUrl, String videoUrl) {
        this.title = title;
        this.category = category;
        this.date = localDateTime;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
    }
}
