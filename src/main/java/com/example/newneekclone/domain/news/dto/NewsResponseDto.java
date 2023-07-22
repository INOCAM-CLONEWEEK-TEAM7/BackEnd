package com.example.newneekclone.domain.news.dto;

import com.example.newneekclone.domain.news.entity.News;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NewsResponseDto {

    private String title;
    private LocalDateTime createDate;
    private String category;

    public NewsResponseDto(News news){
        this.title = news.getTitle();
        this.createDate = news.getCreatedDate();
        this.category = news.getCategory();
    }
}
