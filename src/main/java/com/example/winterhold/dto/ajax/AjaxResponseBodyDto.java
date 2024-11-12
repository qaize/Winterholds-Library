package com.example.winterhold.dto.ajax;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class AjaxResponseBodyDto {
    private Boolean success;
    private HttpStatus status;
    private String message;

    private List<ErrorValidationDto> listError;

    public AjaxResponseBodyDto(Boolean success, HttpStatus status, String message) {
        this.success = success;
        this.status = status;
        this.message = message;
    }
}
