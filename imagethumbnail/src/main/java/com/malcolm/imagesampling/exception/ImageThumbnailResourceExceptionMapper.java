package com.malcolm.imagesampling.exception;

import com.malcolm.imagesampling.domain.StatusResponse;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ImageThumbnailResourceExceptionMapper implements ExceptionMapper<ImageThumbnailException> {

    @Override
    public Response toResponse(ImageThumbnailException err) {
        return Response.status(mapException(err)).entity(getStatusResponse(err)).type(MediaType.APPLICATION_JSON).build();
    }

    private StatusResponse getStatusResponse(ImageThumbnailException err){
        return new StatusResponse(mapException(err),err.getErrorType().toString(),err.getErrorType().getErrorMessage());
    }

    private int mapException(ImageThumbnailException err) {
        if (ImageThumbnailResourceError.INVALID_DATA == err.getErrorType()) {
            return Status.BAD_REQUEST.getStatusCode();
        }else if (ImageThumbnailResourceError.INVALID_IMAGE_FILE == err.getErrorType()) {
            return Status.UNSUPPORTED_MEDIA_TYPE.getStatusCode();
        }else if (ImageThumbnailResourceError.INVALID_FILE_PROCESSING == err.getErrorType()) {
            return Status.INTERNAL_SERVER_ERROR.getStatusCode();
        } else if (ImageThumbnailResourceError.SERVER_ERROR == err.getErrorType()) {
            return Status.INTERNAL_SERVER_ERROR.getStatusCode();
        } else {
            return Status.NOT_IMPLEMENTED.getStatusCode();
        }
    }
}
