package com.example.winterhold.utility;

import com.example.winterhold.dto.BaseResponseDTO;
import org.springframework.http.HttpStatus;

public class ResponseUtil {


    private ResponseUtil(){}


    public static BaseResponseDTO<Object> insertSuccessResponse(Object data){

        return BaseResponseDTO.<Object>builder()
                .status("0")
                .message("Success")
                .httpStatus(HttpStatus.OK)
                .data(data)
                .build();
    }
    public static BaseResponseDTO<Object> insertSuccessResponseWithMetaData(Object data, BaseResponseDTO.MetaData metaData){

        return BaseResponseDTO.<Object>builder()
                .status("0")
                .message("Success")
                .httpStatus(HttpStatus.OK)
                .data(data)
                .metaData(metaData)
                .build();
    }


    public static BaseResponseDTO<Object> insertFailResponse(String message){

        return BaseResponseDTO.<Object>builder()
                .status("1")
                .message(message)
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .data(null)
                .build();
    }

    public static BaseResponseDTO<Object> insertFailResponse(String message, String reason){

        return BaseResponseDTO.<Object>builder()
                .status("1")
                .message("OK")
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .data(message.concat(reason))
                .build();
    }
}
