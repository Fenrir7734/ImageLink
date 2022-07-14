package com.fenrir.imagelink.dto.mapper;

import com.fenrir.imagelink.dto.CollectionRequestDto;
import com.fenrir.imagelink.dto.CollectionResponseDto;
import com.fenrir.imagelink.model.Collection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CollectionMapper {
    CollectionResponseDto toDto(Collection collection);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Collection fromDto(CollectionRequestDto collectionDto);
}
