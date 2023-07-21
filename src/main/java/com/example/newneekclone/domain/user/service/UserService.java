package com.example.newneekclone.domain.user.service;

import com.example.newneekclone.domain.user.dto.UserRequestDto;
import com.example.newneekclone.domain.user.entity.User;
import com.example.newneekclone.domain.user.exception.UserDuplicationException;
import com.example.newneekclone.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.newneekclone.global.enums.ErrorCode.DUPLICATE_RESOURCE;


@Service
@RequiredArgsConstructor
@Slf4j(topic = "User Service")
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signIn(UserRequestDto userRequestDto) {
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
    }

    public Boolean checkEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Boolean checkNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }
}
