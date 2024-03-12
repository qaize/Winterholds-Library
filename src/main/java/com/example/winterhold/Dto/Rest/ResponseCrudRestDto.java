package com.example.winterhold.Dto.Rest;

import lombok.*;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class ResponseCrudRestDto {
    private HttpStatus status;
    private String message;

    private Object object;

    public ResponseCrudRestDto (HttpStatus status,String message){
        this.status=status;
        this.message = message;
    }
}
