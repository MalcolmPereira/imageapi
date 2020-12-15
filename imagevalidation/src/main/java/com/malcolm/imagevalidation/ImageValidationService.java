package com.malcolm.imagevalidation;

import com.malcolm.imagevalidation.domain.ImageMetadata;
import com.malcolm.imagevalidation.domain.ImageMime;
import com.malcolm.imagevalidation.exception.ImageValidationException;
import com.malcolm.imagevalidation.exception.ImageValidationResourceError;
import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

@ApplicationScoped
public class ImageValidationService {

    private static final Logger LOG = Logger.getLogger(ImageValidationResource.class);

    public ImageMetadata validateImage(File image) throws ImageValidationException {
        LOG.debug("ImageCheckService Start image: "+ image);

        if (image == null) {
            LOG.warn("ImageCheckService Invalid Image: ");
            throw new ImageValidationException(ImageValidationResourceError.INVALID_DATA, new Exception("Invalid file upload data"));
        }

        try (BufferedInputStream buffStream = new BufferedInputStream(new FileInputStream(image))) {
            LOG.warn("ImageCheckService Start Apache Tika Validation");

            ImageInfo imageInfo = Imaging.getImageInfo(buffStream, "");

            //Use Apache Tika to get Image Mime Type
            LOG.debug("ImageCheckService Image mimeType: " + imageInfo.getMimeType());
            if (imageInfo.getMimeType() == null || imageInfo.getMimeType().isBlank() || ImageMime.fromValue(imageInfo.getMimeType()) == null) {
                LOG.warn("ImageCheckService Invalid Image mimeType: " + imageInfo.getMimeType());
                throw new ImageValidationException(ImageValidationResourceError.INVALID_FILETYPE, new Exception("Unsupported file mime type detected by Apache Tika"));
            }
            LOG.warn("ImageCheckService End Apache Tika Validation");

            ImageMetadata imageMetadata = new ImageMetadata(
                    imageInfo.getMimeType(),
                    image.length(),
                    imageInfo.getWidth(),
                    imageInfo.getHeight());
            LOG.warn("ImageCheckService Done ImageIO Validation");

            LOG.debug("ImageCheckService End: " + image);
            return imageMetadata;
        }catch(ImageReadException imageReadException){
            throw new ImageValidationException(ImageValidationResourceError.INVALID_FILETYPE, new Exception("Unsupported file mime type detected by Apache Tika"));
        }catch (ImageValidationException imageValidationErr) {
            throw imageValidationErr;
        } catch (Exception err) {
            throw new ImageValidationException(ImageValidationResourceError.INVALID_FILE_PROCESSING, err);
        }
    }
}
