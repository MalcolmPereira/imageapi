package com.malcolm.imagesampling.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;

import javax.ws.rs.FormParam;
import java.io.File;

@RegisterForReflection
public class ImageUpload {
    @FormParam("image")
    @Getter
    @Setter
    public File image;

    @FormParam("scaletype")
    @Getter
    @Setter
    public String scaletype;

    @FormParam("scale")
    @Getter
    @Setter
    public String scale;

    @FormParam("width")
    @Getter
    @Setter
    public String width;

    @FormParam("height")
    @Getter
    @Setter
    public String height;
}
