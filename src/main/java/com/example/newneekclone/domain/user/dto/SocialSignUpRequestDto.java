package com.example.newneekclone.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SocialSignUpRequestDto {
    @Email(message = "이메일 양식이 아닙니다")
    @NotBlank(message = "Email 공백 불가")
    private String email;

    @NotBlank(message = "닉네임 공백 불가")
    private String nickname;

    @NotBlank(message = "마케팅 동의 미기입")
    private Boolean optionCheck;
}
