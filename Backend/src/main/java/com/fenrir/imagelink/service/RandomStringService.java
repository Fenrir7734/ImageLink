package com.fenrir.imagelink.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class RandomStringService {
    private static final char[] alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

    public String generate(int length) {
        if (length < 0) {
            throw new IllegalArgumentException("length should be a positive integer");
        }

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = ThreadLocalRandom.current().nextInt(alphabet.length);
            builder.append(alphabet[index]);
        }
        return builder.toString();
    }
}
