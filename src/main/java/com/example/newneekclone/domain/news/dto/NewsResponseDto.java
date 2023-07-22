package com.example.newneekclone.domain.news.dto;

import com.example.newneekclone.domain.news.entity.News;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NewsResponseDto {
    private Long id;
    private String title;
    private String category;
    private String content;
    private Integer likes;
    private String tags;
    private LocalDateTime date;
    private String imageUrl;
    private String videoUrl;

    public NewsResponseDto(News news) {
        this.id = news.getId();
        this.title = news.getTitle();
        this.category = news.getCategory();
        this.content = news.getContent();
        this.likes = news.getLikes();
        this.tags = news.getTags();
        this.date = news.getCreate_date();
    }
}
