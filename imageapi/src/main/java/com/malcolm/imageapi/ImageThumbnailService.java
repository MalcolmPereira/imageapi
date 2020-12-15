package com.malcolm.imageapi;

import com.malcolm.imageapi.domain.ImageUpload;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/generate")
@RegisterRestClient
public interface ImageThumbnailService {

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_OCTET_STREAM,"image/png"})
    Response generate(@MultipartForm ImageUpload imageUpload);
}
