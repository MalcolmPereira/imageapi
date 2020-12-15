package com.malcolm.imageapi.exception;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
@RegisterForReflection
public enum ImageCheckResourceError {
    SERVICE_ERROR("Service Error Occurred processing request"),
    SERVER_ERROR("Internal Server Error Occurred")
    ;
    @Getter
    @NonNull
    private final String errorMessage;
}