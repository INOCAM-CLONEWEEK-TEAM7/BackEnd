package com.example.newneekclone.domain.news.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "news")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
}
