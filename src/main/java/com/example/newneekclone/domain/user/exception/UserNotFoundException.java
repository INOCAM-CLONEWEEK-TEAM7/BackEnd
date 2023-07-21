package com.example.newneekclone.domain.user.exception;

import com.example.newneekclone.global.enums.ErrorCode;
import com.example.newneekclone.global.exception.GlobalException;

public class UserNotFoundException extends GlobalException {
    public UserNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
