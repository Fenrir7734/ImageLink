package com.fenrir.imagelink.controller;

import com.fenrir.imagelink.dto.ImageRequestDto;
import com.fenrir.imagelink.dto.ImageResponseDto;
import com.fenrir.imagelink.service.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping(
        path = "/api/v1/images",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class ImageController {
    private final ImageService imageService;

    @GetMapping("/{code}")
    public ResponseEntity<ImageResponseDto> getImageByCode(@PathVariable("code") String code) {
        return ResponseEntity.ok(imageService.getImage(code));
    }

    @PutMapping("/{code}")
    public ResponseEntity<ImageResponseDto> updateImage(
            @PathVariable("code") String code,
            @Valid @RequestBody ImageRequestDto image) {

        return ResponseEntity.ok(imageService.updateImage(code, image));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<ImageResponseDto> deleteImageByCode(@PathVariable("code") String code) {
        imageService.deleteImage(code);
        return ResponseEntity.noContent().build();
    }
}
