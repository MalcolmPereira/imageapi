package com.malcolm.imageapi;

import com.malcolm.imageapi.domain.ImageMetadata;
import com.malcolm.imageapi.domain.ImageUpload;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/validate")
@RegisterRestClient
public interface ImageValidationService {

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    ImageMetadata validate(@MultipartForm  ImageUpload imageUpload);
}
