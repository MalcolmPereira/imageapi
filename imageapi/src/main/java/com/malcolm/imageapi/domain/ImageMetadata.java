package com.malcolm.imageapi.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@RegisterForReflection
public class ImageMetadata {
    @Getter
    @Setter
    private String mimetype;

    @Getter
    @Setter
    private long size;

    @Getter
    @Setter
    private int width;

    @Getter
    @Setter
    private int height;
}
