package com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.singleRowFunction;

import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class LENGTH extends SingleRowFunctionElement {
    private String expression = "";
    private boolean hasAlias;
    private String alias = "";

    public LENGTH(String alias, MQLElement ... parameters) {
        super(parameters);
        this.alias = alias;
        makeExpression(parameters);
        setHasAlias();

    }

    public LENGTH(MQLElement ... parameters) {
        this("", parameters);
    }

    private void makeExpression(MQLElement ... parameters) {
        expression = "LENGTH(";

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

    private void setHasAlias() {
        hasAlias = !alias.isEmpty();
    }

    @Override
    public String getElementExpression() {
        return expression;
    }

    @Override
    public boolean hasAlias() {
        return hasAlias;
    }

    @Override
    public String getAlias() {
        return alias;
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
        } else if(param instanceof ValueElement && ((ValueElement) param).getValue() instanceof Number) {
            return (String.valueOf(((ValueElement) param).getValue())).length();
        }  else if(param instanceof ColumnElement ){
            return String.valueOf((singleRow.get(((ColumnElement) param).getColumnName()))).length();
        } else if (param instanceof SingleRowFunctionElement){
            return execute(((SingleRowFunctionElement) param).executeAbout(singleRow), singleRow);
        } else {
            throw new RuntimeException("LENGTH Parameter must String");
        }
    }
}
