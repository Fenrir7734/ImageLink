package com.fenrir.imagelink.service;

import com.fenrir.imagelink.dto.ImageRequestDto;
import com.fenrir.imagelink.dto.ImageResponseDto;
import com.fenrir.imagelink.dto.mapper.ImageMapper;
import com.fenrir.imagelink.exception.ResourceNotFoundException;
import com.fenrir.imagelink.model.Image;
import com.fenrir.imagelink.repository.ImageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ImageService {
    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;

    public ImageResponseDto getImage(String code) {
        return imageMapper.toDto(getImageByCode(code));
    }

    public ImageResponseDto updateImage(String code, ImageRequestDto updatedImage) {
        Image imageToUpdate = getImageByCode(code);
        imageToUpdate.setOriginalUrl(updatedImage.getOriginalUrl());
        imageToUpdate.setTitle(updatedImage.getTitle());
        imageToUpdate.setDescription(updatedImage.getDescription());
        imageToUpdate = imageRepository.save(imageToUpdate);
        return imageMapper.toDto(imageToUpdate);
    }

    public void deleteImage(String code) {
        imageRepository.delete(getImageByCode(code));
    }

    private Image getImageByCode(String code) {
        return imageRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Image was not found for code = %s", code)
                ));
    }
}
