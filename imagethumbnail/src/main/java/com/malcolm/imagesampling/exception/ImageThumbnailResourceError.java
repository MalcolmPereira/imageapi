package com.malcolm.imagesampling.exception;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
@RegisterForReflection
public enum ImageThumbnailResourceError {
    INVALID_DATA("Invalid Data - Please correct"),
    INVALID_IMAGE_FILE("Invalid Image File, Unsupported media type "),
    INVALID_FILE_PROCESSING("Exception processing image file "),
    SERVER_ERROR("Internal Server Error Occurred"),
    ;

    @Getter
    @NonNull
    private final String errorMessage;
}
