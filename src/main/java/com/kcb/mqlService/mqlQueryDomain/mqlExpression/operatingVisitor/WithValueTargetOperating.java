package com.kcb.mqlService.mqlQueryDomain.mqlExpression.operatingVisitor;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.ColumnOperandExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.SingleRowFunctionOperandExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.ValueOperandExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.ColumnElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.SingleRowFunctionElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.ValueElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.relationalOperator.RelationalOperation;

import java.util.*;
import java.util.stream.Collectors;

public class WithValueTargetOperating implements WithTargetOperating {
    private ValueElement compareValue;

    public WithValueTargetOperating(ValueElement compareValue) {
        this.compareValue = compareValue;
    }

    @Override
    public MQLDataStorage operate(ColumnElement standardColumnElement, RelationalOperation rOperation, MQLDataStorage mqlDataStorage) {
        MQLDataSource mqlDataSource = mqlDataStorage.getMqlDataSource();

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
    public MQLDataStorage operate(SingleRowFunctionElement standardFunctionElement, RelationalOperation rOperation, MQLDataStorage mqlDataStorage) {

        MQLDataSource mqlDataSource = mqlDataStorage.getMqlDataSource();


        if (standardFunctionElement.hasColumn()) {
            String standardColumnKey = standardFunctionElement.getDataSourceIdForRow();
            List<Map<String, Object>> tableData = mqlDataSource.dataSourceOf(standardColumnKey);

            List<Map<String, Object>> filteredTable = tableData.stream().filter(
                    eachRow -> rOperation.operating(standardFunctionElement.executeAbout(eachRow), compareValue.getValue())
            ).collect(Collectors.toList());

            MQLTable resultTable = new MQLTable(new HashSet<>(Collections.singletonList(standardColumnKey)), filteredTable);
            return new MQLDataStorage(mqlDataSource, resultTable);
        } else {
            if (rOperation.operating(standardFunctionElement.executeAbout(new HashMap<>()), compareValue.getValue())) {
                return mqlDataStorage;
            } else {
                return new MQLDataStorage(new MQLDataSource(), new MQLTable());
            }
        }
    }

    @Override
    public MQLDataStorage operate(ValueElement standardValueElement, RelationalOperation rOperation, MQLDataStorage mqlDataStorage) {
        return mqlDataStorage;
    }
}
