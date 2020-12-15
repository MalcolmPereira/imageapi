package com.malcolm.imageapi.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;
import java.io.File;

@RegisterForReflection
public class ImageUpload {
    @FormParam("image")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    public File image;

    @FormParam("scale")
    @PartType(MediaType.TEXT_PLAIN)
    public String scale;

    @FormParam("scaletype")
    @PartType(MediaType.TEXT_PLAIN)
    @DefaultValue(value = "BICUBIC")
    public String scaletype;

    @FormParam("width")
    @PartType(MediaType.TEXT_PLAIN)
    public String width;

    @FormParam("height")
    @PartType(MediaType.TEXT_PLAIN)
    public String height;
}
