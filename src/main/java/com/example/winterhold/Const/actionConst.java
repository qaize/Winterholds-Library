package com.example.winterhold.Const;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class actionConst {

    final public static String INSERT = " insert";
    final public static String UPDATE = " update";
    final public static String DELETE = " delete";
    final public static String CATEGORY = " Category";
    final public static String AUTHOR = " Author";
    final public static String BOOK = " Book";
    final public static String LOAN = " Loan";
    final public static String CUSTOMER = " Customer";
    final public static String SUCCESS = " Success";
    final public static String FAILED = " Failed";
    final public static String ALREADY = " Already";
    final public static String PAY = " Acquittance";
    final public static String EXTEND = " Extend";
    final public static String BAN = " BAN";
    final public static String EMPTY_STRING = "";

    //    STATUS

    final public static String SUCCESS_GET_DATA = "Success getting data : {}";
    final public static String SUCCESS_INSERT_DATA = "Success insert data : {}";
    final public static String SUCCESS_UPDATE_DATA = "Success update data : {}";
    final public static String SUCCESS_DELETE_DATA = "Success delete data : {}";
    final public static String FAILED_INSERT_DATA = "Failed insert data : {}";
    final public static String FAILED_UPDATE_DATA = "Failed update data : {}";
    final public static String FAILED_GET_DATA = "Failed getting data : {}";

    //    Messages
    final public static String INDEX_EMPTY = "Data not found";
    final public static String VALIDATE_UPDATE = "Data was validated and there is no update on it.";



}
