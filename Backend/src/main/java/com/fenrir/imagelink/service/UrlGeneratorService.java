package com.fenrir.imagelink.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class UrlGeneratorService {
    private static char[] alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
    private static int URL_LENGTH = 8;

    public String getRandomUrl() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < URL_LENGTH; i++) {
            int index = ThreadLocalRandom.current().nextInt(alphabet.length);
            builder.append(alphabet[index]);
        }
        return builder.toString();
    }
}
