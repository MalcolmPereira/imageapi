package com.malcolm.imagesampling.exception;

public class ImageThumbnailException extends Exception {

    private final ImageThumbnailResourceError errorType;

    @SuppressWarnings("CdiInjectionPointsInspection")
    public ImageThumbnailException(ImageThumbnailResourceError errorType, Exception error){
        super(error.getMessage(),error);
        this.errorType = errorType;
    }

    public ImageThumbnailResourceError getErrorType(){
        return this.errorType;
    }
}

