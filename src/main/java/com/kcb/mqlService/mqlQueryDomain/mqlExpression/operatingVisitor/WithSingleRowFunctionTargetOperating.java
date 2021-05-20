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
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.relationalOperator.RelationalOperator;

import java.util.*;
import java.util.stream.Collectors;

public class WithSingleRowFunctionTargetOperating implements WithTargetOperating {
    private SingleRowFunctionElement functionElement;

    public WithSingleRowFunctionTargetOperating(SingleRowFunctionElement functionElement) {
        this.functionElement = functionElement;
    }

    /**
     *
     * @param mqlDataStorage
     * @return
     *
     * A.EmployeeID >= LENGTH(Column or Value)
     */
    @Override
    public MQLDataStorage operate(ColumnElement standardColumnElement, RelationalOperation rOperation, MQLDataStorage mqlDataStorage) {
        MQLTable resultTable = new MQLTable();
        MQLDataSource mqlDataSource = mqlDataStorage.getMqlDataSource();

        List<Map<String, Object>> mergedTableData = new ArrayList<>();
        List<Map<String, Object>> standardTableData = mqlDataSource.dataSourceOf(standardColumnElement.getDataSourceId());
        resultTable.addJoinList(standardColumnElement.getDataSourceId());

        if (functionElement.hasColumn()) {
            resultTable.addJoinList(functionElement.getDataSourceIdForRow());
            List<Map<String, Object>> compareTableData = mqlDataSource.dataSourceOf(functionElement.getDataSourceIdForRow());

            // function column, standard column are same : ex) A.CustomerID > LENGTH(A.City)
            if (standardColumnElement.getColumnName().equals(functionElement.getDataSourceIdForRow())) {
                List<Map<String, Object>> mergedSameTableData =
                        standardTableData.stream()
                                .filter(eachRow -> rOperation.operating(eachRow.get(standardColumnElement.getColumnName()), functionElement.executeAbout(eachRow)))
                                .collect(Collectors.toList());

                mergedTableData.addAll(mergedSameTableData);

                // function column, standard column are not same : ex ) A.CustomerID > LENGTH(B.CategoryName)
            } else {
                standardTableData.forEach(standardRow -> {
                    compareTableData.forEach(compareRow -> {
                        if (rOperation.operating(standardRow.get(standardColumnElement.getColumnName()), functionElement.executeAbout(compareRow))) {
                            Map<String, Object> mergedRow = new HashMap<>();
                            mergedRow.putAll(standardRow);
                            mergedRow.putAll(compareRow);
                            mergedTableData.add(mergedRow);
                        }
                    });
                });
            }

            // function(value) : ex) A.CustomerID > LENGTH(3)
        } else {
            List<Map<String, Object>> tempMergedTable = standardTableData.stream()
                    .filter(eachRow -> rOperation.operating(eachRow.get(standardColumnElement.getColumnName()), functionElement.executeAbout(new HashMap<>())))
                    .collect(Collectors.toList());

            mergedTableData.addAll(tempMergedTable);
        }

        resultTable.setTableData(mergedTableData);

        return new MQLDataStorage(mqlDataSource, resultTable);
    }

    @Override
    public MQLDataStorage operate(SingleRowFunctionElement standardFunctionElement, RelationalOperation rOperation, MQLDataStorage mqlDataStorage) {
        return null;
    }

    @Override
    public MQLDataStorage operate(ValueElement standardValueElement, RelationalOperation rOperation, MQLDataStorage mqlDataStorage) {
        return null;
    }
}
