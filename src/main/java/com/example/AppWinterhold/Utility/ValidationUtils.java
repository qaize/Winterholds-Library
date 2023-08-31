package com.example.AppWinterhold.Utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class ValidationUtils {

    public static boolean isNumeric(String str){

        String regex = "";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }


}
