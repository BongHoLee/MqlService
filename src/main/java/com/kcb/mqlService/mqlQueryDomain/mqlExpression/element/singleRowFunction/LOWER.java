package com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.singleRowFunction;

import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class LOWER extends SingleRowFunctionElement {
    private String expression = "";

    public LOWER(MQLElement ... parameters) {
        super(parameters);
        expression = "LOWER(";

        for (int i=0; i<parameters.length; i++) {
            MQLElement each = parameters[i];

            if (each instanceof ValueElement && ((ValueElement) each).getValueType() == ValueType.STRING) {
                expression += "'" + each.getElementExpression() + "'";
            } else {
                expression += each.getElementExpression();
            }

            if (i < parameters.length-1) {
                expression += ",";
            }
        }

        expression += ")";
    }

    @Override
    public String getElementExpression() {
        return expression;
    }

    @Override
    public Object executeWith(List<MQLElement> parameters, Map<String, Object> singleRow) {
        return execute(parameters.get(0), singleRow);
    }

    private Object execute(Object param, Map<String, Object> singleRow) {
        if (param instanceof String) {
            return ((String) param).toLowerCase();
        } else if (param instanceof ValueElement && ((ValueElement) param).getValue() instanceof String) {
            return ((String) ((ValueElement) param).getValue()).toLowerCase();
        } else if (param instanceof ColumnElement && singleRow.get(((ColumnElement) param).getColumnName()) instanceof String) {
            return ((String) singleRow.get(((ColumnElement) param).getColumnName())).toLowerCase();
        } else if (param instanceof SingleRowFunctionElement){
            return execute(((SingleRowFunctionElement) param).executeAbout(singleRow), singleRow);
        } else {
            throw new RuntimeException("LOWER Parameter must String");
        }
    }
}
