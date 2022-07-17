package com.fenrir.imagelink.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RandomStringServiceTest {

    @Test
    public void givenPositiveLengthOfString_whenGenerate_thenReturnRandomString() {
        // Given
        int length = 6;
        RandomStringService randomStringService = new RandomStringService();

        // When
        String randomString = randomStringService.generate(length);

        // Then
        assertThat(randomString).isNotNull();
        assertThat(randomString.length()).isEqualTo(length);
    }

    @Test
    public void givenLengthOfStringEqualToZero_whenGenerate_thenReturnEmptyString() {
        // Given
        int length = 0;
        RandomStringService randomStringService = new RandomStringService();

        // When
        String randomString = randomStringService.generate(length);

        // Then
        assertThat(randomString).isEmpty();
    }

    @Test
    public void givenNegativeLengthOfString_whenGenerate_thenThrowsException() {
        // Given
        int length = -3;
        RandomStringService randomStringService = new RandomStringService();

        // Then
        assertThatThrownBy(() -> randomStringService.generate(length))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("length should be a positive integer");
    }
}