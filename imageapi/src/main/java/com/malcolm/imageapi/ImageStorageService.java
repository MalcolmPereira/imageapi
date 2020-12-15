package com.malcolm.imageapi;

import com.malcolm.imageapi.domain.Image;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@RegisterRestClient
public interface ImageStorageService {

    @GET
    @Path("/images")
    @Produces(MediaType.APPLICATION_JSON)
    List<Image> listImages();

    @GET
    @Path("/images/{imageid}")
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_OCTET_STREAM,"image/png"})
    Response getImage(@PathParam("imageid") String imageid);
}
