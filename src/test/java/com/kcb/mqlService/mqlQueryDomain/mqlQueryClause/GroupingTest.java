package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.testData.TestDataFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.greaterThan;

public class GroupingTest {
    private MQLDataSource mqlDataSource = new MQLDataSource();

    @Before
    public void makeMqlDataSource() {
        Map<String, List<Map<String, Object>>> rawDataSource = new HashMap<>();
        rawDataSource.put("A", TestDataFactory.tableOf("test"));

        FromClause from = new FromClause();
        from.addDataSourceIds("A");
        mqlDataSource = from.makeMqlDataSources(rawDataSource);
    }

    /**
     * GROUP BY A.CategoryName, A.Description
     */
    @Test
    public void groupingTest() {
        List<String> elements = Arrays.asList("A.CategoryName","A.Description");

        List<Map<String, Object>> mql = mqlDataSource.dataSourceOf("A");
        Map<String, Object> map = mql.stream().collect(grouping(eachRow -> eachRow.get(elements.get(0)), elements, 1));
        System.out.println(map);
    }

    public <T, D>
    Collector<T, ?, D>  grouping (
            Function<Map<String, Object>, Object> classifier,
            List<String> elements,
            int idx
    ) {

        if (elements.size() > idx ) {
            Function<Map<String, Object>, Object> newClassifier = eachRow -> eachRow.get(elements.get(idx));
            return  (Collector<T, ?, D>) Collectors.groupingBy(classifier, grouping(newClassifier, elements, idx + 1));
        }else {
            ToDoubleFunction<Map<String, Object>> function = eachRow -> ((Number)eachRow.get("A.Description")).doubleValue();

            //return
            return (Collector<T, ?, D>) Collectors.groupingBy(classifier, Collectors.summarizingDouble(function));
        }
    }

    @Test
    public void orderingForGroupingTest() {
        List<String> elements = Arrays.asList("A.CategoryName","A.Description");

        List<Map<String, Object>> tableData = mqlDataSource.dataSourceOf("A");
        System.out.println(tableData);

        tableData.sort((row1, row2) -> compare(row1, row2, elements, 0));
        System.out.println(tableData);


    }

    public int compare(Map<String, Object> row1, Map<String, Object> row2, List<String> elements, int idx) {
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
