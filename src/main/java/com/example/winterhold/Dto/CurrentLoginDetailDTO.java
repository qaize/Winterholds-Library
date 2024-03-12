package com.example.winterhold.Dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class CurrentLoginDetailDTO {
    @Getter
    private String username;
    @Getter
    private String role;
}
