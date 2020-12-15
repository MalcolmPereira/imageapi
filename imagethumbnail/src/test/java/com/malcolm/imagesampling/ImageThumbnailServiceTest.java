package com.malcolm.imagesampling;

import com.malcolm.imagesampling.domain.ImageUpload;
import com.malcolm.imagesampling.exception.ImageThumbnailException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ImageThumbnailServiceTest {

    static ImageThumbnailService service;

    @BeforeAll
    static void setUp(){
        service = new ImageThumbnailService();
    }


    @Test
    void generateImageScaleUpPNG() throws ImageThumbnailException {
        ImageUpload imgUpload = new ImageUpload();
        imgUpload.setImage(Path.of(Paths.get("").toAbsolutePath().toString()+"/src/test/resources/cube.png").toFile());
        imgUpload.setScale("2.0");
        BufferedImage imageData = service.generate(imgUpload, new HashMap<>());
        assertNotNull(imageData);
    }

    @Test
    void generateImageScaleDownPNG() throws ImageThumbnailException {
        ImageUpload imgUpload = new ImageUpload();
        imgUpload.setImage(Path.of(Paths.get("").toAbsolutePath().toString()+"/src/test/resources/cube.png").toFile());
        imgUpload.setScale("0.5");
        BufferedImage imageData = service.generate(imgUpload,new HashMap<>());
        assertNotNull(imageData);
    }

    @Test
    void generateImageScaleUpJPEG() throws ImageThumbnailException {
        ImageUpload imgUpload = new ImageUpload();
        imgUpload.setImage(Path.of(Paths.get("").toAbsolutePath().toString()+"/src/test/resources/jpeg_image.jpg").toFile());
        imgUpload.setScale("2.0");
        BufferedImage imageData = service.generate(imgUpload,new HashMap<>());
        assertNotNull(imageData);
    }

    @Test
    void generateImageScaleDownJPEG() throws ImageThumbnailException {
        ImageUpload imgUpload = new ImageUpload();
        imgUpload.setImage(Path.of(Paths.get("").toAbsolutePath().toString()+"/src/test/resources/jpeg_image.jpg").toFile());
        imgUpload.setScale("0.5");
        BufferedImage imageData = service.generate(imgUpload,new HashMap<>());
        assertNotNull(imageData);
    }

    @Test
    void generateImageScaleUpTIFF() throws ImageThumbnailException {
        ImageUpload imgUpload = new ImageUpload();
        imgUpload.setImage(Path.of(Paths.get("").toAbsolutePath().toString()+"/src/test/resources/tiff_image.tiff").toFile());
        imgUpload.setScale("2.0");
        BufferedImage imageData = service.generate(imgUpload,new HashMap<>());
        assertNotNull(imageData);
    }

    @Test
    void generateImageScaleDownTIFF() throws ImageThumbnailException {
        ImageUpload imgUpload = new ImageUpload();
        imgUpload.setImage(Path.of(Paths.get("").toAbsolutePath().toString()+"/src/test/resources/tiff_image.tiff").toFile());
        imgUpload.setScale("0.5");

        BufferedImage imageData = service.generate(imgUpload,new HashMap<>());
        assertNotNull(imageData);
    }

    @Test
    void generateImageNullException() {
        Assertions.assertThrows(ImageThumbnailException.class, () -> service.generate(null,new HashMap<>()));
    }

}