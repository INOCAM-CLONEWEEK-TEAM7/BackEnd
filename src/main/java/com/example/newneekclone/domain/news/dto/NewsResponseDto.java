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
    private String content;
    private Integer likes;
    private String tags;
    private LocalDateTime date;
    private String imageUrl;
    private String videoUrl;

    public NewsResponseDto(News news){
        this.newsId = news.getId();
        this.title = news.getTitle();
        this.category = news.getCategory();
        this.content = news.getContent();
        this.likes = news.getLikes();
        this.tags = news.getTags();
        this.date = news.getDate();
    }

    public NewsResponseDto(String title, String tags, LocalDateTime localDateTime, String content, String imageUrl, String videoUrl) {
        this.title = title;
        this.content = content;
        this.tags = tags;
        this.date = localDateTime;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
    }
}
