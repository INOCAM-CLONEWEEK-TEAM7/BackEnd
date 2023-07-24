package com.example.newneekclone.domain.user.service;

import com.example.newneekclone.domain.user.dto.UserRequestDto;
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

import static com.example.newneekclone.global.enums.ErrorCode.DUPLICATE_RESOURCE;
import static com.example.newneekclone.global.enums.ErrorCode.USER_NOT_FOUND;
import static com.example.newneekclone.global.enums.SuccessCode.USER_PASSWORD_CHANGE_SUCCESS;
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

        if (checkEmail(email) || checkNickname(nickname)) {
            throw new UserDuplicationException(DUPLICATE_RESOURCE);
        }

        // 사용자 등록
        User user = User.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
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

        return USER_PASSWORD_CHANGE_SUCCESS;
    }

    public Boolean checkEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Boolean checkNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }
}
