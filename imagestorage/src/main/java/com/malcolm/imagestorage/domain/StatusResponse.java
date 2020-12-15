package com.malcolm.imagestorage.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
@RegisterForReflection
public class StatusResponse {
    @Getter
    private final int statusCode;

    @Getter
    @NonNull
    private final String error;

    @Getter
    @NonNull
    private final String message;
}
