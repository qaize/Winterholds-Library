package com.example.winterhold.utility;


import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class CommonUtil {

    public static String generateTransactionId(String loanId, String customerId){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateNow = sdf.format(new Date());

        return loanId.concat(dateNow).concat(customerId);
    }
}
