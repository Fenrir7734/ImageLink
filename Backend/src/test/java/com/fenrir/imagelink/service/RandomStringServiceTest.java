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
    public void givenLengthOfStringEqualToTwo_whenGenerate_thenReturnRandomString() {
        // Given
        int length = 2;
        RandomStringService randomStringService = new RandomStringService();

        // When
        String randomString = randomStringService.generate(length);

        // Then
        assertThat(randomString).isNotNull();
        assertThat(randomString.length()).isEqualTo(length);
    }

    @Test
    public void givenLengthOfStringSmallerThenTwo_whenGenerate_thenThrowsException() {
        // Given
        int length = 1;
        RandomStringService randomStringService = new RandomStringService();

        // Then
        assertThatThrownBy(() -> randomStringService.generate(length))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Length should be greater then 1");
    }
}