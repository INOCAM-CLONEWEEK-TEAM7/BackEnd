package com.example.newneekclone.domain.news.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "news")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class News{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category")
    private String category;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "tags")
    private String tags;

    @Column(name = "createdDate")
    private LocalDateTime createdDate;

    @Transient
    private int likeCount;

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
