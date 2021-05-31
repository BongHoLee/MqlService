package com.kcb.mqlService.mqlQueryDomain.mqlExpression.element;

public enum SingleRowFunctionParameterType {
    COLUMN(true),
    VALUE(false);

    private boolean isColumn;

     SingleRowFunctionParameterType(boolean isColumn) {
        this.isColumn = isColumn;
    }

    public boolean isColumn() {
        return isColumn;
    }
}
