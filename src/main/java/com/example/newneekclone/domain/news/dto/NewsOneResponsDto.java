package com.example.newneekclone.domain.news.dto;

import com.example.newneekclone.domain.news.entity.News;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NewsOneResponsDto {
    private Long newsId;
    private String title;
    private LocalDateTime createDate;
    private String category;

    private  int likeCount;
    public NewsOneResponsDto(News news){
        this.newsId = news.getId();
        this.title = news.getTitle();
        this.createDate = news.getCreatedDate();
        this.category = news.getCategory();
        this.likeCount = news.getLikeCount();
    }

}
