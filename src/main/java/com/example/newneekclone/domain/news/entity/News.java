package com.example.newneekclone.domain.news.entity;

import jakarta.persistence.*;
import lombok.*;
import org.jsoup.nodes.Element;

@Entity
@Table(name = "news")
@Getter
@Setter
@RequiredArgsConstructor
public class News extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category")
    String category;

    @Column(name = "title")
    String title;

    @Column(name = "content")
    String content;

    @Column(name = "likes")
    Integer likes;

    @Column(name = "tags")
    String tags;

    @Column(name = "create_date")
    String date;

    public News(String text, String category, String allContents, String text1, String allTag) {
        this.title = title;
        this.content = allContents;
        this.tags = allTag;
        this.date = date;
    }
}
