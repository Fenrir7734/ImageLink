package com.fenrir.imagelink.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RandomStringServiceTest {

    @Test
    public void givenLengthOfString_whenGenerate_thenReturnRandomString() {
        // Given
        int length = 6;
        RandomStringService randomStringService = new RandomStringService();

        // When
        String randomString = randomStringService.generate(length);

        // Then
        assertThat(randomString).isNotNull();
        assertThat(randomString.length()).isEqualTo(length);
    }
}