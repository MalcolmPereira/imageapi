package com.malcolm.imagevalidation;

import com.malcolm.imagevalidation.domain.ImageMetadata;
import com.malcolm.imagevalidation.domain.ImageMime;
import com.malcolm.imagevalidation.exception.ImageValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ImageValidationServiceTest {

    static ImageValidationService service;

    @BeforeAll
    static void setUp(){
        service = new ImageValidationService();
    }

    @Test
    void validateImagePNG() throws ImageValidationException {
        ImageMetadata imageMetadata = service.validateImage(Path.of(Paths.get("").toAbsolutePath().toString()+"/src/test/resources/cube.png").toFile());
        assertEquals(ImageMime.PNG.getMimetype(),imageMetadata.getMimetype());
        assertEquals(10099,imageMetadata.getSize());
        assertEquals(246,imageMetadata.getHeight());
        assertEquals(283,imageMetadata.getWidth());
    }

    @Test
    void validateImageJPEG() throws ImageValidationException {
        ImageMetadata imageMetadata = service.validateImage(Path.of(Paths.get("").toAbsolutePath().toString()+"/src/test/resources/jpeg_image.jpg").toFile());
        assertEquals(ImageMime.JPEG.getMimetype(),imageMetadata.getMimetype());
        assertEquals(10127,imageMetadata.getSize());
        assertEquals(64,imageMetadata.getHeight());
        assertEquals(196,imageMetadata.getWidth());
    }

    @Test
    void validateImageTIFF() throws ImageValidationException {
        ImageMetadata imageMetadata = service.validateImage(Path.of(Paths.get("").toAbsolutePath().toString()+"/src/test/resources/tiff_image.tiff").toFile());
        assertEquals(ImageMime.TIFF.getMimetype(),imageMetadata.getMimetype());
        assertEquals(54782,imageMetadata.getSize());
        assertEquals(64,imageMetadata.getHeight());
        assertEquals(196,imageMetadata.getWidth());
    }

    @Test
    void validateImageJPEG1() throws ImageValidationException {
        ImageMetadata imageMetadata = service.validateImage(Path.of(Paths.get("").toAbsolutePath().toString()+"/src/test/resources/file1.jpeg").toFile());
        assertEquals(25416,imageMetadata.getSize());
        assertEquals(400,imageMetadata.getHeight());
        assertEquals(400,imageMetadata.getWidth());
    }

    @Test
    void validateImageJPG1() throws ImageValidationException {
        ImageMetadata imageMetadata = service.validateImage(Path.of(Paths.get("").toAbsolutePath().toString()+"/src/test/resources/file1.jpg").toFile());
        assertEquals(33448,imageMetadata.getSize());
        assertEquals(212,imageMetadata.getHeight());
        assertEquals(318,imageMetadata.getWidth());
    }

    @Test
    void validateImageGIF() throws ImageValidationException {
        ImageMetadata imageMetadata = service.validateImage(Path.of(Paths.get("").toAbsolutePath().toString()+"/src/test/resources/file1.gif").toFile());
        assertEquals(2220,imageMetadata.getSize());
        assertEquals(400,imageMetadata.getHeight());
        assertEquals(400,imageMetadata.getWidth());
    }

    @Test
    void validateImageNullException() {
        Assertions.assertThrows(ImageValidationException.class, () ->{
            service.validateImage(null);
        });
    }

    @Test
    void validateImageInvalidException()  {
        Assertions.assertThrows(ImageValidationException.class, () ->{
            service.validateImage(Path.of(Paths.get("").toAbsolutePath().toString()+"/src/test/resources/data.txt").toFile());
        });
    }
}