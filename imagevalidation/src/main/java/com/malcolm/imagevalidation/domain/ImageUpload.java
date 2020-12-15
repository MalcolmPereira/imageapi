package com.malcolm.imagevalidation.domain;

import javax.ws.rs.FormParam;
import java.io.File;

public class ImageUpload {
    @FormParam("image")
    public File image;
}
