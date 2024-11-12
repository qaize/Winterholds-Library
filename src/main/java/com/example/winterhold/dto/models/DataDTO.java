package com.example.winterhold.dto.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class DataDTO<T> {

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    private int flag;

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    private String message;

    private T data;
    private Long totalPage;
}
