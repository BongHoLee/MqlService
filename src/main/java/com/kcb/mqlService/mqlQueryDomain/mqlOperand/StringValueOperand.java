package com.kcb.mqlService.mqlQueryDomain.mqlOperand;

import com.kcb.mqlService.utils.DateFormatUtil;

import java.util.Date;

public class StringValueOperand implements ValueOperand {

    private String value;

    public StringValueOperand(String value) {
        this.value = value;
    }

    @Override
    public String getExpressionToString() {
        return value;
    }

    @Override
    public boolean equalTo(Object target) {
        if (target instanceof String) {
            return value.equals(target);
        } else {
            throw new RuntimeException("Not Matched Type!");
        }
    }

    @Override
    public boolean notEqualTo(Object target) {
        if (target instanceof String) {
            return !(value.equals(target));
        } else {
            throw new RuntimeException("Not Matched Type!");
        }

    }

    @Override
    public boolean lessThan(Object target) {
        if (target instanceof String) {
            if (DateFormatUtil.isDateFormat((String)target) && DateFormatUtil.isDateFormat(value)) {

            }
        }
        return false;
    }

    @Override
    public boolean largerThan(Object target) {
        return false;
    }

    @Override
    public boolean lessThanOrEqualTo(Object target) {
        return false;
    }

    @Override
    public boolean largerThanOrEqualTo(Object target) {
        return false;
    }


}
