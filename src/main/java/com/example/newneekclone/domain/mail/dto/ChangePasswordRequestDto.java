package com.example.newneekclone.domain.mail.dto;

import lombok.Getter;

@Getter
public class ChangePasswordRequestDto {
    private String code;
    private String newPassword;
}
