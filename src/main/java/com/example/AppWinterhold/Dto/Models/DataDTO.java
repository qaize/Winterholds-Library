package com.example.AppWinterhold.Dto.Models;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class DataDTO<T> {

    private T data;
    private int flag;
    private Long totalPage;
    private String message;

}
