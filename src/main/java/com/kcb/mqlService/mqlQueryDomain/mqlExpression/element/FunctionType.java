package com.kcb.mqlService.mqlQueryDomain.mqlExpression.element;

public enum FunctionType {
    COLUMN(true),
    VALUE(false);

    private boolean isColumn;

     FunctionType(boolean isColumn) {
        this.isColumn = isColumn;
    }

    public boolean isColumn() {
        return isColumn;
    }
}
