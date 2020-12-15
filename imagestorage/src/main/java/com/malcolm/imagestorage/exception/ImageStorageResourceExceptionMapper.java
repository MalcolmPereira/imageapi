package com.malcolm.imagestorage.exception;

import com.malcolm.imagestorage.domain.StatusResponse;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ImageStorageResourceExceptionMapper implements ExceptionMapper<ImageStorageException> {

    @Override
    public Response toResponse(ImageStorageException err) {
        return Response.status(mapException(err)).entity(getStatusResponse(err)).type(MediaType.APPLICATION_JSON).build();
    }

    private StatusResponse getStatusResponse(ImageStorageException err){
        return new StatusResponse(mapException(err),err.getErrorType().toString(),err.getErrorType().getErrorMessage());
    }

    private int mapException(ImageStorageException err) {
        if (ImageStorageResourceError.INVALID_DATA == err.getErrorType()) {
            return Response.Status.BAD_REQUEST.getStatusCode();
        } else if (ImageStorageResourceError.NOT_FOUND == err.getErrorType()) {
            return Response.Status.NOT_FOUND.getStatusCode();
        } else if (ImageStorageResourceError.SERVER_ERROR == err.getErrorType()) {
            return Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        } else {
            return Response.Status.NOT_IMPLEMENTED.getStatusCode();
        }
    }
}
