package com.malcolm.imagestorage;

import com.malcolm.imagestorage.orm.Image;
import com.malcolm.imagestorage.orm.ImageRepository;
import io.opentracing.References;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import io.opentracing.propagation.TextMapExtractAdapter;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.apache.commons.codec.binary.Hex;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.jms.*;
import javax.transaction.Transactional;
import org.hibernate.exception.ConstraintViolationException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
class ImageStorage implements Runnable {

    private static final Logger LOG = Logger.getLogger(ImageStorage.class);

    @SuppressWarnings("CdiInjectionPointsInspection")
    @Inject
    ConnectionFactory connectionFactory;

    @Inject
    ImageRepository imageRepository;

    @Inject
    Tracer tracer;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    void onStart(@Observes StartupEvent startUp) {
        scheduler.scheduleWithFixedDelay(this, 0L, 10L, TimeUnit.SECONDS);
    }

    void onStop(@Observes ShutdownEvent ev) {
        scheduler.shutdown();
    }

    @Override
    public void run() {
        LOG.debug("ImageStorage Processing ImageAPI Queue");
        try (
                JMSContext context = connectionFactory.createContext(Session.AUTO_ACKNOWLEDGE);
                JMSConsumer consumer = context.createConsumer(context.createQueue("imageapi"))
        ) {
           while(true){
               Message message = consumer.receive();
               if (message == null) {
                   return;
               }

               @SuppressWarnings("rawtypes")
               Map messagedata = message.getBody(Map.class);
               if(messagedata != null){
                   final Map<String,String> textMap = new HashMap<>();
                   Image image = new Image();

                   messagedata.forEach((key, value) -> {
                       if (key.toString().equalsIgnoreCase("image")) {
                           image.setImg((byte[]) value);
                       }else if(key.toString().equalsIgnoreCase("imagethumbnail")) {
                           image.setImgthumbnail((byte[]) value);
                       }else{
                           textMap.put(key.toString(),(String)value);
                       }
                       image.setDateadded(LocalDateTime.now());
                   });
                   final Span span = tracer.buildSpan("ImageStorage").addReference(References.FOLLOWS_FROM,tracer.extract(Format.Builtin.TEXT_MAP,new TextMapExtractAdapter(textMap))).start();
                   tracer.scopeManager().activate(span,true);
                   LOG.debug("ImageStorage Processing ImageAPI Queue Message");
                   saveImage(image);
                   LOG.debug("Done ImageStorage Processing ImageAPI Queue Message");
                   span.finish();
               }
           }
        }catch (Exception err){
            LOG.error("Error processing images from queue", err);
        }
    }

    @Timed(name = "ImageStorage-Persistence-Timer", description = "Measure Image Storage Persistence Performance.", unit = MetricUnits.MILLISECONDS)
    @Counted(name = "ImageStorage-Persistence-Counter", description = "Number of Image Persistence Performed.")
    @Metered(name = "ImageStorage-Persistence", unit = MetricUnits.MINUTES, description = "Number of Image Persistence.", absolute = true)
    @Transactional
    public void saveImage(Image image) throws NoSuchAlgorithmException {
        try{
            image.setImagehashid(Hex.encodeHexString(MessageDigest.getInstance("SHA3-256").digest(image.getImg())));
            LOG.debug("ImageStorage Calling saveImage ");
            imageRepository.persist(image);
            LOG.debug("ImageStorage Done Saving Image ");
        }catch (Exception err){
            if(err instanceof ConstraintViolationException){
                LOG.error("Skip Saving Image, Image Already Exists:", err);
                return;
            }
            throw err;
        }
    }
}