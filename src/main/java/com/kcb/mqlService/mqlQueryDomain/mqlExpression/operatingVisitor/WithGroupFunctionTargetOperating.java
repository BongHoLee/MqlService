package com.kcb.mqlService.mqlQueryDomain.mqlExpression.operatingVisitor;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.*;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.relationalOperator.RelationalOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class WithGroupFunctionTargetOperating implements WithTargetOperating {
    private GroupFunctionElement compareTargetGroupFunction;

    public WithGroupFunctionTargetOperating(GroupFunctionElement compareTargetGroupFunction) {
        this.compareTargetGroupFunction = compareTargetGroupFunction;
    }

    @Override
    public MQLDataStorage operate(ColumnElement standardColumnElement, RelationalOperation rOperation, MQLDataStorage mqlDataStorage) {
        if (isGrouped(mqlDataStorage) && mqlDataStorage.getMqlTable().getGroupingElements().contains(standardColumnElement.getColumnName())) {
            return operating(mqlDataStorage, rOperation, standardColumnElement);
        } else {
            throw new RuntimeException("Can't Group function Operating!");
        }
    }

    @Override
    public MQLDataStorage operate(SingleRowFunctionElement standardFunctionElement, RelationalOperation rOperation, MQLDataStorage mqlDataStorage) {
        if (isGrouped(mqlDataStorage)) {
            if (standardFunctionElement.hasColumn() && mqlDataStorage.getMqlTable().getGroupingElements().contains(standardFunctionElement.getElementExpression())) {
                return operating(mqlDataStorage, rOperation, standardFunctionElement);
            } else {
                throw new RuntimeException("Can't Group function Operating!");
            }
        } else {
            throw new RuntimeException("Can't Group function Operating!");
        }

    }

    @Override
    public MQLDataStorage operate(ValueElement standardValueElement, RelationalOperation rOperation, MQLDataStorage mqlDataStorage) {
        if (isGrouped(mqlDataStorage)) {
            return operating(mqlDataStorage, rOperation, standardValueElement);
        } else {
            throw new RuntimeException("Can't Group function Operating!");
        }
    }

    @Override
    public MQLDataStorage operate(GroupFunctionElement standardGroupFunctionElement, RelationalOperation rOperation, MQLDataStorage mqlDataStorage) {
        if (isGrouped(mqlDataStorage)) {
            return operating(mqlDataStorage, rOperation, standardGroupFunctionElement);
        } else {
            throw new RuntimeException("Can't Group function Operating!");
        }
    }

    private boolean isGrouped(MQLDataStorage dataStorage) {
        return dataStorage.getMqlTable().isGrouped();
    }

    private MQLDataStorage operating(MQLDataStorage mqlDataStorage, RelationalOperation rOperation, MQLElement element) {

        MQLTable table = new MQLTable(mqlDataStorage.getMqlTable());
        MQLDataSource mqlDataSource = mqlDataStorage.getMqlDataSource();
        List<Map<String, Object>> operatedTableData = new ArrayList<>();
        List<Integer> updatedGroupingIdx = new ArrayList<>();

        int start = 0;
        int skipCount = 0;
        for (int end : table.getGroupingIdxs()) {
            Object value = compareValue(mqlDataStorage, start, end, element);
            Object functionResult = compareTargetGroupFunction.executeAbout(start, end, mqlDataStorage);
            if (rOperation.operating(value, functionResult)) {
                operatedTableData.addAll(table.getTableData().subList(start, end + 1));
                updatedGroupingIdx.add(end - skipCount);
            } else {
                skipCount = skipCount + (end - start + 1);
            }

            start = end + 1;
        }

        table.setTableData(operatedTableData);
        table.setGroupingIdx(updatedGroupingIdx);
        return new MQLDataStorage(mqlDataSource, table);
    }

    private Object compareValue(MQLDataStorage mqlDataStorage, int start, int end, MQLElement element) {
        if (element instanceof ColumnElement) {
            return mqlDataStorage.getMqlTable().getTableData().get(end).get(((ColumnElement) element).getColumnName());
        } else if (element instanceof ValueElement) {
            return ((ValueElement) element).getValue();
        } else if (element instanceof SingleRowFunctionElement) {
            Map<String, Object> row = mqlDataStorage.getMqlTable().getTableData().get(end);
            if (((SingleRowFunctionElement) element).hasColumn()) {
                return row.containsKey(element.getElementExpression())
                        ? row.get(element.getElementExpression())
                        : ((SingleRowFunctionElement) element).executeAbout(row);
            } else {
                return ((SingleRowFunctionElement) element).executeAbout(new HashMap<>());
            }
        } else if (element instanceof GroupFunctionElement) {
            return ((GroupFunctionElement) element).executeAbout(start, end, mqlDataStorage);
        } else {
            throw new RuntimeException("Not Valid Group Function Operating Element!");
        }


    }


}
