package com.example.newneekclone.domain.mail.entity;

import lombok.Builder;
import lombok.Getter;

@Getter
public class EmailMessage {
    private final String to;
    private final String subject;
    private final String message;

    @Builder
    private EmailMessage(String to, String subject, String message) {
        this.to = to;
        this.subject = subject;
        this.message = message;
    }
}
