package com.example.newneekclone.domain.user.exception;

import com.example.newneekclone.global.enums.ErrorCode;
import com.example.newneekclone.global.exception.GlobalException;

public class UserDuplicationException extends GlobalException {
    public UserDuplicationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
