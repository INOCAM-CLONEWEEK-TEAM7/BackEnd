package com.example.newneekclone.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private Boolean marketingAgree;

    @Column(nullable = false)
    private Boolean social;

    public void update(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
    }

    @Builder
    private User(String email, String password, String nickname, Boolean marketingAgree, Boolean social) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.marketingAgree = marketingAgree;
        this.social = social;
    }
}