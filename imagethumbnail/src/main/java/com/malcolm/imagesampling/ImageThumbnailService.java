package com.malcolm.imagesampling;

import com.malcolm.imagesampling.domain.ImageScaleSampling;
import com.malcolm.imagesampling.domain.ImageUpload;
import com.malcolm.imagesampling.exception.ImageThumbnailException;
import com.malcolm.imagesampling.exception.ImageThumbnailResourceError;
import org.apache.commons.io.FileUtils;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Session;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class ImageThumbnailService {

    private static final Logger LOGGER = Logger.getLogger(ImageThumbnailResource.class);

    private static final int DEFAULT_THUMBNAIL = 75;

    private static final double DEFAULT_SCALING = 1.0;

    private static final String PNG = "png";


    @SuppressWarnings("CdiInjectionPointsInspection")
    @Inject
    ConnectionFactory connectionFactory;


    @SuppressWarnings("Unchecked cast")
    public BufferedImage generate(ImageUpload imageUpload, Map<String,String> traceMap) throws ImageThumbnailException {
        if (imageUpload == null || imageUpload.getImage() == null) {
            LOGGER.warn("ImageThumbnailService Invalid Image: ");
            throw new ImageThumbnailException(ImageThumbnailResourceError.INVALID_DATA, new Exception("Invalid file upload data"));
        }
        LOGGER.debug("Got Image Scale "+imageUpload.getScale());
        LOGGER.debug("Got Image Width "+imageUpload.getWidth());
        LOGGER.debug("Got Image Height "+imageUpload.getHeight());
        LOGGER.debug("Got Image Scale Type "+imageUpload.getScaletype());

        try(
             FileInputStream fis = new FileInputStream(imageUpload.getImage());
             BufferedInputStream buffStream = new BufferedInputStream(fis)
            )
        {
            BufferedImage img = ImageIO.read(buffStream);
            ImageScaleSampling scaleType = ImageScaleSampling.BICUBIC;
            double scalex = DEFAULT_SCALING;
            double scaley = DEFAULT_SCALING;
            if(imageUpload.getScaletype() !=null && !imageUpload.getScaletype().isEmpty()
                    && !imageUpload.getScaletype().isBlank() && ImageScaleSampling.fromValue(imageUpload.getScaletype()) != null){
                scaleType = ImageScaleSampling.fromValue(imageUpload.getScaletype());
            }
            if(imageUpload.getScale() != null && !imageUpload.getScale().isBlank() && !imageUpload.getScale().isEmpty()){
                try{
                    scalex = Double.parseDouble(imageUpload.getScale());
                    scaley = Double.parseDouble(imageUpload.getScale());
                }catch(Exception err){
                    LOGGER.warn("Invalid Scale Value, defaulting to 1.0", err);
                }
            }
            LOGGER.debug("Got Image scalex "+scalex);
            LOGGER.debug("Got Image scaley "+scaley);


            int newWidth  = 0;
            int newHeight = 0;
            if(imageUpload.getWidth() != null && !imageUpload.getWidth().isBlank() && !imageUpload.getWidth().isEmpty()) {
                try{
                    newWidth = Integer.parseInt(imageUpload.getWidth());
                    LOGGER.debug("Got Image newWidth "+newWidth);
                }catch(Exception err){
                    LOGGER.warn("Invalid Width Value, defaulting to 0", err);
                }
            }

            if(imageUpload.getHeight() != null && !imageUpload.getHeight().isBlank() && !imageUpload.getHeight().isEmpty()) {
                try{
                    newHeight = Integer.parseInt(imageUpload.getHeight());
                    LOGGER.debug("Got Image newHeight "+newHeight);
                }catch(Exception err){
                    LOGGER.warn("Invalid Height Value, defaulting to 0", err);
                }
            }

            if(newWidth > 0){
                scalex = Math.round( (Double.parseDouble(newWidth+".00") / Double.parseDouble(img.getWidth()+".00")) * 100.0) / 100.0;
                LOGGER.debug("Got Image scalex from newWidth "+scalex);
            }

            if(newHeight > 0){
                scaley = Math.round( (Double.parseDouble(newHeight+".00") / Double.parseDouble(img.getHeight()+".00")) * 100.0) / 100.0;
                LOGGER.debug("Got Image scaley from newHeight "+scaley);
            }

            BufferedImage scaledImage = scaleImage(img, scalex, scaley, scaleType);

            final byte[] imagebytes  = FileUtils.readFileToByteArray(imageUpload.getImage());
            new Thread(() -> {
                try(ByteArrayOutputStream os = new ByteArrayOutputStream()){
                    LOGGER.debug("ImageThumbnailService Starting Send Images to Queue Thread: ");
                    BufferedImage imageThumbnail = scaleImage(img, Math.round( (Double.parseDouble(DEFAULT_THUMBNAIL+".00") / Double.parseDouble(img.getWidth()+".00")) * 100.0) / 100.0 , Math.round( (Double.parseDouble(DEFAULT_THUMBNAIL+".00") / Double.parseDouble(img.getHeight()+".00")) * 100.0) / 100.0,ImageScaleSampling.BICUBIC);
                    ImageIO.write(imageThumbnail,PNG,os);
                    sendImagesToQueue(imagebytes,os,traceMap);
                } catch (Exception err) {
                    LOGGER.error("ImageThumbnailService Error Sending Image Details to Queue: ", err);
                }
            }).start();


            return scaledImage;
        }catch(Exception err){
            throw new ImageThumbnailException(ImageThumbnailResourceError.INVALID_FILE_PROCESSING, err);
        }
    }

    private BufferedImage scaleImage(BufferedImage img, double scalex, double scaley, ImageScaleSampling scaleType) {
        LOGGER.debug("In scaleImage scalex: "+scalex);
        LOGGER.debug("In scaleImage scaley: "+scaley);
        LOGGER.debug("In scaleImage scaleType: "+scaleType);
        LOGGER.debug("In scaleImage creating new image of height: "+((int)(img.getHeight() * scaley)));

        int newImgWidth = (int)(img.getWidth() * scalex);
        LOGGER.debug("In scaleImage creating new image of width: "+newImgWidth);
        if(newImgWidth == 0){
           newImgWidth = 1;
           LOGGER.debug("In scaleImage, width is 0, creating new image of width: "+newImgWidth);
        }

        int newImgHeight = (int)(img.getHeight() * scaley);
        if(newImgHeight == 0){
            newImgHeight = 1;
            LOGGER.debug("In scaleImage, height is 0, creating new image of height: "+newImgHeight);
        }

        BufferedImage newImg = new BufferedImage(newImgWidth , newImgHeight, BufferedImage.TYPE_INT_ARGB);
        LOGGER.debug("In scaleImage Got New Image Buffer Width: "+newImg.getWidth());
        LOGGER.debug("In scaleImage Got New Image Buffer Height: "+newImg.getHeight());
        LOGGER.debug("In  scaleImage AffineTransform Got scalex: "+scalex);
        LOGGER.debug("In  scaleImage AffineTransform Got scaley: "+scaley);
        final AffineTransform at = AffineTransform.getScaleInstance(scalex, scaley);
        AffineTransformOp ato;
        if(ImageScaleSampling.BICUBIC  == scaleType){
            ato = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
        }else if(ImageScaleSampling.BILINEAR  == scaleType){
            ato = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        }else if(ImageScaleSampling.NEAREST_NEIGHBOR  == scaleType){
            ato = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        }else{
            ato = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
        }
        return ato.filter(img, newImg);
    }

    private void sendImagesToQueue(byte[] image,ByteArrayOutputStream thumbnail, Map<String,String> textMap) {
        Map<String,Object> imageDataMap = new HashMap<>();
        imageDataMap.put("image",image);
        imageDataMap.put("imagethumbnail",thumbnail.toByteArray());
        textMap.forEach(imageDataMap::put);
        try (JMSContext context = connectionFactory.createContext(Session.AUTO_ACKNOWLEDGE)) {
            context
                    .createProducer()
                    .send(context.createQueue("imageapi"),imageDataMap);

        }catch (Exception err){
            LOGGER.error("Error send images to queue", err);
        }
    }
}
