package com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.groupFunction;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.*;

import java.util.Map;
import java.util.stream.IntStream;

public class COUNT extends GroupFunctionElement {
    private String expression = "";
    private String alias = "";
    private boolean hasAlias;

    public COUNT(String alias, MQLElement parameter) {
        super(parameter);
        this.alias = alias;
        expression = "COUNT(" + getParameterExpression() + ")";
        setHasAlias();
    }

    public COUNT(MQLElement parameter) {
        this("", parameter);
    }

    private void setHasAlias() {
        hasAlias =  !alias.isEmpty();
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
    protected Object executeWithColumnParameter(int start, int end, ColumnElement parameter, MQLDataStorage mqlDataStorage) {
        String columnName = parameter.getColumnName();

        return IntStream.range(start, end+1).filter(idx -> {
            Map<String, Object> eachMap = mqlDataStorage.getMqlTable().getTableData().get(idx);
            return eachMap.containsKey(columnName) && eachMap.get(columnName) != null;
        }).count();
    }

    @Override
    protected Object executeWithValueParameter(int start, int end, ValueElement parameter, MQLDataStorage mqlDataStorage) {
        return defaultCounting(start, end);
    }

    @Override
    protected Object executeWithSingleRowFunctionParameter(int start, int end, SingleRowFunctionElement parameter, MQLDataStorage mqlDataStorage) {
        return defaultCounting(start, end);
    }

    private int defaultCounting(int start, int end) {
        return (end - start) + 1;
    }

    @Override
    public String getElementExpression() {
        return expression;
    }
}
