package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.optionalClause;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.ColumnElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.MQLElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.SingleRowFunctionElement;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.OptionalClause;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 *
 */
public class GroupByClause implements OptionalClause {
    private List<MQLElement> groupingElements = new ArrayList<>();

    public GroupByClause(MQLElement... groupingElements) {
        this.groupingElements = new ArrayList<>(Arrays.asList(groupingElements));
    }

    public GroupByClause(List<MQLElement> groupingElements) {
        this.groupingElements = groupingElements;
    }

    public GroupByClause() {
        this.groupingElements = new ArrayList<>();
    }

    @Override
    public MQLDataStorage executeClause(MQLDataStorage mqlDataStorage) {
        return groupingWith(mqlDataStorage);
    }

    private MQLDataStorage groupingWith(MQLDataStorage mqlDataStorage) {
        MQLTable table = updateTable(mqlDataStorage.getMqlTable());
        return new MQLDataStorage(mqlDataStorage.getMqlDataSource(), table);
    }

    private MQLTable updateTable(MQLTable table) {
        MQLTable updatedTable = new MQLTable(table);
        if (!groupingElements.isEmpty()) {
            updatedTable.setGrouped(true);
            updatedTable.setTableData(updateTableData(table.getTableData()));
            updatedTable.getTableData().sort((row1, row2) -> compare(row1, row2, groupingElements, 0));
            updatedTable.setGroupingElements(updateGroupingElements());

        } else {
            updatedTable.setGrouped(false);
        }

        return updatedTable;
    }


    // if Grouping Elements contains singleRowFunction(ex: LENGTH(A.ID)), add to tableData's column
    private List<Map<String, Object>> updateTableData(List<Map<String, Object>> origin) {
        List<Map<String, Object>> updatedTableData = new ArrayList<>(origin);

        groupingElements.forEach(element -> {
            if (element instanceof SingleRowFunctionElement) {
                updatedTableData.forEach(eachRow -> {
                    eachRow.put(element.getElementExpression(), ((SingleRowFunctionElement) element).executeAbout(eachRow));
                });
            }
        });

        return updatedTableData;
    }

    // update MQLTable's grouping elements to contains only column
    private List<String> updateGroupingElements() {
        List<String> updatedElements = new ArrayList<>();

        groupingElements.forEach(element -> {
            updatedElements.add(element.getElementExpression());
        });

        return updatedElements;
    }

    private int compare(Map<String, Object> row1, Map<String, Object> row2, List<MQLElement> elements, int idx) {

        if (elements.size() > idx) {
            MQLElement element = elements.get(idx);
            String compareColumn = element.getElementExpression();

            if (row1.get(compareColumn) instanceof String && row2.get(compareColumn) instanceof String) {
                int compareValue = ((String) row1.get(compareColumn)).compareTo((String) row2.get(compareColumn));

                if (compareValue != 0) {
                    return compareValue;
                } else {
                    return compare(row1, row2, elements, idx + 1);
                }

            } else if (row1.get(compareColumn) instanceof Number && row2.get(compareColumn) instanceof Number) {
                double compareValue = (((Number) row1.get(compareColumn)).doubleValue()) - (((Number) row2.get(compareColumn)).doubleValue());

                if (compareValue > 0) {
                    return 1;
                } else if (compareValue < 0) {
                    return -1;
                } else {
                    return compare(row1, row2, elements, idx + 1);
                }
            } else {
                throw new RuntimeException("Not Matched Type!");
            }


        } else {
            return 0;
        }
    }


}
