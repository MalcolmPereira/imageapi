package com.malcolm.imagevalidation.exception;

import com.malcolm.imagevalidation.domain.StatusResponse;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ImageValidationResourceExceptionMapper implements ExceptionMapper<ImageValidationException> {

    @Override
    public Response toResponse(ImageValidationException err) {
        return Response.status(mapException(err)).entity(getStatusResponse(err)).type(MediaType.APPLICATION_JSON).build();
    }

    private StatusResponse getStatusResponse(ImageValidationException err){
        return new StatusResponse(mapException(err),err.getErrorType().toString(),err.getErrorType().getErrorMessage());
    }

    private int mapException(ImageValidationException err) {
        if (ImageValidationResourceError.INVALID_DATA == err.getErrorType()) {
            return Status.BAD_REQUEST.getStatusCode();
        }else if(ImageValidationResourceError.INVALID_FILETYPE == err.getErrorType()) {
            return Status.UNSUPPORTED_MEDIA_TYPE.getStatusCode();
        } else if (ImageValidationResourceError.SERVER_ERROR == err.getErrorType()) {
            return Status.INTERNAL_SERVER_ERROR.getStatusCode();
        } else {
            return Status.NOT_IMPLEMENTED.getStatusCode();
        }
    }
}
