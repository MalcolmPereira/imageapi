package com.malcolm.imagevalidation.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.*;

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
