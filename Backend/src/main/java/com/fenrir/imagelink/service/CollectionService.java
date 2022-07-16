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
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CollectionService {
    private final CollectionRepository collectionRepository;
    private final ImageRepository imageRepository;

    private final CollectionMapper collectionMapper;
    private final ImageMapper imageMapper;

    public CollectionResponseDto getCollection(String code) {
        return collectionMapper.toDto(getCollectionByCode(code));
    }

    public List<ImageResponseDto> getAllImagesByCollectionCode(String collectionCode) {
        List<Image> images = imageRepository.findAllByCollectionCode(collectionCode);
        return imageMapper.toDto(images);
    }

    public CollectionResponseDto saveCollection(CollectionRequestDto collectionToSave) {
        Collection collection = collectionMapper.fromDto(collectionToSave);
        collection = collectionRepository.save(collection);
        return collectionMapper.toDto(collection);
    }

    public ImageResponseDto saveImage(String collectionCode, ImageRequestDto imageToSave) {
        Collection collection = getCollectionByCode(collectionCode);
        Image image = imageMapper.fromDto(imageToSave);
        image.setCollection(collection);
        image = imageRepository.save(image);
        return imageMapper.toDto(image);
    }

    public CollectionResponseDto updateCollection(String code, CollectionRequestDto updatedCollection) {
        Collection collectionToUpdate = getCollectionByCode(code);
        collectionToUpdate.setHidden(updatedCollection.getHidden());
        collectionToUpdate.setLifePeriod(updatedCollection.getLifePeriod());
        collectionToUpdate.setTitle(updatedCollection.getTitle());
        collectionToUpdate.setDescription(updatedCollection.getDescription());
        collectionToUpdate = collectionRepository.save(collectionToUpdate);
        return collectionMapper.toDto(collectionToUpdate);
    }

    public void deleteCollection(String code) {
        collectionRepository.delete(getCollectionByCode(code));
    }

    private Collection getCollectionByCode(String code) {
        return collectionRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Collection was not found for code = %s", code)
                ));
    }
}
