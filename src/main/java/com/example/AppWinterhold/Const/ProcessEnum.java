package com.example.AppWinterhold.Const;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ProcessEnum {
    SUCCESS("Success"),
    FAILED("Failed"),
    AUTHOR("Author"),
    CATEGORY("Category"),
    BOOK("Book"),
    LOAN("Loan"),
    CUSTOMER("Customer");

    private String message;
}

