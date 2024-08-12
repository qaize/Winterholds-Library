package com.example.winterhold.dto.ajax;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class ErrorValidationDto {
    private String propertyName;
    private String errorMessage;

}
