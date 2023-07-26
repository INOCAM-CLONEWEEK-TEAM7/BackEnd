package com.example.newneekclone.domain.sociallogin.service;

import com.example.newneekclone.domain.sociallogin.dto.SocialResponseDto;
import com.example.newneekclone.domain.user.repository.UserRepository;
import com.example.newneekclone.global.security.jwt.JwtUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SocialService {

    private final Environment env;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RestTemplate restTemplate = new RestTemplate();
    public SocialResponseDto socialLogin(String code, String registrationId, HttpServletResponse response) throws IOException, ServletException {
        String accessToken = getAccessToken(code, registrationId);
        JsonNode userResourceNode = getUserResource(accessToken, registrationId);
        String email = userResourceNode.get("email").asText();
        String nickname = userResourceNode.get("name").asText();
        if(userRepository.existsByEmail(email)){// 이미 한번 구글 로그인을 했을때
            String token = jwtUtil.createToken(email);
            jwtUtil.addTokenToHeader(token, response);
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("success", true);
            data.put("statusCode", HttpServletResponse.SC_OK);
            data.put("msg", "로그인 성공");
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(data);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonString);
            return null;
        }

        return new SocialResponseDto(email,nickname);// 뉴닉 자체가 구글로 인증한번하고 회원가입 페이지로 이동하게 되어있음
                                                    // 대신에 이메일,닉네임 칸은 구글 이메일이 자동으로 들어가있음
    }

    private String getAccessToken(String authorizationCode, String registrationId) {
        String clientId = "241187094315-9h1rc47r7k69fr03uldk4dggtq8l739v.apps.googleusercontent.com";
        String clientSecret = "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";
        final String redirectUri = "http://localhost:8080/login/oauth2/code/google";
        final String tokenUri = "https://oauth2.googleapis.com/token";
         MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", authorizationCode);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity entity = new HttpEntity(params, headers);

        ResponseEntity<JsonNode> responseNode = restTemplate.exchange(tokenUri, HttpMethod.POST, entity, JsonNode.class);
        JsonNode accessTokenNode = responseNode.getBody();
        return accessTokenNode.get("access_token").asText();
    }

    private JsonNode getUserResource(String accessToken, String registrationId) {
        final String resourceUri = "https://www.googleapis.com/oauth2/v2/userinfo";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity entity = new HttpEntity(headers);
        return restTemplate.exchange(resourceUri, HttpMethod.GET, entity, JsonNode.class).getBody();
    }
}