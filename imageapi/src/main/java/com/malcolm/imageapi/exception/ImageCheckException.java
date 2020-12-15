package com.malcolm.imageapi.exception;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class ImageCheckException extends Exception {

    private final ImageCheckResourceError errorType;

    @SuppressWarnings("CdiInjectionPointsInspection")
    public ImageCheckException(ImageCheckResourceError errorType, Exception error){
        super(error.getMessage(),error);
        this.errorType = errorType;
    }

    public ImageCheckResourceError getErrorType(){
        return this.errorType;
    }
}

