package com.malcolm.imagestorage.exception;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
@RegisterForReflection
public enum ImageStorageResourceError {
    INVALID_DATA("Invalid Data - Please correct"),
    NOT_FOUND("Image Not Found"),
    SERVER_ERROR("Internal Server Error Occurred"),
    ;

    @Getter
    @NonNull
    private final String errorMessage;
}
