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

public class WithColumnTargetOperating implements WithTargetOperating {
    private ColumnElement compareTargetColumn;
    public WithColumnTargetOperating(ColumnElement compareTargetColumn) {
        this.compareTargetColumn = compareTargetColumn;

    }


    @Override
    public MQLDataStorage operate(ColumnElement standardColumnElement, RelationalOperation rOperation, MQLDataStorage mqlDataStorage) {
        MQLTable table = new MQLTable();

        MQLDataSource mqlDataSource = mqlDataStorage.getMqlDataSource();;
        String standardDataSourceId = standardColumnElement.getDataSourceId();
        String compareDataSourceId = compareTargetColumn.getDataSourceId();

        List<Map<String, Object>> leftDataSource = mqlDataSource.dataSourceOf(standardDataSourceId);
        List<Map<String, Object>> rightDataSource = mqlDataSource.dataSourceOf(compareDataSourceId);

        List<Map<String, Object>> joinedTable = new ArrayList<>();
        leftDataSource.forEach(standardRow -> rightDataSource.forEach(compareRow -> {
            Object standardValue = standardRow.get(standardColumnElement.getColumnName());
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
    public MQLDataStorage operate(SingleRowFunctionElement standardFunctionElement, RelationalOperation rOperation, MQLDataStorage mqlDataStorage) {

        MQLTable resultTable = new MQLTable();
        MQLDataSource mqlDataSource = mqlDataStorage.getMqlDataSource();

        List<Map<String, Object>> mergedTableData = new ArrayList<>();
        List<Map<String, Object>> compareTableData = mqlDataSource.dataSourceOf(compareTargetColumn.getDataSourceId());
        resultTable.addJoinList(compareTargetColumn.getDataSourceId());

        if (standardFunctionElement.hasColumn()) {
            resultTable.addJoinList(standardFunctionElement.getDataSourceIdForRow());
            List<Map<String, Object>> standardTableData = mqlDataSource.dataSourceOf(standardFunctionElement.getDataSourceIdForRow());

            // function column's table, compare column's table are same : ex)  LENGTH(A.City) > A.CategoryID
            if (compareTargetColumn.getColumnName().equals(standardFunctionElement.getDataSourceIdForRow())) {
                List<Map<String, Object>> mergedSameTableData =
                        standardTableData.stream()
                                .filter(eachRow -> rOperation.operating(standardFunctionElement.executeAbout(eachRow), eachRow.get(compareTargetColumn.getColumnName())))
                                .collect(Collectors.toList());

                mergedTableData.addAll(mergedSameTableData);

                // function column's table, compare column's table are not same : ex )  LENGTH(B.CategoryName) > A.CustomerID
            } else {
                standardTableData.forEach(standardRow -> {
                    compareTableData.forEach(compareRow -> {
                        if (rOperation.operating(standardFunctionElement.executeAbout(standardRow), compareRow.get(compareTargetColumn.getColumnName()))) {
                            Map<String, Object> mergedRow = new HashMap<>();
                            mergedRow.putAll(standardRow);
                            mergedRow.putAll(compareRow);
                            mergedTableData.add(mergedRow);
                        }
                    });
                });
            }

            // function(value) : ex)  LENGTH(3) > A.CustomerID
        } else {
            List<Map<String, Object>> tempMergedTable = compareTableData.stream()
                    .filter(eachRow -> rOperation.operating(standardFunctionElement.executeAbout(new HashMap<>()), eachRow.get(compareTargetColumn.getColumnName())))
                    .collect(Collectors.toList());

            mergedTableData.addAll(tempMergedTable);
        }

        resultTable.setTableData(mergedTableData);

        return new MQLDataStorage(mqlDataSource, resultTable);
    }

    @Override
    public MQLDataStorage operate(ValueElement standardValueElement, RelationalOperation rOperation, MQLDataStorage mqlDataStorage) {
        MQLDataSource mqlDataSource = mqlDataStorage.getMqlDataSource();

        String compareColumnKey = compareTargetColumn.getColumnName();
        String compareDataSourceId = compareTargetColumn.getDataSourceId();

        List<Map<String, Object>> tableData = mqlDataSource.dataSourceOf(compareDataSourceId);

        List<Map<String, Object>> filteredTable = tableData.stream().filter(
                eachRow ->rOperation.operating(standardValueElement.getValue(), eachRow.get(compareColumnKey))
        ).collect(Collectors.toList());

        MQLTable resultTable = new MQLTable(new HashSet<>(Collections.singletonList(compareDataSourceId)), filteredTable);

        return new MQLDataStorage(mqlDataSource, resultTable);
    }
}
