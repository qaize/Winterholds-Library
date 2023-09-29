package com.example.AppWinterhold.CustomException;

public class FileUploadSizeLimitExceededException extends RuntimeException{
    public FileUploadSizeLimitExceededException (String message){
        super(message);
    }
}
