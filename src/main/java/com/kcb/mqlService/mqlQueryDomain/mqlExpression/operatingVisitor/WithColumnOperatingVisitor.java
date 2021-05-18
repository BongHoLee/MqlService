package com.kcb.mqlService.mqlQueryDomain.mqlExpression.operatingVisitor;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.ColumnOperandExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.GroupFunctionOperandExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.SingleRowFunctionOperandExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.ValueOperandExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.ColumnElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.SingleRowFunctionElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.ValueElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.relationalOperator.RelationalOperation;

import java.util.*;
import java.util.stream.Collectors;

public class WithColumnOperatingVisitor implements WithOperatingVisitor {
    private ColumnElement compareTargetColumn;
    private RelationalOperation rOperation;

    public WithColumnOperatingVisitor(ColumnElement compareTargetColumn, RelationalOperation rOperation) {
        this.compareTargetColumn = compareTargetColumn;
        this.rOperation = rOperation;
    }


    // column = column => equi Join
    @Override
    public MQLDataStorage visit(ColumnOperandExpression standardExpression, MQLDataStorage mqlDataStorage) {
        MQLTable table = new MQLTable();

        MQLDataSource mqlDataSource = mqlDataStorage.getMqlDataSource();;
        ColumnElement standardColumn = standardExpression.getColumnElement();

        String standardDataSourceId = standardColumn.getDataSourceId();
        String compareDataSourceId = compareTargetColumn.getDataSourceId();

        List<Map<String, Object>> leftDataSource = mqlDataSource.dataSourceOf(standardDataSourceId);
        List<Map<String, Object>> rightDataSource = mqlDataSource.dataSourceOf(compareDataSourceId);

        List<Map<String, Object>> joinedTable = new ArrayList<>();
        leftDataSource.forEach(standardRow -> rightDataSource.forEach(compareRow -> {
            Object standardValue = standardRow.get(standardColumn.getColumnName());
            Object compareValue = compareRow.get(compareTargetColumn.getColumnName());

            if (rOperation.operating(standardValue, compareValue)){
                Map<String, Object> mergedRow = new HashMap<>();
                mergedRow.putAll(compareRow);
                mergedRow.putAll(standardRow);
                joinedTable.add(mergedRow);
            }
        }));

        table.addJoinList(standardDataSourceId, compareDataSourceId);
        table.setTableData(joinedTable);


        return new MQLDataStorage(mqlDataSource, table);
    }


    @Override
    public MQLDataStorage visit(SingleRowFunctionOperandExpression standardExpression, MQLDataStorage mqlDataStorage) {
        return null;
    }

    // value = column
    // ex : 1 = A.KEY
    @Override
    public MQLDataStorage visit(ValueOperandExpression standardExpression, MQLDataStorage mqlDataStorage) {
        MQLDataSource mqlDataSource = mqlDataStorage.getMqlDataSource();

        ValueElement standardValue = standardExpression.getValueElement();
        String compareColumnKey = compareTargetColumn.getColumnName();
        String compareDataSourceId = compareTargetColumn.getDataSourceId();

        List<Map<String, Object>> tableData = mqlDataSource.dataSourceOf(compareDataSourceId);

        List<Map<String, Object>> filteredTable = tableData.stream().filter(
                eachRow ->rOperation.operating(standardValue.getValue(), eachRow.get(compareColumnKey))
        ).collect(Collectors.toList());

        MQLTable resultTable = new MQLTable(new HashSet<>(Collections.singletonList(compareDataSourceId)), filteredTable);

        return new MQLDataStorage(mqlDataSource, resultTable);
    }

    @Override
    public MQLDataStorage visit(GroupFunctionOperandExpression standardExpression, MQLDataStorage mqlDataStorage) {
        return null;
    }
}
