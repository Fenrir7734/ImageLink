package com.fenrir.imagelink.service;

import com.fenrir.imagelink.dto.ImageRequestDto;
import com.fenrir.imagelink.dto.ImageResponseDto;
import com.fenrir.imagelink.dto.mapper.ImageMapper;
import com.fenrir.imagelink.exception.ResourceNotFoundException;
import com.fenrir.imagelink.model.Collection;
import com.fenrir.imagelink.model.Image;
import com.fenrir.imagelink.repository.ImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {
    @Mock
    private ImageRepository imageRepository;

    @Mock
    private ImageMapper imageMapper;

    @InjectMocks
    private ImageService imageService;

    private Image image;
    private ImageRequestDto imageRequestDto;
    private ImageResponseDto imageResponseDto;

    @BeforeEach
    public void setup() {
        image = new Image(1L, "ABCDEF", "url", "title", "description", LocalDateTime.of(2022, 1, 1, 12, 0), LocalDateTime.of(2022, 1, 1, 12, 0), new Collection());
        imageRequestDto = new ImageRequestDto("new_url", "new_title", "new_description");
        imageResponseDto = new ImageResponseDto("ABCDEF", "url", "title", "description", LocalDateTime.of(2022, 1, 1, 12, 0), LocalDateTime.of(2022, 1, 1, 12, 0));
    }

    @Test
    public void givenExistingCode_whenGetImage_thenReturnImageResponseDtoObject() {
        // Given
        given(imageRepository.findByCode(image.getCode()))
                .willReturn(Optional.of(image));
        given(imageMapper.toDto(image))
                .willReturn(imageResponseDto);

        // When
        ImageResponseDto actualImageResponseDto = imageService.getImage(image.getCode());

        // Then
        assertThat(actualImageResponseDto)
                .isNotNull()
                .isEqualTo(imageResponseDto);
    }

    @Test
    public void givenNoExistingCode_whenGetImage_thenThrowsException() {
        // Given
        String code = "ABCDEF";
        given(imageRepository.findByCode(any()))
                .willReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> imageService.getImage(code))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(String.format("Image was not found for code = %s", code));
    }

    @Test
    public void givenImageRequestDtoObject_whenUpdateImage_thenReturnImageResponseDtoObject() {
        // Given
        given(imageRepository.findByCode(image.getCode()))
                .willReturn(Optional.of(image));

        image.setOriginalUrl(imageRequestDto.getOriginalUrl());
        image.setTitle(imageRequestDto.getTitle());
        image.setDescription(imageRequestDto.getDescription());

        imageResponseDto.setOriginalUrl(imageRequestDto.getOriginalUrl());
        imageResponseDto.setTitle(imageRequestDto.getTitle());
        imageResponseDto.setDescription(imageRequestDto.getDescription());

        given(imageRepository.save(image))
                .willReturn(image);
        given(imageMapper.toDto(image))
                .willReturn(imageResponseDto);

        // When
        ImageResponseDto updatedImage = imageService.updateImage(image.getCode(), imageRequestDto);

        // Then
        assertThat(updatedImage)
                .isNotNull()
                .isEqualTo(imageResponseDto);
    }

    @Test
    public void givenNoExistingCode_whenUpdateImage_thenThrowsException() {
        // Given
        String code = "ABCDEF";
        given(imageRepository.findByCode(any()))
                .willReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> imageService.updateImage(code, imageRequestDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(String.format("Image was not found for code = %s", code));
    }

    @Test
    public void givenExistingCode_whenDeleteImage_thenNothing() {
        // Given
        given(imageRepository.findByCode(image.getCode()))
                .willReturn(Optional.of(image));
        willDoNothing().given(imageRepository).delete(image);

        // When
        imageService.deleteImage(image.getCode());

        // Then
        verify(imageRepository, times(1)).delete(image);
    }

    @Test
    public void givenNoExistingCode_whenDeleteImage_thenThrowsException() {
        // Given
        String code = "ABCDEF";
        given(imageRepository.findByCode(any()))
                .willReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> imageService.deleteImage(code))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(String.format("Image was not found for code = %s", code));
    }

}