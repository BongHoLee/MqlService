package com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.singleRowFunction;

import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.ColumnElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.MQLElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.SingleRowFunctionElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.ValueElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class LENGTH extends SingleRowFunctionElement {
    public LENGTH(MQLElement ... parameters) {
        super(parameters);
    }

    @Override
    public String getElementExpression() {
        return "LENGTH";
    }

    @Override
    protected Object executeWith(List<MQLElement> parameters, Map<String, Object> singleRow) {
        return execute(parameters.get(0), singleRow);
    }

    private Object execute(Object param, Map<String, Object> singleRow) {
        if (param instanceof String) {
            return ((String) param).length();
        }else if (param instanceof ValueElement && ((ValueElement) param).getValue() instanceof String) {
            return ((String) ((ValueElement) param).getValue()).length();
        } else if (param instanceof ColumnElement && singleRow.get(((ColumnElement) param).getColumnName()) instanceof String) {
            return ((String) singleRow.get(((ColumnElement) param).getColumnName())).length();
        } else if (param instanceof SingleRowFunctionElement){
            return execute(((SingleRowFunctionElement) param).executeAbout(singleRow), singleRow);
        } else {
            throw new RuntimeException("LENGTH Parameter must String");
        }
    }
}
