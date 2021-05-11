package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 *
 * Deal With Aggregate Function
 */
public class GroupByClaus {
    private List<String> groupingElements;

    public GroupByClaus(String ... elements) {
        groupingElements = new ArrayList<>(Arrays.asList(elements));
    }

    public MQLTable groupingWith(MQLTable table) {

        List<Map<String, Object>> tableData = table.getTableData();
        tableData.sort((row1, row2) -> compare(row1, row2, groupingElements, 0));

        return null;
    }


    private int compare(Map<String, Object> row1, Map<String, Object> row2, List<String> elements, int idx) {
        if (elements.size() > idx) {
            String compareKey = elements.get(idx);

            if (row1.get(compareKey) instanceof String && row2.get(compareKey) instanceof  String) {
                int compareValue = ((String) row1.get(compareKey)).compareTo((String)row2.get(compareKey));

                if (compareValue != 0) {
                    return compareValue;
                } else {
                    return compare(row1, row2, elements, idx + 1);
                }

            } else if (row1.get(compareKey) instanceof Number && row2.get(compareKey) instanceof Number) {
                double compareValue = (((Number)row1.get(compareKey)).doubleValue()) - (((Number)row2.get(compareKey)).doubleValue());

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
