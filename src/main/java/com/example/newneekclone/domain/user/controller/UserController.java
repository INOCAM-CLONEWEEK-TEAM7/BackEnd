package com.example.newneekclone.domain.user.controller;

import com.example.newneekclone.domain.mail.dto.ChangePasswordRequestDto;
import com.example.newneekclone.domain.mail.dto.EmailRequestDto;
import com.example.newneekclone.domain.mail.entity.EmailMessage;
import com.example.newneekclone.domain.mail.service.MailService;
import com.example.newneekclone.domain.user.dto.UserRequestDto;
import com.example.newneekclone.domain.user.service.UserService;
import com.example.newneekclone.global.responsedto.ApiResponse;
import com.example.newneekclone.global.utils.ResponseUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

import static com.example.newneekclone.global.enums.ErrorCode.USER_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j(topic = "user controller")
public class UserController {
    private final UserService userService;
    private final MailService mailService;

    @PostMapping("/auth/signup")
    public ApiResponse<?> signUp(@Valid @RequestBody UserRequestDto userRequestDto) {
        return ResponseUtils.ok(userService.signUp(userRequestDto));

    }

    @PostMapping("/auth/forgot")
    @ResponseBody
    public ApiResponse<?> sendPasswordMail(@RequestBody EmailRequestDto email) {
        if (!userService.checkEmail(email.getEmail())) {
            return ResponseUtils.error(USER_NOT_FOUND);
        }

        EmailMessage emailMessage = EmailMessage.builder()
                .to(email.getEmail())
                .subject("비밀번호 변경 안내")
                .build();
        
        Map<String, String> map = Collections.singletonMap("email", email.getEmail());

        return ResponseUtils.ok(mailService.sendMail(emailMessage, "forgot_password"), map);
    }

    @GetMapping("/auth/newPwChange/{code}")
    public ModelAndView sendPasswordChangePage(@PathVariable("code") String code) {
        ModelAndView mav = new ModelAndView();
        if (!code.contains("@newneekclone@")) {
            mav.setViewName("bad_request");
            return mav;
        }

        Date date = new Date();
        String deadline = code.split("@newneekclone@")[1];
        log.info("currentDate={} deadline={} diff={}", date.getTime(), Long.parseLong(deadline), (date.getTime() - Long.parseLong(deadline)));
        if ((date.getTime() - Long.parseLong(deadline)) > 0) {
            mav.setViewName("overtime");
        } else {
            mav.setViewName("password");
        }
        return mav;
    }

    @PostMapping("/auth/newPwChange")
    public ApiResponse<?> changeNewPassword(@RequestBody ChangePasswordRequestDto requestDto) {
        String email = requestDto.getCode().split("@newneekclone@")[0];
        String newPassword = requestDto.getNewPassword();
        return ResponseUtils.ok(userService.changePassword(email, newPassword));
    }
}
