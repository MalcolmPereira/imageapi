package com.malcolm.imagesampling.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.Arrays;

@RegisterForReflection
public enum ImageScaleSampling {
    BICUBIC,
    BILINEAR,
    NEAREST_NEIGHBOR
    ;

    public static ImageScaleSampling fromValue(String samplingType){
        return Arrays.stream(ImageScaleSampling.values())
                .filter(imageScaleSampling -> imageScaleSampling.toString().equalsIgnoreCase(samplingType))
                .findFirst().orElse(ImageScaleSampling.BICUBIC);
    }

}
