package com.malcolm.imagevalidation;

import com.malcolm.imagevalidation.domain.ImageMetadata;
import com.malcolm.imagevalidation.exception.ImageValidationException;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.config.LogConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
public class ImageValidationResourceTest {

    @InjectMock
    ImageValidationService service;

    @BeforeAll
    static void setup() {
        config().logConfig(LogConfig.logConfig().enablePrettyPrinting(true));
    }


    @Test
    public void testValidateImageEndpoint() throws ImageValidationException {
        ImageMetadata metadata = new ImageMetadata("image/png",10099,283,246);
        Mockito.when(service.validateImage(any(File.class))).thenReturn(metadata);
        given()
                .log().all()
                .header("Content-Type", "multipart/form-data")
                .multiPart("image", Path.of(Paths.get("").toAbsolutePath().toString()+"/src/test/resources/cube.png").toFile())
                .post("/imagevalidation/validate")
          .then()
             .statusCode(200)
             .body("size",is(10099))
             .body("height",is(246))
             .body("width",is(283))
             .body("mimetype",is("image/png"))
          ;
    }
}