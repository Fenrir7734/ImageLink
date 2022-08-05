package com.fenrir.imagelink.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fenrir.imagelink.dto.ImageRequestDto;
import com.fenrir.imagelink.dto.ImageResponseDto;
import com.fenrir.imagelink.exception.ResourceNotFoundException;
import com.fenrir.imagelink.service.ImageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ImageController.class)
class ImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImageService imageService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void givenExistingImageCode_whenGetImageByCode_thenReturnSavedImage() throws Exception {
        // Given
        String imageCode = "ABCDEF";
        ImageResponseDto imageResponseDto = ImageResponseDto.builder()
                .code(imageCode)
                .originalUrl("http://localhost:1234/image.png")
                .title("Image")
                .description("description")
                .createdAt(LocalDateTime.of(2022, 1, 1, 12, 10, 10))
                .updatedAt(LocalDateTime.of(2022, 1, 1, 12, 10, 10))
                .build();
        given(imageService.getImage(imageCode))
                .willReturn(imageResponseDto);

        // When
        ResultActions response = mockMvc.perform(get("/api/v1/images/{code}", imageCode));

        // Then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.code", is(imageResponseDto.getCode())))
                .andExpect(jsonPath("$.originalUrl", is(imageResponseDto.getOriginalUrl())))
                .andExpect(jsonPath("$.title", is(imageResponseDto.getTitle())))
                .andExpect(jsonPath("$.description", is(imageResponseDto.getDescription())))
                .andExpect(jsonPath("$.createdAt", is(imageResponseDto.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updatedAt", is(imageResponseDto.getUpdatedAt().toString())));
    }

    @Test
    public void givenNoExistingImageCode_whenGetImageByCode_thenReturnErrorMessage() throws Exception {
        // Given
        String imageCode = "ABCDEF";
        String errorMessage = String.format("Resource not found for code = %s", imageCode);

        given(imageService.getImage(imageCode))
                .willThrow(new ResourceNotFoundException(errorMessage));

        // When
        ResultActions response = mockMvc.perform(get("/api/v1/images/{code}", imageCode));

        // Then
        response.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.statusCode", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                .andExpect(jsonPath("$.message", is(errorMessage)))
                .andExpect(jsonPath("$.description", is(notNullValue())));
    }

    @Test
    public void givenExistingImageCodeAndImageObject_whenUpdateImage_thenReturnUpdatedImage() throws Exception {
        // Given
        String imageCode = "ABCDEF";
        ImageRequestDto updatedImageRequestDto = ImageRequestDto.builder()
                .originalUrl("http://localhost:1234/updated_image.png")
                .title("updated_title")
                .description("updated_description")
                .build();
        ImageResponseDto updatedImageResponseDto = ImageResponseDto.builder()
                .code(imageCode)
                .originalUrl("http://localhost:1234/updated_image.png")
                .title("updated_title")
                .description("updated_description")
                .createdAt(LocalDateTime.of(2022, 1, 1, 12, 10, 10))
                .updatedAt(LocalDateTime.of(2022, 1, 1, 12, 10, 10))
                .build();

        given(imageService.updateImage(imageCode, updatedImageRequestDto))
                .willReturn(updatedImageResponseDto);

        // When
        ResultActions response = mockMvc.perform(put("/api/v1/images/{code}", imageCode)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedImageRequestDto)));

        // Then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.code", is(updatedImageResponseDto.getCode())))
                .andExpect(jsonPath("$.originalUrl", is(updatedImageResponseDto.getOriginalUrl())))
                .andExpect(jsonPath("$.title", is(updatedImageResponseDto.getTitle())))
                .andExpect(jsonPath("$.description", is(updatedImageResponseDto.getDescription())))
                .andExpect(jsonPath("$.createdAt", is(updatedImageResponseDto.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updatedAt", is(updatedImageResponseDto.getUpdatedAt().toString())));
    }

    @Test
    public void givenNoExistingImageCode_whenUpdateImage_thenReturnErrorMessage() throws Exception {
        // Given
        String imageCode = "ABCDEF";
        ImageRequestDto updatedImageRequestDto = ImageRequestDto.builder()
                .originalUrl("http://localhost:1234/updated_image.png")
                .title("updated_title")
                .description("updated_description")
                .build();
        String errorMessage = String.format("Resource not found for code = %s", imageCode);

        given(imageService.updateImage(imageCode, updatedImageRequestDto))
                .willThrow(new ResourceNotFoundException(errorMessage));

        // When
        ResultActions response = mockMvc.perform(put("/api/v1/images/{code}", imageCode)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedImageRequestDto)));

        // Then
        response.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.statusCode", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                .andExpect(jsonPath("$.message", is(errorMessage)))
                .andExpect(jsonPath("$.description", is(notNullValue())));
    }

    @Test
    public void givenExistingImageCode_whenDeleteImage_thenReturnNoContentHttpStatus() throws Exception {
        // Given
        String imageCode = "ABCDEF";
        willDoNothing().given(imageService).deleteImage(imageCode);

        // When
        ResultActions response = mockMvc.perform(delete("/api/v1/images/{code}", imageCode));

        // Then
        response.andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    public void givenNoExistingImageCode_whenDeleteImage_thenReturnNoContentHttpStatus() throws Exception {
        // Given
        String imageCode = "ABCDEF";
        String errorMessage = String.format("Resource not found for code = %s", imageService);
        willThrow(new ResourceNotFoundException(errorMessage))
                .given(imageService).deleteImage(imageCode);
        // When
        ResultActions response = mockMvc.perform(delete("/api/v1/images/{code}", imageCode));

        // Then
        response.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.statusCode", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                .andExpect(jsonPath("$.message", is(errorMessage)))
                .andExpect(jsonPath("$.description", is(notNullValue())));
    }
}