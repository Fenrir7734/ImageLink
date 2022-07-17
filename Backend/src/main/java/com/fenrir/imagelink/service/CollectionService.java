package com.fenrir.imagelink.service;

import com.fenrir.imagelink.dto.CollectionRequestDto;
import com.fenrir.imagelink.dto.CollectionResponseDto;
import com.fenrir.imagelink.dto.ImageRequestDto;
import com.fenrir.imagelink.dto.ImageResponseDto;
import com.fenrir.imagelink.dto.mapper.CollectionMapper;
import com.fenrir.imagelink.dto.mapper.ImageMapper;
import com.fenrir.imagelink.exception.CodeGenerationException;
import com.fenrir.imagelink.exception.ResourceNotFoundException;
import com.fenrir.imagelink.model.Collection;
import com.fenrir.imagelink.model.Image;
import com.fenrir.imagelink.repository.CollectionRepository;
import com.fenrir.imagelink.repository.ImageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

@AllArgsConstructor
@Service
public class CollectionService {
    private final RandomStringService randomStringService;

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
        collection.setCode(generateCode(collectionRepository::existsByCode));
        collection = collectionRepository.save(collection);
        return collectionMapper.toDto(collection);
    }

    public ImageResponseDto saveImage(String collectionCode, ImageRequestDto imageToSave) {
        Collection collection = getCollectionByCode(collectionCode);
        Image image = imageMapper.fromDto(imageToSave);
        image.setCode(generateCode(imageRepository::existsByCode));
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

    private String generateCode(Predicate<String> existsByCodePredicate) {
        int count = 0;
        String code;

        do {
            code = randomStringService.generate(6);
            count++;
        } while (existsByCodePredicate.test(code) && count < 10);

        if (existsByCodePredicate.test(code)) {
            throw new CodeGenerationException("Failed to generate code");
        }

        return code;
    }

    private Collection getCollectionByCode(String code) {
        return collectionRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Collection was not found for code = %s", code)
                ));
    }
}
