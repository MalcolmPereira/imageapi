package com.malcolm.imageapi.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@RegisterForReflection
public class StatusResponse {
    @Getter
    @Setter
    private int statusCode;

    @Getter
    @Setter
    private String error;

    @Getter
    @Setter
    private String message;
}
