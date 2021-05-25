package com.kcb.mqlService.mqlQueryDomain.mqlExpression.element;

public enum FunctionParameterType {
    COLUMN(true),
    VALUE(false);

    private boolean isColumn;

     FunctionParameterType(boolean isColumn) {
        this.isColumn = isColumn;
    }

    public boolean isColumn() {
        return isColumn;
    }
}
