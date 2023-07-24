package com.example.newneekclone.domain.news.dto;

import com.example.newneekclone.domain.news.entity.News;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NewsOneResponsDto {
    private Long newsId;
    private String title;
    private String category;
    private LocalDateTime createDate;
    private String tag;

    private  int likeCount;
    private boolean likeCheck;

    private String imageUrl;
    private String videoUrl;
    public NewsOneResponsDto(News news){
        this.newsId = news.getId();
        this.title = news.getTitle();
        this.createDate = news.getDate();
        this.category = news.getCategory();
        this.tag = news.getTags();
        this.likeCount = news.getLikeCount();
        this.likeCheck = news.getLikeCheck();
        this.imageUrl = news.getImageUrl();
        this.videoUrl = news.getVideoUrl();
    }

}
