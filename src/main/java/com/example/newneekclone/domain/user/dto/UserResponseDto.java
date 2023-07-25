package com.example.newneekclone.domain.user.dto;

import com.example.newneekclone.domain.user.entity.User;
import lombok.Getter;

@Getter
public class UserResponseDto {
    private final String email;
    private final String password;
    private final String nickname;

    public UserResponseDto(User user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.nickname = user.getNickname();
    }
}
