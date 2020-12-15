package com.malcolm.imagesampling;

import com.malcolm.imagesampling.domain.ImageScaleSampling;
import com.malcolm.imagesampling.domain.ImageUpload;
import com.malcolm.imagesampling.exception.ImageThumbnailException;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.config.LogConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.ws.rs.core.MediaType;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@QuarkusTest
public class ImageThumbnailResourceTest {

    @InjectMock
    ImageThumbnailService service;

    @BeforeAll
    static void setup() {
        config().logConfig(LogConfig.logConfig().enablePrettyPrinting(true));
    }


    @Test
    public void testGenerateImageEndpoint() throws ImageThumbnailException {
        Mockito.when(service.generate(any(ImageUpload.class),any(Map.class))).thenReturn(new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB));
        given()
                .log().all()
                .header("Content-Type", "multipart/form-data")
                .accept(MediaType.APPLICATION_OCTET_STREAM)
                .multiPart("image", Path.of(Paths.get("").toAbsolutePath().toString()+"/src/test/resources/cube.png").toFile())
                .multiPart("scale", "2")
                .post("/imagesampling/generate")
          .then()
             .log().all()
             .statusCode(200)
          ;

    }
}