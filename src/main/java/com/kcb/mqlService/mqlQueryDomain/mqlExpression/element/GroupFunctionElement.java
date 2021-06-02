package com.kcb.mqlService.mqlQueryDomain.mqlExpression.element;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;

public abstract class GroupFunctionElement implements MQLElement{
    private MQLElement parameter;
    private String parameterExpression = "";

    public GroupFunctionElement(MQLElement parameter) {
        this.parameter = parameter;

        if (parameter instanceof ValueElement && ((ValueElement) parameter).getValueType() == ValueType.STRING) {
            parameterExpression = "'" + parameter.getElementExpression() + "'";
        } else {
            parameterExpression = parameter.getElementExpression();
        }
    }

    public Object executeAbout(int start, int end, MQLDataStorage mqlDataStorage) {
        if (parameter instanceof ColumnElement) {
            return executeWithColumnParameter(start, end, (ColumnElement) parameter, mqlDataStorage);
        } else if (parameter instanceof ValueElement) {
            return executeWithValueParameter(start, end, (ValueElement) parameter, mqlDataStorage);
        } else if (parameter instanceof SingleRowFunctionElement) {
            return executeWithSingleRowFunctionParameter(start, end, (SingleRowFunctionElement) parameter, mqlDataStorage);
        } else {
            throw new RuntimeException("Not Valid Parameter Type!");
        }
    }

    protected String getParameterExpression() {
        return parameterExpression;
    }

    protected abstract Object executeWithColumnParameter(int start, int end, ColumnElement parameter, MQLDataStorage mqlDataStorage);
    protected abstract Object executeWithValueParameter(int start, int end, ValueElement parameter, MQLDataStorage mqlDataStorage);
    protected abstract Object executeWithSingleRowFunctionParameter(int start, int end, SingleRowFunctionElement parameter, MQLDataStorage mqlDataStorage);


}
