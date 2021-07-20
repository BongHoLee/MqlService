package com.kcb.mqlService.mqlQueryDomain.mqlExpression.operatingVisitor;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.ColumnOperandExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.SingleRowFunctionOperandExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.ValueOperandExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.ColumnElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.GroupFunctionElement;
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
        String queryID = mqlDataStorage.getQueryID();
        String queryScript = mqlDataStorage.getQueryScript();

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

        return new MQLDataStorage(queryID, queryScript, mqlDataSource, resultTable);
    }

    @Override
    public MQLDataStorage operate(SingleRowFunctionElement standardFunctionElement, RelationalOperation rOperation, MQLDataStorage mqlDataStorage) {
        MQLTable resultTable = new MQLTable();
        MQLDataSource mqlDataSource = mqlDataStorage.getMqlDataSource();
        String queryID = mqlDataStorage.getQueryID();
        String queryScript = mqlDataStorage.getQueryScript();
        List<Map<String, Object>> mergedTableData = new ArrayList<>();

        // LENGTH(COLUMN) == LENGTH(COLUMN)
        if (standardFunctionElement.hasColumn() && functionElement.hasColumn()) {
            List<Map<String, Object>> standardTableData = mqlDataSource.dataSourceOf(standardFunctionElement.getDataSourceIdForRow());
            List<Map<String, Object>> compareTableData = mqlDataSource.dataSourceOf(functionElement.getDataSourceIdForRow());
            resultTable.addJoinList(standardFunctionElement.getDataSourceIdForRow(), functionElement.getDataSourceIdForRow());

            if (standardFunctionElement.getDataSourceIdForRow().equals(functionElement.getDataSourceIdForRow())) {
                List<Map<String, Object>> mergedSameTableData =
                        standardTableData.stream()
                                .filter(eachRow -> rOperation.operating(standardFunctionElement.executeAbout(eachRow), functionElement.executeAbout(eachRow)))
                                .collect(Collectors.toList());

                mergedTableData.addAll(mergedSameTableData);

                // function column, standard column are not same : ex ) LENGTH(A.CustomerID) > LENGTH(B.CategoryName)
            } else {
                standardTableData.forEach(standardRow -> {
                    compareTableData.forEach(compareRow -> {
                        if (rOperation.operating(standardFunctionElement.executeAbout(standardRow), functionElement.executeAbout(compareRow))) {
                            Map<String, Object> mergedRow = new HashMap<>();
                            mergedRow.putAll(standardRow);
                            mergedRow.putAll(compareRow);
                            mergedTableData.add(mergedRow);
                        }
                    });
                });
            }
            // LENGTH(COLUMN) == LENGTH(VALUE)
        } else if (!standardFunctionElement.hasColumn() && functionElement.hasColumn()) {
            List<Map<String, Object>> compareTableData = mqlDataSource.dataSourceOf(functionElement.getDataSourceIdForRow());
            List<Map<String, Object>> tempMergedTable = compareTableData.stream()
                    .filter(eachRow -> rOperation.operating(standardFunctionElement.executeAbout(new HashMap<>()), functionElement.executeAbout(eachRow)))
                    .collect(Collectors.toList());

            mergedTableData.addAll(tempMergedTable);
        } else if (standardFunctionElement.hasColumn() && !functionElement.hasColumn()) {
            List<Map<String, Object>> standardTableData = mqlDataSource.dataSourceOf(standardFunctionElement.getDataSourceIdForRow());
            List<Map<String, Object>> tempMergedTable = standardTableData.stream()
                    .filter(eachRow -> rOperation.operating(standardFunctionElement.executeAbout(eachRow), functionElement.executeAbout(new HashMap<>())))
                    .collect(Collectors.toList());

            mergedTableData.addAll(tempMergedTable);

        } else {
            // NOT TO DO
        }


        resultTable.setTableData(mergedTableData);
        return new MQLDataStorage(queryID, queryScript, mqlDataSource, resultTable);

    }

    @Override
    public MQLDataStorage operate(ValueElement standardValueElement, RelationalOperation rOperation, MQLDataStorage mqlDataStorage) {
        MQLDataSource mqlDataSource = mqlDataStorage.getMqlDataSource();
        String queryID = mqlDataStorage.getQueryID();
        String queryScript = mqlDataStorage.getQueryScript();

        if (functionElement.hasColumn()) {
            String standardColumnKey = functionElement.getDataSourceIdForRow();
            List<Map<String, Object>> tableData = mqlDataSource.dataSourceOf(standardColumnKey);

            List<Map<String, Object>> filteredTable = tableData.stream().filter(
                    eachRow -> rOperation.operating( standardValueElement.getValue(), functionElement.executeAbout(eachRow))
            ).collect(Collectors.toList());

            MQLTable resultTable = new MQLTable(new HashSet<>(Collections.singletonList(standardColumnKey)), filteredTable);
            return new MQLDataStorage(queryID, queryScript, mqlDataSource, resultTable);
        } else {
            if (rOperation.operating(standardValueElement.getValue(), functionElement.executeAbout(new HashMap<>()))) {
                return mqlDataStorage;
            } else {
                return new MQLDataStorage(queryID, queryScript, new MQLDataSource(), new MQLTable());
            }
        }
    }

    @Override
    public MQLDataStorage operate(GroupFunctionElement standardGroupFunctionElement, RelationalOperation rOperation, MQLDataStorage mqlDataStorage) {

        if (mqlDataStorage.getMqlTable().isGrouped()) {
            String queryID = mqlDataStorage.getQueryID();
            String queryScript = mqlDataStorage.getQueryScript();
            MQLTable table = new MQLTable(mqlDataStorage.getMqlTable());
            MQLDataSource mqlDataSource = mqlDataStorage.getMqlDataSource();
            List<Map<String, Object>> operatedTableData = new ArrayList<>();
            List<Integer> updatedGroupingIdx = new ArrayList<>();

            int start = 0;
            int skipCount = 0;
            for (int end : table.getGroupingIdxs()) {
                Object value = executeForGroupingData(mqlDataStorage, end, functionElement);
                Object functionResult = standardGroupFunctionElement.executeAbout(start, end, mqlDataStorage);
                if (rOperation.operating( functionResult, value)) {
                    operatedTableData.addAll(table.getTableData().subList(start, end + 1));
                    updatedGroupingIdx.add(end - skipCount);
                } else {
                    skipCount = skipCount + (end - start + 1);
                }
                start = end + 1;
            }

            table.setTableData(operatedTableData);
            table.setGroupingIdx(updatedGroupingIdx);
            return new MQLDataStorage(queryID, queryScript, mqlDataSource, table);
        } else {
            throw new RuntimeException("Can't Group function Operating!");
        }
    }

    private Object executeForGroupingData(MQLDataStorage mqlDataStorage, int idx, SingleRowFunctionElement element) {
        Map<String, Object> row = mqlDataStorage.getMqlTable().getTableData().get(idx);
        if ( element.hasColumn()) {
            if (mqlDataStorage.getMqlTable().getGroupingElements().contains( element.getColumnParameterName())) {
                return ( element.executeAbout(row));
            } else {
                throw new RuntimeException(element.getElementExpression() + " should included in Group By Clause!");
            }
        } else {
            return ( element.executeAbout(new HashMap<>()));
        }
    }
}
