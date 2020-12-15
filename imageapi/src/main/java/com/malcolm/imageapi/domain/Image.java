package com.malcolm.imageapi.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@RegisterForReflection
public class Image {
    @Getter
    @Setter
    private String imageid;

    @Getter
    @Setter
    private String image;

    @Getter
    @Setter
    private String imagethumbnail;

    @Getter
    @Setter
    private String dateadded;
}
