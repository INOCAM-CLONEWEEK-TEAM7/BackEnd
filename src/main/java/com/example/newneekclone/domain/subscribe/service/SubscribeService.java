package com.example.newneekclone.domain.subscribe.service;

import com.example.newneekclone.domain.subscribe.dto.SubscribeRequestDto;
import com.example.newneekclone.domain.subscribe.entity.Subscribe;
import com.example.newneekclone.domain.subscribe.repository.SubscribeRepository;
import com.example.newneekclone.domain.user.exception.UserDuplicationException;
import com.example.newneekclone.global.enums.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.newneekclone.global.enums.ErrorCode.*;
import static com.example.newneekclone.global.enums.SuccessCode.*;

@RequiredArgsConstructor
@Service
public class SubscribeService {
    private final SubscribeRepository subscribeRepository;
    public SuccessCode subscribe(SubscribeRequestDto subscribeRequestDto) {
        String email = subscribeRequestDto.getEmail();
        String nickname = subscribeRequestDto.getNickname();
        if (checkEmail(email)) {
            throw new UserDuplicationException(DUPLICATE_RESOURCE);
        }

        // 구독자 등록
        Subscribe subscribe = new Subscribe(email, nickname);
        subscribeRepository.save(subscribe);
        return SUBSCRIBE_SUCCESS;
    }

    public Long subscribeCount() {
        return subscribeRepository.count();
    }

    public Boolean checkEmail(String email) {
        return subscribeRepository.existsByEmail(email);
    }
}
