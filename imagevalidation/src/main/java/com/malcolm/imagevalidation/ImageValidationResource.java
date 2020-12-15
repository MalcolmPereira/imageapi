package com.malcolm.imagevalidation;

import com.malcolm.imagevalidation.domain.ImageMetadata;
import com.malcolm.imagevalidation.domain.ImageUpload;
import com.malcolm.imagevalidation.exception.ImageValidationException;
import com.malcolm.imagevalidation.exception.ImageValidationResourceError;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/imagevalidation")
public class ImageValidationResource {

    private static final Logger LOGGER = Logger.getLogger(ImageValidationResource.class);

    @Inject
    ImageValidationService imageValidationService;

    @POST
    @Path("/validate")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed(name   = "ImageValidation-validate-Timer",  displayName = "ImageValidation-validate-Timer",description = "Measure Image Validation Performance.", unit = MetricUnits.SECONDS)
    @Counted(name = "ImageValidation-validate-Counter",displayName = "ImageValidation-validate-Counter",description = "Number of Image Validations Performed.")
    @Metered(name = "ImageValidation-validate-Meter", displayName = "ImageValidation-validate-Meter", description = "Number of Image Validation.")
    public ImageMetadata validate(@MultipartForm ImageUpload imageUpload) throws ImageValidationException {
        LOGGER.debug("Image Validation called ImageUpload: "+ imageUpload);
        try {
            LOGGER.debug("Image Validation calling imageCheckService: ");
            return imageValidationService.validateImage(imageUpload.image);
        }catch(ImageValidationException imageErr){
            LOGGER.error("Invalid Validation Service Error",imageErr);
            throw imageErr;
        }catch(Exception err){
            LOGGER.error("Invalid Validation Server Error",err);
            throw new ImageValidationException(ImageValidationResourceError.SERVER_ERROR, err);
        }
    }
}
