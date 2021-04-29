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
        } else if (target instanceof StringValueOperand) {
            return equalTo(((StringValueOperand) target).getExpressionToString());
        } else {
            throw new RuntimeException("Not Matched Type!");
        }
    }

    @Override
    public boolean notEqualTo(Object target) {
        if (target instanceof String) {
            return !(value.equals(target));
        } else if (target instanceof StringValueOperand) {
            return notEqualTo(((StringValueOperand) target).getExpressionToString());
        } else {
            throw new RuntimeException("Not Matched Type!");
        }

    }

    @Override
    public boolean lessThan(Object target) {
        if (target instanceof String) {
            if (DateFormatUtil.isDateFormat((String)target) && DateFormatUtil.isDateFormat(value)) {
                return DateFormatUtil.lessThan(value, (String)target);
            }
            else {
                return value.compareTo((String)target) < 0;
            }
        }  else if (target instanceof StringValueOperand) {
            return lessThan(((StringValueOperand) target).getExpressionToString());
        }else {
            throw new RuntimeException("Not Matched Type!");
        }

    }

    @Override
    public boolean largerThan(Object target) {
        if (target instanceof String) {
            if (DateFormatUtil.isDateFormat((String)target) && DateFormatUtil.isDateFormat(value)) {
                return DateFormatUtil.largerThan(value, (String)target);
            } else {
                return value.compareTo((String)target) > 0;
            }
        } else if (target instanceof StringValueOperand){
            return largerThan(((StringValueOperand) target).getExpressionToString());
        } else {
            throw new RuntimeException("Not Matched Type!");
        }
    }

    @Override
    public boolean lessThanOrEqualTo(Object target) {
        if (target instanceof String) {
            if (DateFormatUtil.isDateFormat((String)target) && DateFormatUtil.isDateFormat(value)) {
                return DateFormatUtil.lessThanOrEqual(value, (String)target);
            }  else {
                return value.compareTo((String)target) <= 0;
            }
        } else if(target instanceof StringValueOperand) {
            return lessThanOrEqualTo(((StringValueOperand) target).getExpressionToString());
        } else {
            throw new RuntimeException("Not Matched Type!");
        }
    }

    @Override
    public boolean largerThanOrEqualTo(Object target) {
        if (target instanceof String) {
            if (DateFormatUtil.isDateFormat((String)target) && DateFormatUtil.isDateFormat(value)) {
                return DateFormatUtil.largerThanOrEqual(value, (String)target);
            } else {
                return value.compareTo((String)target) >= 0;
            }
        } else if (target instanceof StringValueOperand){
            return largerThanOrEqualTo(((StringValueOperand) target).getExpressionToString());
        } else {
            throw new RuntimeException("Not Matched Type!");
        }
    }


}
