package com.example.newneekclone.domain.news.entity;

import com.example.newneekclone.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "news_like", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "news_id"}))
public class NewsLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "news_id", nullable = false)
    private News news;

    public NewsLike(User user, News news){
        this.user = user;
        this.news = news;
    }
}
