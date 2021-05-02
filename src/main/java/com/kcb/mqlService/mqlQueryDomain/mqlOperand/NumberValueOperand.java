package com.kcb.mqlService.mqlQueryDomain.mqlOperand;

import java.math.BigDecimal;

public class NumberValueOperand implements ValueOperand {

    private BigDecimal value;

    public NumberValueOperand(String value) {
        this.value = new BigDecimal(value);
    }

    public NumberValueOperand(Number value) {
        this.value = new BigDecimal(String.valueOf(value));
    }

    @Override
    public String getExpressionToString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equalTo(Object target) {
        BigDecimal bdTarget = convertToBigDecimal(target);
        return value.compareTo(bdTarget) == 0;
    }

    @Override
    public boolean notEqualTo(Object target) {
        BigDecimal bdTarget = convertToBigDecimal(target);
        return value.compareTo(bdTarget) != 0;
    }

    @Override
    public boolean lessThan(Object target) {
        BigDecimal bdTarget = convertToBigDecimal(target);
        return value.compareTo(bdTarget) < 0;
    }

    @Override
    public boolean largerThan(Object target) {
        BigDecimal bdTarget = convertToBigDecimal(target);
        return value.compareTo(bdTarget) > 0;
    }

    @Override
    public boolean lessThanOrEqualTo(Object target) {
        BigDecimal bdTarget = convertToBigDecimal(target);
        return value.compareTo(bdTarget) <= 0;
    }

    @Override
    public boolean largerThanOrEqualTo(Object target) {
        BigDecimal bdTarget = convertToBigDecimal(target);
        return value.compareTo(bdTarget) >= 0;
    }

    private BigDecimal convertToBigDecimal(Object target) {
        if (target instanceof Number) {
            return new BigDecimal(String.valueOf(target));
        } else if (target instanceof NumberValueOperand) {
            return new BigDecimal(((NumberValueOperand) target).getExpressionToString());
        } else {
            throw new RuntimeException("Not Matched Type!");
        }
    }


}
