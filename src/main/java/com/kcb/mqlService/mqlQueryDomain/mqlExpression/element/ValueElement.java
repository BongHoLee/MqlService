package com.kcb.mqlService.mqlQueryDomain.mqlExpression.element;

public class ValueElement implements MQLElement{
    private Object value;

    public ValueElement(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String getElementExpression() {
        return String.valueOf(value);
    }
}
