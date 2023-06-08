package com.example.AppWinterhold.Dto.Rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class ValidatorRestDto {

    private String propertyName;
    private String messageError;
}
