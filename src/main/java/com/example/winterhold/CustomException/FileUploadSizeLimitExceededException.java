package com.example.winterhold.CustomException;

public class FileUploadSizeLimitExceededException extends RuntimeException{
    public FileUploadSizeLimitExceededException (String message){
        super(message);
    }
}
