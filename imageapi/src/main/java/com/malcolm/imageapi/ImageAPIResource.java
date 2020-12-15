package com.malcolm.imageapi;

import com.malcolm.imageapi.domain.Image;
import com.malcolm.imageapi.domain.ImageMetadata;
import com.malcolm.imageapi.domain.ImageUpload;
import com.malcolm.imageapi.exception.ImageCheckException;
import com.malcolm.imageapi.exception.ImageCheckResourceError;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@RegisterForReflection
@Path("/imageapi")
public class ImageAPIResource {

    private static final Logger LOGGER = Logger.getLogger(ImageAPIResource.class);

    @Inject
    @RestClient
    ImageValidationService validationService;

    @Inject
    @RestClient
    ImageThumbnailService thumbnailService;

    @Inject
    @RestClient
    ImageStorageService storageService;

    @POST
    @Path("/validate")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed(name = "ImageAPI-Validate-Timer", displayName = "ImageAPI-Validate-Timer", description = "Measure ImageAPI Validation Performance.", unit = MetricUnits.SECONDS)
    @Counted(name = "ImageAPI-Validate-Counter", displayName = "ImageAPI-Validate-Counter", description = "Number of ImageAPI Validations Performed.")
    @Metered(name = "ImageAPI-Validate-Meter",displayName = "ImageAPI-Validate-Meter", description = "Number of ImageAPI Validations Metered.")
    public ImageMetadata validate(@MultipartForm ImageUpload imageUpload) throws ImageCheckException {
        try {
            LOGGER.debug("ImageAPI Validation called ImageUpload: " + imageUpload);
            return validationService.validate(imageUpload);
        }catch(WebApplicationException err){
            LOGGER.error("ImageAPI Validation Service Error ",err);
            throw err;
        }catch(Exception err){
            LOGGER.error("ImageAPI Validation Error ",err);
            throw new ImageCheckException(ImageCheckResourceError.SERVER_ERROR,err);
        }
    }

    @POST
    @Path("/generate")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_OCTET_STREAM, "image/png"})
    @Timed(name = "ImageAPI-Generate-Timer", displayName = "ImageAPI-Generate-Timer",description = "Measure ImageAPI Generate Performance.", unit = MetricUnits.SECONDS)
    @Counted(name = "ImageAPI-Generate-Counter", displayName = "ImageAPI-Generate-Counter", description = "Number of ImageAPI Generate Performed.")
    @Metered(name = "ImageAPI-Generate-Meter", displayName = "ImageAPI-Generate-Meter",description = "Number of ImageAPI Generate Metered.")
    public Response generate(@MultipartForm ImageUpload imageUpload) throws ImageCheckException {
        try {
            LOGGER.debug("Image Validation called ImageUpload: " + imageUpload);
            validationService.validate(imageUpload);
            LOGGER.debug("Image Generation called ImageUpload: " + imageUpload);
            return thumbnailService.generate(imageUpload);
        }catch(WebApplicationException err){
            LOGGER.error("ImageAPI Generate Image Thumbnail Service Error ",err);
            throw err;
        }catch(Exception err){
            LOGGER.error("ImageAPI Generate Image Thumbnail  Error ",err);
            throw new ImageCheckException(ImageCheckResourceError.SERVER_ERROR,err);
        }
    }

    @GET
    @Path("/images")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed(name = "ImageAPI-ListImages-Timer", displayName = "ImageAPI-ListImages-Timer",description = "Measure ImageAPI List Images Performance.", unit = MetricUnits.SECONDS)
    @Counted(name = "ImageAPI-ListImages-Counter", displayName = "ImageAPI-ListImages-Counter",description = "Number of ImageAPI List Images Performed.")
    @Metered(name = "ImageAPI-ListImages-Meter", displayName = "ImageAPI-ListImages-Meter",description = "Number of ListImages Metered")
    public List<Image> listImages() throws ImageCheckException {
        try{
            LOGGER.debug("Image List called");
            return storageService.listImages();
        }catch(WebApplicationException err){
            LOGGER.error("ImageAPI List Images Service Error ",err);
            throw err;
        }catch(Exception err){
            LOGGER.error("ImageAPI List Images Error ",err);
            throw new ImageCheckException(ImageCheckResourceError.SERVER_ERROR,err);
        }
    }

    @GET
    @Path("/images/{imageid}")
    @Produces({MediaType.APPLICATION_OCTET_STREAM,MediaType.APPLICATION_JSON})
    @Timed(name = "ImageAPI-GetImage-Timer", displayName = "ImageAPI-GetImage-Timer",description = "Measure ImageAPI Get Image Performance.", unit = MetricUnits.SECONDS)
    @Counted(name = "ImageAPI-GetImage-Counter",displayName = "ImageAPI-GetImage-Counter",description = "Number of ImageAPI Get Image Performed.")
    @Metered(name = "ImageAPI-GetImage-Meter", displayName = "ImageAPI-GetImage-Meter",description = "Number of Get Image Metered.")
    public Response getImage(@PathParam("imageid") String imageid) throws ImageCheckException {
        try{
            LOGGER.debug("Get Image called");
            return storageService.getImage(imageid);
        }catch(WebApplicationException err){
            LOGGER.error("ImageAPI Get Image By ID Service Error ",err);
            throw err;
        }catch(Exception err){
            LOGGER.error("ImageAPI Get Image By ID Error ",err);
            throw new ImageCheckException(ImageCheckResourceError.SERVER_ERROR,err);
        }
    }
}