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
            return (Collector<T, ?, D>) Collectors.groupingBy(classifier, Collectors.summarizingDouble(function));
        }
    }

}
