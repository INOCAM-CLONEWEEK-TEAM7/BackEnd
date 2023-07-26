package com.example.newneekclone.domain.sociallogin.controller;

import com.example.newneekclone.domain.sociallogin.dto.SocialResponseDto;
import com.example.newneekclone.domain.sociallogin.service.SocialService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/login/oauth2", produces = "application/json")
public class SocialController {

    private final SocialService socialService;


    @GetMapping("/code/{registrationId}")
    public SocialResponseDto googleLogin(@RequestParam String code, @PathVariable String registrationId,HttpServletResponse response)throws IOException, ServletException {
        return socialService.socialLogin(code, registrationId,response);
    }
}