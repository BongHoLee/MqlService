package com.kcb.mqlService.mqlQueryDomain.mqlExpression.relationalOperator;

@FunctionalInterface
public interface  RelationalOperation {
    boolean operating(Object t1, Object t2);
}
