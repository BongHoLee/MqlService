package com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.groupFunction;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.*;

public class AVG extends GroupFunctionElement {
    private String expression = "";
    public AVG(MQLElement parameter) {
        super(parameter);
        expression = "AVG(" + getParameterExpression() + ")";
    }


    @Override
    protected Object executeWithColumnParameter(int start, int end, ColumnElement parameter, MQLDataStorage mqlDataStorage) {

        return 0;
    }

    @Override
    protected Object executeWithValueParameter(int start, int end, ValueElement parameter, MQLDataStorage mqlDataStorage) {
        return 0;
    }

    @Override
    protected Object executeWithSingleRowFunctionParameter(int start, int end, SingleRowFunctionElement parameter, MQLDataStorage mqlDataStorage) {

        return 0;
    }

    @Override
    public String getElementExpression() {
        return expression;
    }
}
