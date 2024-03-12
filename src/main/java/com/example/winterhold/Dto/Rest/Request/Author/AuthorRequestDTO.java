package com.example.winterhold.Dto.Rest.Request.Author;

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
