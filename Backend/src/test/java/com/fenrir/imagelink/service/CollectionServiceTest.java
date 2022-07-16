package com.fenrir.imagelink.service;

import com.fenrir.imagelink.dto.CollectionRequestDto;
import com.fenrir.imagelink.dto.CollectionResponseDto;
import com.fenrir.imagelink.dto.ImageRequestDto;
import com.fenrir.imagelink.dto.ImageResponseDto;
import com.fenrir.imagelink.dto.mapper.CollectionMapper;
import com.fenrir.imagelink.dto.mapper.ImageMapper;
import com.fenrir.imagelink.exception.ResourceNotFoundException;
import com.fenrir.imagelink.model.Collection;
import com.fenrir.imagelink.model.Image;
import com.fenrir.imagelink.repository.CollectionRepository;
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
class CollectionServiceTest {

    @Mock
    private CollectionRepository collectionRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private CollectionMapper collectionMapper;

    @Mock
    private ImageMapper imageMapper;

    @InjectMocks
    private CollectionService collectionService;

    private Collection collection;
    private CollectionRequestDto collectionRequestDto;
    private CollectionResponseDto collectionResponseDto;
    private Image image;
    private ImageRequestDto imageRequestDto;
    private ImageResponseDto imageResponseDto;

    @BeforeEach
    public void setup() {
        collection = new Collection(1L, "FEDCBA", false, 10000L, "collection title", "collection description", LocalDateTime.of(2022, 1, 1, 12, 0), LocalDateTime.of(2022, 1, 1, 12, 0));
        collectionRequestDto = new CollectionRequestDto(false, 10000L, "collection title", "collection description");
        collectionResponseDto = new CollectionResponseDto("FEDCBA", false, 10000L, "collection title", "collection description", LocalDateTime.of(2022, 1, 1, 12, 0), LocalDateTime.of(2022, 1, 1, 12, 0));

        image = new Image(1L, "ABCDEF", "url", "title", "description", LocalDateTime.of(2022, 1, 1, 12, 0), LocalDateTime.of(2022, 1, 1, 12, 0), new Collection());
        imageRequestDto = new ImageRequestDto("url", "title", "description");
        imageResponseDto = new ImageResponseDto("ABCDEF", "url", "title", "description", LocalDateTime.of(2022, 1, 1, 12, 0), LocalDateTime.of(2022, 1, 1, 12, 0));
    }

    @Test
    public void givenExistingCode_whenGetCollection_thenReturnCollectionResponseDtoObject() {
        // Given
        given(collectionRepository.findByCode(collection.getCode()))
                .willReturn(Optional.of(collection));
        given(collectionMapper.toDto(collection))
                .willReturn(collectionResponseDto);

        // When
        CollectionResponseDto actualCollectionResponseDto = collectionService.getCollection(collection.getCode());

        // Then
        assertThat(actualCollectionResponseDto)
                .isNotNull()
                .isEqualTo(collectionResponseDto);
    }

    @Test
    public void givenNoExistingCode_whenGetCollection_thenThrowsException() {
        // Given
        String code = "ABCDEF";
        given(collectionRepository.findByCode(any()))
                .willReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> collectionService.getCollection(code))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(String.format("Collection was not found for code = %s", code));
    }

    @Test
    public void givenCollectionRequestObject_whenSaveCollection_thenReturnCollectionResponseDto() {
        // Given
        given(collectionMapper.fromDto(collectionRequestDto))
                .willReturn(collection);
        given(collectionRepository.save(collection))
                .willReturn(collection);
        given(collectionMapper.toDto(collection))
                .willReturn(collectionResponseDto);

        // When
        CollectionResponseDto actualCollectionResponseDto = collectionService.saveCollection(collectionRequestDto);

        // Then
        assertThat(actualCollectionResponseDto)
                .isNotNull()
                .isEqualTo(collectionResponseDto);
    }

    @Test
    public void givenImageRequestObject_whenSaveImage_thenReturnImageResponseDto() {
        // Given
        given(collectionRepository.findByCode(collection.getCode()))
                .willReturn(Optional.of(collection));
        given(imageMapper.fromDto(imageRequestDto))
                .willReturn(image);
        given(imageRepository.save(image))
                .willReturn(image);
        given(imageMapper.toDto(image))
                .willReturn(imageResponseDto);

        // When
        ImageResponseDto actualImageResponseDto = collectionService.saveImage(collection.getCode(), imageRequestDto);

        // Then
        assertThat(actualImageResponseDto)
                .isNotNull()
                .isEqualTo(imageResponseDto);
    }

    @Test
    public void givenNoExistingCollectionCode_whenSaveImage_thenThrowsException() {
        // Given
        String code = "ABCDEF";
        given(collectionRepository.findByCode(any()))
                .willReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> collectionService.getCollection(code))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(String.format("Collection was not found for code = %s", code));
    }

    @Test
    public void givenCollectionRequestDto_whenUpdateCollection_thenReturnCollectionResponseDto() {
        // Given
        given(collectionRepository.findByCode(collection.getCode()))
                .willReturn(Optional.of(collection));

        collectionRequestDto.setHidden(true);
        collectionRequestDto.setLifePeriod(200L);
        collectionRequestDto.setTitle("New title");
        collectionRequestDto.setDescription("New description");

        collection.setHidden(collectionRequestDto.getHidden());
        collection.setLifePeriod(collectionRequestDto.getLifePeriod());
        collection.setTitle(collectionRequestDto.getTitle());
        collection.setDescription(collectionRequestDto.getDescription());

        collectionResponseDto.setHidden(collectionRequestDto.getHidden());
        collectionResponseDto.setLifePeriod(collectionRequestDto.getLifePeriod());
        collectionResponseDto.setTitle(collectionRequestDto.getTitle());
        collectionResponseDto.setDescription(collectionRequestDto.getDescription());

        given(collectionRepository.save(collection))
                .willReturn(collection);
        given(collectionMapper.toDto(collection))
                .willReturn(collectionResponseDto);

        // When
        CollectionResponseDto updatedCollection = collectionService.updateCollection(collection.getCode(), collectionRequestDto);

        // Then
        assertThat(updatedCollection)
                .isNotNull()
                .isEqualTo(collectionResponseDto);
    }

    @Test
    public void givenNoExistingCode_whenUpdateCollection_thenThrowsException() {
        // Given
        String code = "ABCDEF";
        given(collectionRepository.findByCode(any()))
                .willReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> collectionService.updateCollection(code, collectionRequestDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(String.format("Collection was not found for code = %s", code));
    }

    @Test
    public void givenExistingCode_whenDeleteCollection_thenNothing() {
        // Given
        given(collectionRepository.findByCode(collection.getCode()))
                .willReturn(Optional.of(collection));
        willDoNothing().given(collectionRepository).delete(collection);

        // When
        collectionService.deleteCollection(collection.getCode());

        // Then
        verify(collectionRepository, times(1)).delete(collection);
    }

    @Test
    public void givenNoExistingCode_whenDeleteCollection_thenNothing() {
        // Given
        String code = "ABCDEF";
        given(collectionRepository.findByCode(any()))
                .willReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> collectionService.deleteCollection(code))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(String.format("Collection was not found for code = %s", code));
    }
}