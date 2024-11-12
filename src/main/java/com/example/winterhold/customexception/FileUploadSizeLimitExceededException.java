package com.example.winterhold.customexception;

public class FileUploadSizeLimitExceededException extends RuntimeException{
    public FileUploadSizeLimitExceededException (String message){
        super(message);
    }
}
