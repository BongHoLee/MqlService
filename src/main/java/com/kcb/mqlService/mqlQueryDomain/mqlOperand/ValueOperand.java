package com.kcb.mqlService.mqlQueryDomain.mqlOperand;

public class ValueOperand implements MQLOperand {

    private String expression;

    public ValueOperand(String expression) {
        this.expression = expression;
    }

    @Override
    public String getExpression() {
        return expression;
    }
}
