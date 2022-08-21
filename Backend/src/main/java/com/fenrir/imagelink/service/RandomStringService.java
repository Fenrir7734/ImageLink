package com.fenrir.imagelink.service;

import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class RandomStringService {

    public String generate(int length) {
        if (length < 2) {
            throw new IllegalArgumentException("Length should be greater then 1");
        }

        int n = (length * 3) / 4;
        byte[] arr = new byte[n];
        ThreadLocalRandom.current().nextBytes(arr);
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(arr);
    }
}
