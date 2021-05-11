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
public class MQLGroupByClaus {
    private List<String> groupingElements;

    public MQLGroupByClaus(String ... elements) {
        groupingElements = new ArrayList<>(Arrays.asList(elements));
    }

    public MQLTable groupingWith(MQLTable table) {
        List<Map<String, Object>> tableData = table.getTableData();
        Map<String, Object> map = tableData.stream().collect(grouping(eachRow -> eachRow.get(groupingElements.get(0)), groupingElements, 1));


        return null;
    }

    private <T, D>
    Collector<T, ?, D> grouping (
            Function<Map<String, Object>, Object> classifier,
            List<String> elements,
            int idx
    ) {

        if (elements.size() > idx ) {

            Function<Map<String, Object>, Object> newClassifier = eachRow -> eachRow.get(elements.get(idx));
            return  (Collector<T, ?, D>) Collectors.groupingBy(classifier, grouping(newClassifier, elements, idx + 1));
        }else {
            return (Collector<T, ?, D>) Collectors.groupingBy(classifier);
        }
    }


}
