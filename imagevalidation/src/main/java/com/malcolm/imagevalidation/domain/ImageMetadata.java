package com.malcolm.imagevalidation.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.*;

@AllArgsConstructor
@RegisterForReflection
public class ImageMetadata {
    @Getter
    private final String mimetype;

    @Getter
    private final long size;

    @Getter
    private final int width;

    @Getter
    private final int height;
}
