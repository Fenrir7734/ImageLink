package com.fenrir.imagelink.exception.message;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.ConstraintViolation;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ConstraintViolationInfo {
    private String propertyName;
    private String invalidValue;
    private String violationMessage;

    public static ConstraintViolationInfo from(ConstraintViolation<?> cv) {
        return new ConstraintViolationInfo(
                cv.getPropertyPath().toString(),
                cv.getInvalidValue().toString(),
                cv.getMessage()
        );
    }
}
