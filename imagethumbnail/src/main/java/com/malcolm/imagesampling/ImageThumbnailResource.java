package com.malcolm.imagesampling;

import com.malcolm.imagesampling.domain.ImageUpload;
import com.malcolm.imagesampling.exception.ImageThumbnailException;
import com.malcolm.imagesampling.exception.ImageThumbnailResourceError;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import io.opentracing.propagation.TextMap;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Path("/imagesampling")
public class ImageThumbnailResource {

    private static final Logger LOGGER = Logger.getLogger(ImageThumbnailResource.class);

    private static final String PNG = "png";

    @Inject
    ImageThumbnailService imageCheckService;

    @Inject
    Tracer tracer;

    @POST
    @Path("/generate")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces({MediaType.APPLICATION_OCTET_STREAM,MediaType.APPLICATION_JSON})
    @Timed(name = "ImageThumbnail-Generate-Timer", displayName = "ImageThumbnail-Generate-Timer", description = "Measure Image Generate Performance.", unit = MetricUnits.SECONDS)
    @Counted(name = "ImageThumbnail-Generate-Counter", displayName = "ImageThumbnail-Generate-Counter", description = "Number of Image Generate Performed.")
    @Metered(name = "ImageThumbnail-Generate-Meter", displayName = "ImageThumbnail-Generate-Meter",description = "Number of Image Generate.")
    public Response generate(@MultipartForm ImageUpload imageUpload) throws ImageThumbnailException {
        LOGGER.debug("Image Sampling Called ImageUpload: " + imageUpload);
        try {
            LOGGER.debug("Image Sampling Creating Tracer: ");
            final Map<String,String> textMap = new HashMap<>();
            tracer.inject(tracer.activeSpan().context(), Format.Builtin.TEXT_MAP, new TextMap() {
                @Override
                public Iterator<Map.Entry<String,String>> iterator() {
                    throw new UnsupportedOperationException("iterator not supported with Tracer.inject()");
                }
                @Override
                public void put(final String key, final String value) {
                    textMap.put(key, value);
                }
            });
            LOGGER.debug("Image Sampling Done Creating Tracer: ");

            LOGGER.debug("Image Sampling Calling Image Sampling Service: ");
            final BufferedImage scaledImage = imageCheckService.generate(imageUpload,textMap);
            LOGGER.debug("Image Sampling Done Calling Image Sampling Service: ");

            LOGGER.debug("Image Sampling Creating Streaming Output: ");
            return Response.ok(new StreamingSamplingOutput(scaledImage)).build();
        } catch (ImageThumbnailException imageErr) {
            LOGGER.error("ImageThumbnail Service Error", imageErr);
            throw imageErr;
        }catch(NumberFormatException numErr){
            LOGGER.error("ImageThumbnail Server Error", numErr);
            throw new ImageThumbnailException(ImageThumbnailResourceError.INVALID_DATA, numErr);
        } catch (Exception err) {
            LOGGER.error("ImageThumbnail Server Error", err);
            throw new ImageThumbnailException(ImageThumbnailResourceError.SERVER_ERROR, err);
        }
    }

    class StreamingSamplingOutput implements StreamingOutput {

        private BufferedImage sampledImage;

        public StreamingSamplingOutput(BufferedImage sampledImage) {
            this.sampledImage = sampledImage;
        }

        @Override
        public void write(OutputStream outputStream) throws IOException, WebApplicationException {
            try {
                ImageIO.write(this.sampledImage,PNG,outputStream);
                outputStream.flush();
                outputStream.close();
            } catch (Exception err) {
                LOGGER.error("ImageThumbnail Streaming OutPut Error", err);
                throw new IOException("ImageThumbnail Streaming OutPut Error", err);
            }
        }
    }
}