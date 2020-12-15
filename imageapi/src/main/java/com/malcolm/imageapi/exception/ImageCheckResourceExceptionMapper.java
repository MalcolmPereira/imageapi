package com.malcolm.imageapi.exception;

import com.malcolm.imageapi.domain.StatusResponse;
import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@RegisterForReflection
@Provider
public class ImageCheckResourceExceptionMapper implements ExceptionMapper<ImageCheckException> {

    @Override
    public Response toResponse(ImageCheckException err) {
        return Response.status(mapException(err)).entity(getStatusResponse(err)).type(MediaType.APPLICATION_JSON).build();
    }

    private StatusResponse getStatusResponse(ImageCheckException err){
        return  new StatusResponse(mapException(err),err.getErrorType().toString(),err.getErrorType().getErrorMessage());
    }

    private int mapException(ImageCheckException err) {
        if (ImageCheckResourceError.SERVER_ERROR == err.getErrorType()) {
            return Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        } else {
            return Response.Status.NOT_IMPLEMENTED.getStatusCode();
        }
    }
}
