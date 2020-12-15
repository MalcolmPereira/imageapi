package com.malcolm.imagestorage;

import com.malcolm.imagestorage.domain.Image;
import com.malcolm.imagestorage.exception.ImageStorageException;
import com.malcolm.imagestorage.exception.ImageStorageResourceError;
import com.malcolm.imagestorage.orm.ImageRepository;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Path("/imagestorage")
public class ImageStorageResource {

    private static final Logger LOGGER = Logger.getLogger(ImageStorageResource.class);

    @Inject
    ImageRepository imageRepository;

    @GET
    @Path("/images")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed(name = "ImageStorage-ListImages-Timer", displayName = "ImageStorage-ListImages-Timer",description = "Measure ImageStorage List Performance.", unit = MetricUnits.SECONDS)
    @Counted(name = "ImageStorage-ListImages-Counter", displayName = "ImageStorage-ListImages-Counter",description = "Number of ImageStorage List Performed.")
    @Metered(name = "ImageStorage-Meter", displayName = "ImageStorage-Meter", description = "Number of ImageStorage List Performed.")
    public List<Image> listImages() throws ImageStorageException {
        LOGGER.debug("ImageStorage List Images Called: ");
        List<Image> images = new ArrayList<>();
        try{
            List<com.malcolm.imagestorage.orm.Image> imagesdatalist = imageRepository.listAll();
            System.out.println("imagesdatalist "+imagesdatalist.size());
            imagesdatalist.forEach(imagedata ->{
                Image image = new Image();
                image.setImageid(""+imagedata.getImageid());
                image.setImage(new String(Base64.getEncoder().encode(imagedata.getImgthumbnail())));
                image.setDateadded(""+imagedata.getDateadded());
                images.add(image);
            });
            return images;
        } catch (Exception err) {
            LOGGER.error("Invalid Validation Server Error", err);
            throw new ImageStorageException(ImageStorageResourceError.SERVER_ERROR, err);
        }
    }

    @GET
    @Path("/images/{imageid}")
    @Produces({MediaType.APPLICATION_OCTET_STREAM,MediaType.APPLICATION_JSON})
    @Timed(name = "ImageStorage-GetImage-Timer", displayName = "ImageStorage-GetImage-Timer", description = "Measure ImageStorage Get Image Performance.", unit = MetricUnits.SECONDS)
    @Counted(name = "ImageStorage-GetImage-Counter", displayName = "ImageStorage-GetImage-Counter", description = "Number of ImageStorage Get Image Performed.")
    @Metered(name = "ImageStorage-GetImage-Meter", displayName = "ImageStorage-GetImage-Meter", description = "Number of ImageStorage Get Images Performed.")
    public Response getImage(@PathParam("imageid") String imageid) throws ImageStorageException {
        LOGGER.debug("ImageStorage Get Image Called: ");
        try {

            final com.malcolm.imagestorage.orm.Image image = imageRepository.findById(Long.valueOf(imageid));

            if (image == null) {
                LOGGER.error("Image Not Found for imageid " + imageid);
                throw new ImageStorageException(ImageStorageResourceError.NOT_FOUND, new Exception("Image Not Found for imageid: " + imageid));
            }

            StreamingOutput imageStreamingOutput = output -> output.write(image.getImg());
            return Response.ok(imageStreamingOutput).build();
        }catch (ImageStorageException imageStorageException){
            throw imageStorageException;
        } catch (NumberFormatException numErr) {
            LOGGER.error("ImageStorage Validation Server Error", numErr);
            throw new ImageStorageException(ImageStorageResourceError.INVALID_DATA, numErr);
        } catch (Exception err) {
            LOGGER.error("ImageStorage Server Error", err);
            throw new ImageStorageException(ImageStorageResourceError.SERVER_ERROR, err);
        }
    }
}
