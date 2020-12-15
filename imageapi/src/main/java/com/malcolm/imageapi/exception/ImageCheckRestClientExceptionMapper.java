package com.malcolm.imageapi.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.malcolm.imageapi.domain.StatusResponse;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.apache.commons.io.IOUtils;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;

@RegisterForReflection
@Provider
public class ImageCheckRestClientExceptionMapper implements ExceptionMapper<WebApplicationException> {

    final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Response toResponse(WebApplicationException err) {
        StatusResponse statusResponse;
        try {
            statusResponse = objectMapper.readValue(new String(IOUtils.toByteArray((InputStream) err.getResponse().getEntity())), StatusResponse.class);
        } catch (IOException ioErr) {
            statusResponse = new StatusResponse(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), ImageCheckResourceError.SERVICE_ERROR.toString(), ImageCheckResourceError.SERVICE_ERROR.getErrorMessage());
        }
        return Response.status(statusResponse.getStatusCode()).entity(statusResponse).type(MediaType.APPLICATION_JSON).build();
    }
}
