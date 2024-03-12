package com.example.winterhold.Dto.Rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class ResponseRestDto {

    /*Token*/

    private String username;
    private String token;
}
