package com.example.newneekclone.domain.user.controller;

import com.example.newneekclone.domain.user.dto.UserRequestDto;
import com.example.newneekclone.domain.user.service.UserService;
import com.example.newneekclone.global.responsedto.ApiResponse;
import com.example.newneekclone.global.utils.ResponseUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.newneekclone.global.enums.SuccessCode.USER_SIGNUP_SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    @PostMapping("/auth/signin")
    public ApiResponse<?> signIn(@Valid @RequestBody UserRequestDto userRequestDto) {
        userService.signIn(userRequestDto);
        return ResponseUtils.ok(USER_SIGNUP_SUCCESS);

    }
}
