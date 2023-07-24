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
    private String content;

    private  int likeCount;
    private boolean likeCheck;

    private String imageUrl;
    private String videoUrl;
    public NewsOneResponsDto(News news){
        this.newsId = news.getId();
        this.title = news.getTitle();
        this.createDate = news.getDate();
        this.content = news.getContent();
        this.category = news.getCategory();
        this.tag = news.getTags();
        this.likeCount = news.getLikeCount();
        this.likeCheck = news.getLikeCheck();
        this.imageUrl = news.getImageUrl();
        this.videoUrl = news.getVideoUrl();
    }

    public NewsOneResponsDto(String title, String allTag, LocalDateTime localDateTime, String content, String imageUrl, String videoUrl) {
        this.title = title;
        this.tag = allTag;
        this.createDate = localDateTime;
        this.content = content;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
    }
}
