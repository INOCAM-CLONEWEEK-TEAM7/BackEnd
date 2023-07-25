package com.example.newneekclone.global.utils;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class JasyptUtils {
    private final static StandardPBEStringEncryptor jasypt = new StandardPBEStringEncryptor();

    public static String encode(String text) {
        jasypt.setPassword("newneekclone7");
        return jasypt.encrypt(text);
    }

    public static String decode(String code) {
        jasypt.setPassword("newneekclone7");
        return jasypt.decrypt(code);
    }
}
