package com.malcolm.imagevalidation.exception;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
@RegisterForReflection
public enum ImageValidationResourceError {
    INVALID_DATA("Invalid Data - Please correct"),
    INVALID_FILETYPE("UnSupported Mime Type "),
    INVALID_FILE_PROCESSING("Exception processing image file "),
    SERVER_ERROR("Internal Server Error Occurred")
    ;

    @Getter
    @NonNull
    private final String errorMessage;
}
