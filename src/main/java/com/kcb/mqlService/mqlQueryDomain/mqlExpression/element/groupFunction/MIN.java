package com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.groupFunction;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

public class MIN extends GroupFunctionElement {
    private String expression = "";
    private String alias = "";
    private boolean hasAlias;

    public MIN(String alias, MQLElement parameter) {
        super(parameter);
        this.alias = alias;
        expression = "MIN(" + getParameterExpression() + ")";
        setHasAlias();
    }

    public MIN(MQLElement parameter) {
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

        Optional<Map<String, Object>> result = IntStream.range(start, end+1).filter(idx -> {
            Map<String, Object> eachRow = mqlDataStorage.getMqlTable().getTableData().get(idx);
            return eachRow.containsKey(columnName) && eachRow.get(columnName) != null && eachRow.get(columnName) instanceof Number;
        }).mapToObj(idx -> mqlDataStorage.getMqlTable().getTableData().get(idx))
                .min((row1, row2) -> {
                    BigDecimal row1Number = new BigDecimal(String.valueOf(row1.get(columnName)));
                    BigDecimal row2Number = new BigDecimal(String.valueOf(row2.get(columnName)));

                    return row1Number.compareTo(row2Number);
                });


        return result.map(stringObjectMap -> stringObjectMap.get(columnName)).orElse(0);
    }

    @Override
    protected Object executeWithValueParameter(int start, int end, ValueElement parameter, MQLDataStorage mqlDataStorage) {
        return parameter.getValue();
    }

    @Override
    protected Object executeWithSingleRowFunctionParameter(int start, int end, SingleRowFunctionElement parameter, MQLDataStorage mqlDataStorage) {

        Optional<Object> result = IntStream.range(start, end+1).mapToObj(idx -> {
            Map<String, Object> eachRow = mqlDataStorage.getMqlTable().getTableData().get(idx);
            return parameter.executeAbout(eachRow);
        }).filter(eachResult -> eachResult instanceof Number)
                .min((result1, result2) -> {
                    BigDecimal number1 = new BigDecimal(String.valueOf(result1));
                    BigDecimal number2 = new BigDecimal(String.valueOf(result2));

                    return number1.compareTo(number2);
                });


        return result.orElse(0);
    }
    @Override
    public String getElementExpression() {
        return expression;
    }
}
