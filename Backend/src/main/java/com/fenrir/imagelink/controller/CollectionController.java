package com.fenrir.imagelink.controller;

import com.fenrir.imagelink.dto.CollectionRequestDto;
import com.fenrir.imagelink.dto.CollectionResponseDto;
import com.fenrir.imagelink.dto.ImageRequestDto;
import com.fenrir.imagelink.dto.ImageResponseDto;
import com.fenrir.imagelink.service.CollectionService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(
        path = "/api/v1/collections",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class CollectionController {
    private final CollectionService collectionService;

    @GetMapping("/{code}")
    public ResponseEntity<CollectionResponseDto> getCollectionByCode(@PathVariable("code") String code) {
        return ResponseEntity.ok(collectionService.getCollection(code));
    }

    @GetMapping("/{code}/images")
    public ResponseEntity<List<ImageResponseDto>> getAllImagesByCollectionCode(@PathVariable("code") String code) {
        return ResponseEntity.ok(collectionService.getAllImagesByCollectionCode(code));
    }

    @PostMapping
    public ResponseEntity<CollectionResponseDto> saveCollection(
            @Valid @RequestBody CollectionRequestDto collectionRequestDto,
            UriComponentsBuilder builder) {

        CollectionResponseDto collectionResponseDto = collectionService.saveCollection(collectionRequestDto);
        URI location = builder.replacePath("/api/v1/collections/{code}")
                .buildAndExpand(collectionResponseDto.getCode())
                .toUri();
        return ResponseEntity.created(location).body(collectionResponseDto);
    }

    @PostMapping("/{collectionCode}")
    public ResponseEntity<ImageResponseDto> saveImage(
            @PathVariable("collectionCode") String collectionCode,
            @Valid @RequestBody ImageRequestDto imageRequestDto,
            UriComponentsBuilder builder) {

        ImageResponseDto imageResponseDto = collectionService.saveImage(collectionCode, imageRequestDto);
        URI location = builder.replacePath("/api/v1/images/{code}")
                .buildAndExpand(imageResponseDto.getCode())
                .toUri();
        return ResponseEntity.created(location).body(imageResponseDto);
    }

    @PutMapping("/{code}")
    public ResponseEntity<CollectionResponseDto> updateCollection(
            @PathVariable("code") String code,
            @Valid @RequestBody CollectionRequestDto collectionRequestDto) {

        return ResponseEntity.ok(collectionService.updateCollection(code, collectionRequestDto));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Void> deleteCollection(@PathVariable("code") String code) {
        collectionService.deleteCollection(code);
        return ResponseEntity.noContent().build();
    }
}
