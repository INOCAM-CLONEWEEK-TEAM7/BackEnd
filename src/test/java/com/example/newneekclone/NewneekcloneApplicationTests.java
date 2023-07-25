package com.example.newneekclone;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NewneekcloneApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void jasypt() {

        String username = "username";
        String password = "password";

        String enUsername = jasyptEncoding(username);
        String enPassword = jasyptEncoding(password);
        System.out.println(enUsername);
        System.out.println(enPassword);
        System.out.println(jasyptDecoding(enUsername));
        System.out.println(jasyptDecoding(enPassword));
    }

    public String jasyptEncoding(String value) {

        String key = "newneekclone7";
        StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
        pbeEnc.setAlgorithm("PBEWithMD5AndDES");
        pbeEnc.setPassword(key);
        return pbeEnc.encrypt(value);
    }

    public String jasyptDecoding(String value) {

        String key = "newneekclone7";
        StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
        pbeEnc.setAlgorithm("PBEWithMD5AndDES");
        pbeEnc.setPassword(key);
        return pbeEnc.decrypt(value);
    }

}
