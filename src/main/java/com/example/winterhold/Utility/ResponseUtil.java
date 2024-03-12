package com.example.winterhold.Utility;

import com.example.winterhold.Dto.BaseResponseDTO;
import org.springframework.http.HttpStatus;

public class ResponseUtil {

    public static BaseResponseDTO<Object> insertSuccessResponse(Object data){

        return BaseResponseDTO.<Object>builder()
                .status("1")
                .message("Success")
                .httpStatus(HttpStatus.OK)
                .data(data)
                .build();
    }
    public static BaseResponseDTO<Object> insertSuccessResponseWithMetaData(Object data, BaseResponseDTO.MetaData metaData){

        return BaseResponseDTO.<Object>builder()
                .status("1")
                .message("Success")
                .httpStatus(HttpStatus.OK)
                .data(data)
                .metaData(metaData)
                .build();
    }


    public static BaseResponseDTO<Object> insertFailResponse(String message){

        return BaseResponseDTO.<Object>builder()
                .status("0")
                .message("OK")
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .data(message)
                .build();
    }

    public static BaseResponseDTO<Object> insertFailResponse(String message, String reason){

        return BaseResponseDTO.<Object>builder()
                .status("0")
                .message("OK")
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .data(message.concat(reason))
                .build();
    }
}
