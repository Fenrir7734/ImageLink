package com.fenrir.imagelink.exception.message;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

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

    public static ConstraintViolationInfo from(FieldError fe) {
        return new ConstraintViolationInfo(
                fe.getField(),
                fe.getRejectedValue() != null ? fe.getRejectedValue().toString() : null,
                fe.getDefaultMessage()
        );
    }
}
