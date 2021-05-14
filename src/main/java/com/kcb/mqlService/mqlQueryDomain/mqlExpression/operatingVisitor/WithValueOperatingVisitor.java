package com.kcb.mqlService.mqlQueryDomain.mqlExpression.operatingVisitor;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.ColumnOperandExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.FunctionOperandExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.ValueOperandExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.ColumnElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.ValueElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.relationalOperator.RelationalOperation;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WithValueOperatingVisitor implements WithOperatingVisitor {
    private ValueElement compareValue;
    private RelationalOperation rOperation;

    public WithValueOperatingVisitor(ValueElement compareValue, RelationalOperation rOperation) {
        this.compareValue = compareValue;
        this.rOperation = rOperation;
    }


    @Override
    public MQLDataStorage visit(ColumnOperandExpression standardExpression, MQLDataStorage mqlDataStorage) {
        MQLDataSource mqlDataSource = mqlDataStorage.getMqlDataSource();

        ColumnElement standardColumnElement = standardExpression.getColumnElement();
        String standardColumnKey = standardColumnElement.getColumnName();
        String standardDataSourceId = standardColumnElement.getDataSourceId();

        List<Map<String, Object>> tableData = mqlDataSource.dataSourceOf(standardDataSourceId);

        List<Map<String, Object>> filteredTable = tableData.stream().filter(
                eachRow ->rOperation.operating(eachRow.get(standardColumnKey), compareValue.getValue())
        ).collect(Collectors.toList());

        MQLTable resultTable = new MQLTable(new HashSet<>(Collections.singletonList(standardDataSourceId)), filteredTable);
        return new MQLDataStorage(mqlDataSource, resultTable);
    }

    @Override
    public MQLDataStorage visit(FunctionOperandExpression standardExpression, MQLDataStorage mqlDataStorage) {
        return null;
    }

    // WHERE 1=1
    @Override
    public MQLDataStorage visit(ValueOperandExpression standardExpression,MQLDataStorage mqlDataStorage) {
        return mqlDataStorage;
    }
}
