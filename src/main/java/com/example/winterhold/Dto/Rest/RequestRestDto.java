package com.example.winterhold.Dto.Rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class RequestRestDto {
    private String subject;
    private String secretKey;
    private String audience;
    private String username;
    private String password;

}
