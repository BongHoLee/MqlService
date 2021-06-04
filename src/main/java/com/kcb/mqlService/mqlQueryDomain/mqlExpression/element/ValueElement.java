package com.kcb.mqlService.mqlQueryDomain.mqlExpression.element;

public class ValueElement implements MQLElement{
    private Object value;
    private ValueType valueType;

    public ValueElement(Object value) {
        this.value = value;

        if (value instanceof Number)
            valueType = ValueType.NUMBER;
        else
            valueType = ValueType.STRING;
    }

    public Object getValue() {
        return value;
    }
    public ValueType getValueType(){
        return valueType;
    }

    @Override
    public String getElementExpression() {
        return String.valueOf(value);
    }

    @Override
    public boolean hasAlias() {
        return false;
    }

    @Override
    public String getAlias() {
        return "";
    }
}
