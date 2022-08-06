package com.fenrir.imagelink.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fenrir.imagelink.dto.CollectionRequestDto;
import com.fenrir.imagelink.dto.CollectionResponseDto;
import com.fenrir.imagelink.dto.ImageRequestDto;
import com.fenrir.imagelink.dto.ImageResponseDto;
import com.fenrir.imagelink.exception.ResourceNotFoundException;
import com.fenrir.imagelink.service.CollectionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CollectionController.class)
class CollectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CollectionService collectionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void givenExistingCollectionCode_whenGetCollectionByCode_thenReturnSavedCollection() throws Exception {
        // Given
        String collectionCode = "ABCDEF";
        CollectionResponseDto collectionResponseDto = CollectionResponseDto.builder()
                .code(collectionCode)
                .lifePeriod(10000L)
                .title("title")
                .description("description")
                .createdAt(LocalDateTime.of(2022, 1, 1, 12, 10, 10))
                .updatedAt(LocalDateTime.of(2022, 1, 1, 12, 10, 10))
                .build();
        given(collectionService.getCollection(collectionCode))
                .willReturn(collectionResponseDto);

        // When
        ResultActions response = mockMvc.perform(get("/api/v1/collections/{code}", collectionCode));

        // Then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.code", is(collectionResponseDto.getCode())))
                .andExpect(jsonPath("$.lifePeriod", is(collectionResponseDto.getLifePeriod().intValue())))
                .andExpect(jsonPath("$.title", is(collectionResponseDto.getTitle())))
                .andExpect(jsonPath("$.description", is(collectionResponseDto.getDescription())))
                .andExpect(jsonPath("$.createdAt", is(collectionResponseDto.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updatedAt", is(collectionResponseDto.getUpdatedAt().toString())));
    }

    @Test
    public void givenNoExistingCollectionCode_whenGetCollectionByCode_thenReturnErrorMessage() throws Exception {
        // Given
        String collectionCode = "ABCDEF";
        String errorMessage = String.format("Resource not found for code = %s", collectionCode);

        given(collectionService.getCollection(collectionCode))
                .willThrow(new ResourceNotFoundException(errorMessage));

        // When
        ResultActions response = mockMvc.perform(get("/api/v1/collections/{code}", collectionCode));

        // Then
        response.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.statusCode", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                .andExpect(jsonPath("$.message", is(errorMessage)))
                .andExpect(jsonPath("$.description", is(notNullValue())));
    }

    @Test
    public void givenExistingCollectionCode_whenGetAllImagesByCollectionCode_thenReturnListOfImages() throws Exception {
        // Given
        String collectionCode = "ABCDEF";
        List<ImageResponseDto> images = List.of(
                ImageResponseDto.builder()
                        .code("FEDCBA")
                        .originalUrl("http://localhost:1234/image1.png")
                        .title("Image 1")
                        .description("description")
                        .createdAt(LocalDateTime.of(2022, 1, 1, 12, 10, 10))
                        .updatedAt(LocalDateTime.of(2022, 1, 1, 12, 10, 10))
                        .build(),
                ImageResponseDto.builder()
                        .code("CBAFED")
                        .originalUrl("http://localhost:1234/image.png")
                        .title("Image 2")
                        .description("description")
                        .createdAt(LocalDateTime.of(2022, 1, 1, 12, 10, 10))
                        .updatedAt(LocalDateTime.of(2022, 1, 1, 12, 10, 10))
                        .build()
        );
        given(collectionService.getAllImagesByCollectionCode(collectionCode))
                .willReturn(images);

        // When
        ResultActions response = mockMvc.perform(get("/api/v1/collections/{collectionCode}/images", collectionCode));

        // Then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(images.size())));
    }

    @Test
    public void givenNoExistingCollectionCode_whenGetAllImagesByCollectionCode_thenReturnErrorMessage() throws Exception {
        // Given
        String collectionCode = "ABCDEF";
        String errorMessage = String.format("Resource not found for code = %s", collectionCode);

        given(collectionService.getAllImagesByCollectionCode(collectionCode))
                .willThrow(new ResourceNotFoundException(errorMessage));

        // When
        ResultActions response = mockMvc.perform(get("/api/v1/collections/{collectionCode}/images", collectionCode));

        // Then
        response.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.statusCode", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                .andExpect(jsonPath("$.message", is(errorMessage)))
                .andExpect(jsonPath("$.description", is(notNullValue())));
    }

    @Test
    public void givenCollectionObject_whenSaveCollection_thenReturnSavedCollection() throws Exception {
        // Given
        String collectionCode = "ABCDEF";
        CollectionRequestDto collectionRequestDto = CollectionRequestDto.builder()
                .lifePeriod(10000L)
                .title("title")
                .description("description")
                .build();
        CollectionResponseDto collectionResponseDto = CollectionResponseDto.builder()
                .code(collectionCode)
                .lifePeriod(1000L)
                .title("title")
                .description("description")
                .createdAt(LocalDateTime.of(2022, 1, 1, 12, 10, 10))
                .updatedAt(LocalDateTime.of(2022, 1, 1, 12, 10, 10))
                .build();

        given(collectionService.saveCollection(collectionRequestDto))
                .willReturn(collectionResponseDto);

        // When
        ResultActions response = mockMvc.perform(post("/api/v1/collections")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(collectionRequestDto)));

        // Then
        response.andExpect(status().isCreated())
                .andDo(print())
                .andExpect(header().string("Location", endsWith(String.format("/api/v1/collections/%s", collectionCode))))
                .andExpect(jsonPath("$.code", is(collectionResponseDto.getCode())))
                .andExpect(jsonPath("$.lifePeriod", is(collectionResponseDto.getLifePeriod().intValue())))
                .andExpect(jsonPath("$.title", is(collectionResponseDto.getTitle())))
                .andExpect(jsonPath("$.description", is(collectionResponseDto.getDescription())))
                .andExpect(jsonPath("$.createdAt", is(collectionResponseDto.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updatedAt", is(collectionResponseDto.getUpdatedAt().toString())));
    }

    @Test
    public void givenExistingCollectionCodeAndImageObject_whenSaveImage_thenReturnSavedImage() throws Exception {
        // Given
        String collectionCode = "ABCDEF";
        String imageCode = "FEDCBA";
        ImageRequestDto imageRequestDto = ImageRequestDto.builder()
                .originalUrl("http://localhost:1234/image.png")
                .title("Image")
                .description("description")
                .build();
        ImageResponseDto imageResponseDto = ImageResponseDto.builder()
                .code(imageCode)
                .originalUrl("http://localhost:1234/image.png")
                .title("Image")
                .description("description")
                .createdAt(LocalDateTime.of(2022, 1, 1, 12, 10, 10))
                .updatedAt(LocalDateTime.of(2022, 1, 1, 12, 10, 10))
                .build();

        given(collectionService.saveImage(collectionCode, imageRequestDto))
                .willReturn(imageResponseDto);

        // When
        ResultActions response = mockMvc.perform(post("/api/v1/collections/{collectionsCode}", collectionCode)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(imageRequestDto)));

        // Then
        response.andExpect(status().isCreated())
                .andDo(print())
                .andExpect(header().string("Location", endsWith(String.format("/api/v1/images/%s", imageCode))))
                .andExpect(jsonPath("$.code", is(imageResponseDto.getCode())))
                .andExpect(jsonPath("$.originalUrl", is(imageResponseDto.getOriginalUrl())))
                .andExpect(jsonPath("$.title", is(imageResponseDto.getTitle())))
                .andExpect(jsonPath("$.description", is(imageResponseDto.getDescription())))
                .andExpect(jsonPath("$.createdAt", is(imageResponseDto.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updatedAt", is(imageResponseDto.getUpdatedAt().toString())));
    }

    @Test
    public void givenNoExistingCollectionCode_whenSaveImage_thenReturnErrorMessage() throws Exception {
        // Given
        String collectionCode = "ABCDEF";
        ImageRequestDto imageRequestDto = ImageRequestDto.builder()
                .originalUrl("http://localhost:1234/image.png")
                .title("Image")
                .description("description")
                .build();
        String errorMessage = String.format("Resource not found for code = %s", collectionCode);

        given(collectionService.saveImage(collectionCode, imageRequestDto))
                .willThrow(new ResourceNotFoundException(errorMessage));

        // When
        ResultActions response = mockMvc.perform(post("/api/v1/collections/{collectionCode}", collectionCode)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(imageRequestDto)));

        // Then
        response.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.statusCode", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                .andExpect(jsonPath("$.message", is(errorMessage)))
                .andExpect(jsonPath("$.description", is(notNullValue())));
    }

    @Test
    public void givenExistingCollectionCodeAndCollectionObject_whenUpdateCollection_thenReturnUpdatedCollections() throws Exception {
        // Given
        String collectionCode = "ABCDEF";
        CollectionRequestDto collectionRequestDto = CollectionRequestDto.builder()
                .lifePeriod(10000L)
                .title("Updated title")
                .description("Updated description")
                .build();
        CollectionResponseDto collectionResponseDto = CollectionResponseDto.builder()
                .code(collectionCode)
                .lifePeriod(1000L)
                .title("Updated title")
                .description("Updated description")
                .createdAt(LocalDateTime.of(2022, 1, 1, 12, 10, 10))
                .updatedAt(LocalDateTime.of(2022, 1, 1, 12, 10, 10))
                .build();

        given(collectionService.updateCollection(collectionCode, collectionRequestDto))
                .willReturn(collectionResponseDto);

        // When
        ResultActions response = mockMvc.perform(put("/api/v1/collections/{code}", collectionCode)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(collectionRequestDto)));

        // Then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.code", is(collectionResponseDto.getCode())))
                .andExpect(jsonPath("$.lifePeriod", is(collectionResponseDto.getLifePeriod().intValue())))
                .andExpect(jsonPath("$.title", is(collectionResponseDto.getTitle())))
                .andExpect(jsonPath("$.description", is(collectionResponseDto.getDescription())))
                .andExpect(jsonPath("$.createdAt", is(collectionResponseDto.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updatedAt", is(collectionResponseDto.getUpdatedAt().toString())));
    }

    @Test
    public void givenNoExistingCollectionCode_whenUpdateCollection_thenReturnErrormessage() throws Exception {
        // Given
        String collectionCode = "ABCDEF";
        CollectionRequestDto collectionRequestDto = CollectionRequestDto.builder()
                .lifePeriod(10000L)
                .title("Updated title")
                .description("Updated description")
                .build();
        String errorMessage = String.format("Resource not found for code = %s", collectionCode);

        given(collectionService.updateCollection(collectionCode, collectionRequestDto))
                .willThrow(new ResourceNotFoundException(errorMessage));

        // When
        ResultActions response = mockMvc.perform(put("/api/v1/collections/{code}", collectionCode)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(collectionRequestDto)));

        // Then
        response.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.statusCode", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                .andExpect(jsonPath("$.message", is(errorMessage)))
                .andExpect(jsonPath("$.description", is(notNullValue())));
    }

    @Test
    public void givenExistingCollectionCode_whenDeleteCollection_thenReturnNoContentHttpStatus() throws Exception {
        // Given
        String collectionCode = "ABCDEF";
        willDoNothing().given(collectionService).deleteCollection(collectionCode);

        // When
        ResultActions response = mockMvc.perform(delete("/api/v1/collections/{code}", collectionCode));

        // Then
        response.andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    public void givenNoExistingCollectionCode_whenDeleteCollection_thenReturnErrormessage() throws Exception {
        // Given
        String collectionCode = "ABCDEF";
        String errorMessage = String.format("Resource not found for code = %s", collectionCode);
        willThrow(new ResourceNotFoundException(errorMessage))
                .given(collectionService).deleteCollection(collectionCode);

        // When
        ResultActions response = mockMvc.perform(delete("/api/v1/collections/{code}", collectionCode));

        // Then
        response.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.statusCode", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                .andExpect(jsonPath("$.message", is(errorMessage)))
                .andExpect(jsonPath("$.description", is(notNullValue())));
    }
}