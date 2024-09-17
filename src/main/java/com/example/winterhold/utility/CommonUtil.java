package com.example.winterhold.utility;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtil {

    public static String generateTransactionId(String loanId,String customerId){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String date = dateFormat.format(new Date());

        return date.concat(loanId).concat(customerId);
    }
}
