package com.malcolm.imagevalidation.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@RegisterForReflection
@AllArgsConstructor
public enum ImageMime {
    PNG("image/png"),
    GIF("image/gif"),
    JPEG("image/jpeg"),
    TIFF("image/tiff")
    ;

    @Getter
    private final String mimetype;

    public static ImageMime fromValue(String mimetype){
        return Arrays.stream(ImageMime.values()).filter(mime -> mime.getMimetype().equalsIgnoreCase(mimetype)).findFirst().orElse(null);
    }
}
