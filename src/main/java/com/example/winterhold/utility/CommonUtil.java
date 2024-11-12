package com.example.winterhold.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

public class CommonUtil {

    public static String generateTransactionId(String loanId, String customerId) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String date = dateFormat.format(new Date());

        return date.concat(loanId).concat(customerId);
    }

    public static String convertBirthdateIdn(LocalDate date) {
        String birthDate;
        try {
            Locale indo = new Locale("id", "ID");
            Date originalDate = new SimpleDateFormat("yyyy-MM-dd").parse(date.toString());
            birthDate = new SimpleDateFormat("dd MMMM yyyy", indo).format(originalDate);
        } catch (Exception e) {
            birthDate = "";
        }
        return birthDate;
    }
}
