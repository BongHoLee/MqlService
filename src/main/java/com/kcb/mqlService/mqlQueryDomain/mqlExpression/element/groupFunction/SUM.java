package com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.groupFunction;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.*;

import javax.management.StringValueExp;
import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.IntStream;

public class SUM extends GroupFunctionElement {
    private String expression = "";
    private String alias = "";
    private boolean hasAlias;

    public SUM(String alias, MQLElement parameter) {
        super(parameter);
        this.alias = alias;
        expression = "SUM(" + getParameterExpression() + ")";
        setHasAlias();
    }

    public SUM(MQLElement parameter) {
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

        return IntStream.range(start, end+1).mapToDouble(idx -> {
            Map<String, Object> eachRow = mqlDataStorage.getMqlTable().getTableData().get(idx);
            return eachRow.containsKey(columnName) && eachRow.get(columnName) != null && eachRow.get(columnName) instanceof Number ? (Double) eachRow.get(columnName) : 0;
        }).sum();

    }

    @Override
    protected Object executeWithValueParameter(int start, int end, ValueElement parameter, MQLDataStorage mqlDataStorage) {
        if (parameter.getValueType() == ValueType.NUMBER) {
            BigDecimal value = new BigDecimal(String.valueOf(parameter.getValue()));
            BigDecimal rowCount = new BigDecimal((end - start + 1));
            return value.multiply(rowCount).doubleValue();
        } else {
            return 0;
        }

    }

    @Override
    protected Object executeWithSingleRowFunctionParameter(int start, int end, SingleRowFunctionElement parameter, MQLDataStorage mqlDataStorage) {

        return IntStream.range(start, end+1).mapToDouble(idx -> {
            Map<String, Object> row = mqlDataStorage.getMqlTable().getTableData().get(idx);
            Object functionExecuteResult = parameter.executeAbout(row);
            if (functionExecuteResult instanceof Number) {
                return new BigDecimal(String.valueOf(functionExecuteResult)).doubleValue();
            } else {
                return 0;
            }
        }).sum();

    }

    @Override
    public String getElementExpression() {
        return expression;
    }
}
