package com.example.winterhold.Dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
public class BaseResponseDTO<T> {

    private String status;
    private String message;
    private HttpStatus httpStatus;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private T data;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private MetaData metaData;

    @Builder
    public BaseResponseDTO(String status,String message,HttpStatus httpStatus,T data, MetaData metaData) {
        this.status = status;
        this.message = message;
        this.httpStatus = httpStatus;
        this.data = data;
        this.metaData = metaData;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MetaData {
        private long Total;
        private Integer totalPage;
        private Integer size;
    }




}
