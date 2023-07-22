package com.example.newneekclone.domain.news.exception;

import com.example.newneekclone.global.enums.ErrorCode;
import com.example.newneekclone.global.exception.GlobalException;

public class NewsNotFoundException extends GlobalException {
    public NewsNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}

