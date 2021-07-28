package com.kcb.mqlService.mqlQueryDomain.mqlExpression.element;

public enum SingleRowFunctionParameterType {
    NULL("NULL"),
    COLUMN("COLUMN"),
    VALUE("VALUE");

    private String TYPE;

     SingleRowFunctionParameterType(String TYPE) {
        this.TYPE = TYPE;
    }

    public boolean isColumn() {
        return TYPE.equals("COLUMN");
    }

    public boolean isNull() {
         return TYPE.equals("NULL");
    }

}
