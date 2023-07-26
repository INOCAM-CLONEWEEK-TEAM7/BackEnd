package com.example.newneekclone.domain.user.service;

import com.example.newneekclone.domain.user.dto.SocialSignUpRequestDto;
import com.example.newneekclone.domain.user.dto.UserRequestDto;
import com.example.newneekclone.domain.user.dto.UserResponseDto;
import com.example.newneekclone.domain.user.entity.User;
import com.example.newneekclone.domain.user.exception.UserDuplicationException;
import com.example.newneekclone.domain.user.exception.UserNotFoundException;
import com.example.newneekclone.domain.user.repository.UserRepository;
import com.example.newneekclone.global.enums.SuccessCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.newneekclone.global.enums.ErrorCode.*;
import static com.example.newneekclone.global.enums.SuccessCode.USER_DATA_UPDATE_SUCCESS;
import static com.example.newneekclone.global.enums.SuccessCode.USER_SIGNUP_SUCCESS;


@Service
@RequiredArgsConstructor
@Slf4j(topic = "User Service")
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SuccessCode signUp(UserRequestDto userRequestDto) {
        log.info("회원가입");
        String email = userRequestDto.getEmail();
        String nickname = userRequestDto.getNickname();
        String password = passwordEncoder.encode(userRequestDto.getPassword());
        Boolean marketingAgree = userRequestDto.getOptionCheck();

        if (checkEmail(email)) {
            throw new UserDuplicationException(DUPLICATE_EMAIL);
        } else if (checkNickname(nickname)) {
            throw new UserDuplicationException(DUPLICATE_NICKNAME);
        }

        // 사용자 등록
        User user = User.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .marketingAgree(marketingAgree)
                .social(false)
                .build();
        userRepository.save(user);

        return USER_SIGNUP_SUCCESS;
    }

    public SuccessCode signUp(SocialSignUpRequestDto socialSignUpRequestDto) {
        log.info("회원가입");
        String email = socialSignUpRequestDto.getEmail();
        String nickname = socialSignUpRequestDto.getNickname();
        Boolean marketingAgree = socialSignUpRequestDto.getOptionCheck();

        if (checkNickname(nickname)) {
            throw new UserDuplicationException(DUPLICATE_NICKNAME);
        }

        // 사용자 등록
        User user = User.builder()
                .email(email)
                .password("social")
                .nickname(nickname)
                .marketingAgree(marketingAgree)
                .social(true)
                .build();
        userRepository.save(user);

        return USER_SIGNUP_SUCCESS;
    }

    @Transactional
    public SuccessCode changePassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException(USER_NOT_FOUND)
        );
        newPassword = passwordEncoder.encode(newPassword);

        user.update(user.getNickname(), newPassword);

        return USER_DATA_UPDATE_SUCCESS;
    }

    public Boolean checkEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Boolean checkNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    public UserResponseDto findById(Long userId) {
        return new UserResponseDto(userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(USER_NOT_FOUND)
        ));
    }

    public String getNickname(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException(USER_NOT_FOUND)
        ).getNickname();
    }
}
