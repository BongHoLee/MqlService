package com.kcb.mqlService.utils;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class DateFormatUtilTest {

    @Test
    public void dateFormatTest() {
        String target =  "2020-01-02 23:00:00";

        assertThat(true, equalTo(DateFormatUtil.isDateFormat(target)));
    }

    @Test
    public void dateFormatCompareTest() {
        String s1 = "2020-01-01 23:00:00";
        String s2 = "2020-01-02 23:00:00";
        String s3 = "2020-01-02";

        LocalDateTime date1 = LocalDateTime.parse(s1);
        LocalDateTime date2 = LocalDateTime.parse(s2);
        LocalDateTime date3 = LocalDateTime.parse(s3);

        System.out.println(date1.compareTo(date2));
        System.out.println(date1.compareTo(date3));
        
    }
}
