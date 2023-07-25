package com.example.newneekclone.domain.news.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import java.time.LocalDateTime;

@Entity
@Table(name = "news",
        indexes = {
        @Index(name = "news_category_index", columnList = "category")
})
@Getter
@Setter
@RequiredArgsConstructor
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category")
    private String category;

    @Column(name = "title")
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    String content;

    @Column(name = "tags", columnDefinition = "TEXT")
    String tags;

    @Column(name = "date")
    LocalDateTime date;

    @Column(name = "url")
    String url;

    @Column(name = "imageURL")
    String imageUrl;

    @Column(name = "videoURL")
    String videoUrl;

    @Transient
    private int likeCount;

    public News(String allTag, String title, String category, String link, String content, LocalDateTime date, String imageUrl, String videoUrl) {
        this.tags = allTag;
        this.title = title;
        this.category = category;
        this.url = link;
        this.content = content;
        this.date = date;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
    }

    public int getLikeCount(){
        return this.likeCount;
    }

    public void setLikeCount(int likeCount){
        this.likeCount = likeCount;
    }

    @Transient
    private boolean likeCheck;
    public boolean getLikeCheck(){ return this.likeCheck; }
}
