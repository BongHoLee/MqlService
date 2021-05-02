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
        String date1 = "2020-01-01 23:10:00";
        String date11 = "2020-01-01 23:10:00";
        String date2 = "2020-01-02 23:00:00";
        String date22 = "2020-01-02 23:00:00";

        assertThat(true, equalTo(DateFormatUtil.lessThan(date1,date2)));
        assertThat(true, equalTo(DateFormatUtil.lessThanOrEqual(date1,date11)));
        assertThat(true, equalTo(DateFormatUtil.largerThan(date2, date1)));
        assertThat(true, equalTo(DateFormatUtil.largerThanOrEqual(date2, date22)));
    }
}
