package com.fenrir.imagelink.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class ImageRequestDto {

    @NotNull
    @URL
    private String originalUrl;

    @NotBlank
    @Size(max = 254)
    private String title;

    @Size(max = 2048)
    private String description;
}
