package com.example.winterhold.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum enumList {

    RUNTIME_ERROR_SERVER("Runtime Error On Server"),
    INTERNAL_SERVER_ERROR("Internal Server Error");

    private final String message;
}
