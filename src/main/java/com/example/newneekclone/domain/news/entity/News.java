package com.example.newneekclone.domain.news.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "news")
@Getter
@Setter
@RequiredArgsConstructor
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category")
    String category;

    @Column(name = "title")
    String title;

    @Column(name = "content", columnDefinition = "TEXT")
    String content;

    @Column(name = "likes")
    Integer likes;

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
}
