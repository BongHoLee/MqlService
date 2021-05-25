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

    public static boolean lessThan(String date1, String date2) {
        return (compareTo(date1, date2) < 0);
    }

    public static boolean largerThan(String date1, String date2) {
        return (compareTo(date1, date2) > 0);
    }

    public static boolean equalTo(String date1, String date2) {
        return (compareTo(date1, date2) == 0);
    }

    public static boolean lessThanOrEqual(String date1, String date2) {
        return lessThan(date1, date2) || equalTo(date1, date2);
    }

    public static boolean largerThanOrEqual(String date1, String date2) {
        return largerThan(date1, date2) || equalTo(date1, date2);
    }

    public static String getPattern(String date) {
        if (date != null) {
            for (String parse : formats) {
                SimpleDateFormat sdf = new SimpleDateFormat(parse);
                try {
                    sdf.parse(date);
                    return parse;
                } catch (ParseException e) {

                }
            }
        }
        return null;
    }

    private static int compareTo(String date1, String date2) {
        SimpleDateFormat sdf = new SimpleDateFormat(getPattern(date1));

        try {
            return sdf.parse(date1).compareTo(sdf.parse(date2));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return -999;
    }

}
