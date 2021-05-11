package com.kcb.mqlService.mqlQueryDomain.mqlOperand;

public interface  ValueOperand extends MQLOperand {
    boolean equalTo(Object target);
    boolean notEqualTo(Object target);
    boolean lessThan(Object target);
    boolean largerThan(Object target);
    boolean lessThanOrEqualTo(Object target);
    boolean largerThanOrEqualTo(Object target);
}
