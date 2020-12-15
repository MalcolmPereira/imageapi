package com.malcolm.imagevalidation.exception;

public class ImageValidationException extends Exception {

    private final ImageValidationResourceError errorType;

    @SuppressWarnings("CdiInjectionPointsInspection")
    public ImageValidationException(ImageValidationResourceError errorType, Exception error){
        super(error.getMessage(),error);
        this.errorType = errorType;
    }

    public ImageValidationResourceError getErrorType(){
        return this.errorType;
    }
}

