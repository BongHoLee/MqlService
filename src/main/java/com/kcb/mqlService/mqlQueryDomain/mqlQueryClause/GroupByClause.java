package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.ColumnElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.MQLElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.SingleRowFunctionElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 *
 */
public class GroupByClause {
    private List<MQLElement> groupingElements;

    public GroupByClause(MQLElement... groupingElements) {
        this.groupingElements = new ArrayList<>(Arrays.asList(groupingElements));
    }

    public GroupByClause() {
        this.groupingElements = new ArrayList<>();
    }

    public MQLTable groupingWith(MQLTable table) {

        List<Map<String, Object>> tableData = new ArrayList<>(table.getTableData());

        tableData.sort((row1, row2) -> compare(row1, row2, groupingElements, 0));

        return new MQLTable(table.getJoinSet(), tableData);
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
