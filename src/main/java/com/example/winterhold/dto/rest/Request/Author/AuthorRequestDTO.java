package com.example.winterhold.dto.rest.Request.Author;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class AuthorRequestDTO {

    private int page;

    private int dataCount;
}
