package com.parentoff.rest.otp.resource;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pooja on 28/12/16.
 */
public class PhoneNumberUtil {

    private static String regexStr = "^[0-9]{10,15}$";
    private static Pattern pattern = Pattern.compile(regexStr);


    public static boolean isMobileNumber(String number){
        if(null != number){
            Matcher matcher = pattern.matcher(number);
            return matcher.matches();
        }else {
            return false;
        }

    }
}
