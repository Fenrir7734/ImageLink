package com.fenrir.imagelink.dto.mapper;

import com.fenrir.imagelink.dto.ImageRequestDto;
import com.fenrir.imagelink.dto.ImageResponseDto;
import com.fenrir.imagelink.model.Image;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.List;

@Mapper
public interface ImageMapper {
    ImageResponseDto toDto(Image image);
    List<ImageResponseDto> toDto(Collection<Image> images);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "collection", ignore = true)
    Image fromDto(ImageRequestDto imageDto);
}
