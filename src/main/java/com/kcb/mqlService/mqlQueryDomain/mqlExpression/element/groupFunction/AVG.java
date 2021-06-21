package com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.groupFunction;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class AVG extends GroupFunctionElement {
    private String expression = "";
    private String alias = "";
    private boolean hasAlias;

    public AVG(String alias) {
        this.alias = alias;
        setHasAlias();
    }

    public AVG(String alias, MQLElement parameter) {
        super(parameter);
        this.alias = alias;
        setExpression();
        setHasAlias();
    }

    public void addParameter(MQLElement parameter) {
        super.addParameter(parameter);
        setExpression();
    }

    public AVG(MQLElement parameter) {
        this("", parameter);
    }

    private void setExpression() {
        expression = "AVG(" + getParameterExpression() + ")";
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
            Map<String, Object> row = mqlDataStorage.getMqlTable().getTableData().get(idx);
            return row.containsKey(columnName) && row.get(columnName) != null && row.get(columnName) instanceof Number;
        }).mapToDouble(idx -> {
            Map<String, Object> row = mqlDataStorage.getMqlTable().getTableData().get(idx);
            return new BigDecimal(String.valueOf(row.get(columnName))).doubleValue();
        }).average().orElse(Double.NaN);
    }

    @Override
    protected Object executeWithValueParameter(int start, int end, ValueElement parameter, MQLDataStorage mqlDataStorage) {
        return parameter.getValueType() == ValueType.STRING ? 0 : parameter.getValue();
    }

    @Override
    protected Object executeWithSingleRowFunctionParameter(int start, int end, SingleRowFunctionElement parameter, MQLDataStorage mqlDataStorage) {
        if (parameter.hasColumn()) {
            return IntStream.range(start, end+1).mapToObj(idx -> {
                Map<String, Object> row = mqlDataStorage.getMqlTable().getTableData().get(idx);
                return parameter.executeAbout(row);
            }).filter(executeResult -> executeResult instanceof Number)
                    .mapToDouble(numberResult -> new BigDecimal(String.valueOf(numberResult)).doubleValue())
                    .average().orElse(Double.NaN);
        } else {
            Object result = parameter.executeAbout(new HashMap<>());
            return result instanceof Number ? new BigDecimal(String.valueOf(result)).doubleValue() : 0;
        }

    }

    @Override
    public String getElementExpression() {
        return expression;
    }

}
