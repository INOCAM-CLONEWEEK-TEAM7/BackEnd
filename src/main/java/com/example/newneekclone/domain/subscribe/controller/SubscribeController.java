package com.example.newneekclone.domain.subscribe.controller;

import com.example.newneekclone.domain.subscribe.dto.SubscribeRequestDto;
import com.example.newneekclone.domain.subscribe.service.SubscribeService;
import com.example.newneekclone.global.responsedto.ApiResponse;
import com.example.newneekclone.global.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/news")
public class SubscribeController {
    private final SubscribeService subscribeService;

    @PostMapping("/subscribe")
    public ApiResponse<?> subscribeNews(@RequestBody SubscribeRequestDto subscribeRequestDto){
        return ResponseUtils.ok(subscribeService.subscribe(subscribeRequestDto));
    }

    @GetMapping("/subscribe_count")
    public ApiResponse<?> subscribeCount(){
        Number data = subscribeService.subscribeCount();
        return ResponseUtils.ok(data);
    }
}
