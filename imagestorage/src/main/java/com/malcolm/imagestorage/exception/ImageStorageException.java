package com.malcolm.imagestorage.exception;

public class ImageStorageException extends Exception {
    private final ImageStorageResourceError errorType;

    @SuppressWarnings("CdiInjectionPointsInspection")
    public ImageStorageException(ImageStorageResourceError errorType, Exception error){
        super(error.getMessage(),error);
        this.errorType = errorType;
    }

    public ImageStorageResourceError getErrorType(){
        return this.errorType;
    }
}
