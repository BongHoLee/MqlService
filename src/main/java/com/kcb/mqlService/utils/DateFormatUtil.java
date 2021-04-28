package com.kcb.mqlService.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateFormatUtil {
    private static final String[] formats = {
            "yyyy-MM-dd'T'HH:mm:ss'Z'",   "yyyy-MM-dd'T'HH:mm:ssZ",
            "yyyy-MM-dd'T'HH:mm:ss",      "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            "yyyy-MM-dd'T'HH:mm:ss.SSSZ", "yyyy-MM-dd HH:mm:ss",
            "MM/dd/yyyy HH:mm:ss",        "MM/dd/yyyy'T'HH:mm:ss.SSS'Z'",
            "MM/dd/yyyy'T'HH:mm:ss.SSSZ", "MM/dd/yyyy'T'HH:mm:ss.SSS",
            "MM/dd/yyyy'T'HH:mm:ssZ",     "MM/dd/yyyy'T'HH:mm:ss",
            "yyyy:MM:dd HH:mm:ss",        "yyyyMMdd"
    };

    public static boolean isDateFormat(String date)  {
        if (date != null) {
            for (String parse : formats) {
                SimpleDateFormat sdf = new SimpleDateFormat(parse);
                try {
                    sdf.parse(date);
                    return true;
                } catch (ParseException e) {

                }
            }
        }
        return false;
    }

    public static boolean lessThan(String date1, String date2, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        try {
            return sdf.parse(date1).before(sdf.parse(date2));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static String matchedPattern(String date1, String date2) {
        Map<String, Object> result = new HashMap<>();

        if (date1 != null && date2 != null) {
            for (String pattern : formats) {
                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                try {
                    sdf.parse(date1);
                    sdf.parse(date2);
                    return pattern;
                } catch (ParseException e) { }
            }
        }
        return null;
    }

    public static Date parse(String dateValue) {
        Date date = null;

        for (String pattern : formats) {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            try {
                date = sdf.parse(dateValue);
                break;
            } catch (ParseException e) { }
        }

        return date;
    }

}
