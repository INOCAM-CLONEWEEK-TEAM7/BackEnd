package com.example.newneekclone.domain.user.dto;

import com.example.newneekclone.domain.user.entity.User;
import lombok.Getter;

@Getter
public class UserResponseDto {
    private final String email;
    private final String password;
    private final String nickname;
    private final Boolean marketingAgree;
    private final Boolean social;

    public UserResponseDto(User user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.nickname = user.getNickname();
        this.marketingAgree = user.getMarketingAgree();
        this.social = user.getSocial();
    }
}
