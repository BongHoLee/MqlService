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

import java.util.*;
import java.util.stream.Collectors;

public class WithValueTargetOperating implements WithTargetOperating {
    private ValueElement compareValue;

    public WithValueTargetOperating(ValueElement compareValue) {
        this.compareValue = compareValue;
    }

    @Override
    public MQLDataStorage operate(ColumnElement standardColumnElement, RelationalOperation rOperation, MQLDataStorage mqlDataStorage) {
        String queryID = mqlDataStorage.getQueryID();
        String queryScript = mqlDataStorage.getQueryScript();
        MQLDataSource mqlDataSource = mqlDataStorage.getMqlDataSource();

        String standardColumnKey = standardColumnElement.getColumnName();
        String standardDataSourceId = standardColumnElement.getDataSourceId();

        List<Map<String, Object>> tableData = mqlDataSource.dataSourceOf(standardDataSourceId);

        List<Map<String, Object>> filteredTable = tableData.stream().filter(
                eachRow ->rOperation.operating(eachRow.get(standardColumnKey), compareValue.getValue())
        ).collect(Collectors.toList());

        MQLTable resultTable = new MQLTable(new HashSet<>(Collections.singletonList(standardDataSourceId)), filteredTable);
        return new MQLDataStorage(queryID, queryScript, mqlDataSource, resultTable);
    }

    @Override
    public MQLDataStorage operate(SingleRowFunctionElement standardFunctionElement, RelationalOperation rOperation, MQLDataStorage mqlDataStorage) {
        String queryID = mqlDataStorage.getQueryID();
        String queryScript = mqlDataStorage.getQueryScript();
        MQLDataSource mqlDataSource = mqlDataStorage.getMqlDataSource();


        if (standardFunctionElement.hasColumn()) {
            String standardColumnKey = standardFunctionElement.getDataSourceIdForRow();
            List<Map<String, Object>> tableData = mqlDataSource.dataSourceOf(standardColumnKey);

            List<Map<String, Object>> filteredTable = tableData.stream().filter(
                    eachRow -> rOperation.operating(standardFunctionElement.executeAbout(eachRow), compareValue.getValue())
            ).collect(Collectors.toList());

            MQLTable resultTable = new MQLTable(new HashSet<>(Collections.singletonList(standardColumnKey)), filteredTable);
            return new MQLDataStorage(queryID, queryScript, mqlDataSource, resultTable);
        } else {
            if (rOperation.operating(standardFunctionElement.executeAbout(new HashMap<>()), compareValue.getValue())) {
                return mqlDataStorage;
            } else {
                return new MQLDataStorage(queryID, queryScript, new MQLDataSource(), new MQLTable());
            }
        }
    }

    @Override
    public MQLDataStorage operate(ValueElement standardValueElement, RelationalOperation rOperation, MQLDataStorage mqlDataStorage) {
        return mqlDataStorage;
    }

    @Override
    public MQLDataStorage operate(GroupFunctionElement standardGroupFunctionElement, RelationalOperation rOperation, MQLDataStorage mqlDataStorage) {
        MQLTable table = new MQLTable(mqlDataStorage.getMqlTable());
        MQLDataSource mqlDataSource = mqlDataStorage.getMqlDataSource();
        String queryID = mqlDataStorage.getQueryID();
        String queryScript = mqlDataStorage.getQueryScript();
        List<Map<String, Object>> operatedTableData = new ArrayList<>();
        List<Integer> updatedGroupingIdx = new ArrayList<>();

        int start = 0;
        int skipCount = 0;
        for (int end : table.getGroupingIdxs()) {
            Object value = compareValue.getValue();
            Object functionResult = standardGroupFunctionElement.executeAbout(start, end, mqlDataStorage);
            if (rOperation.operating(functionResult, value)) {
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
    }
}
