package com.fenrir.imagelink.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class CollectionRequestDto {

    @NotNull
    private Boolean hidden;

    @NotNull
    @Positive
    @Max(31622400000L)
    private Long lifePeriod;

    @NotBlank
    @Size(max = 254)
    private String title;

    @Size(max = 2048)
    private String description;
}
