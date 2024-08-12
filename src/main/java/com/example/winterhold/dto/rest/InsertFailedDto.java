package com.example.winterhold.dto.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class InsertFailedDto {
    private HttpStatus status;
    private String message;

    private List<ValidatorRestDto> object;
}
