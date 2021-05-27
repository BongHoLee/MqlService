package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.ColumnElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.MQLElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.singleRowFunction.LENGTH;
import com.kcb.mqlService.testData.TestDataFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

public class GroupByClauseTest {
    private MQLDataStorage mqlDataStorage;

    @Before
    public void makeMqlDataSource() {
        MQLDataSource mqlDataSource = new MQLDataSource();

        Map<String, List<Map<String, Object>>> rawDataSource = new HashMap<>();
        rawDataSource.put("A", TestDataFactory.tableOf("categories"));
        rawDataSource.put("B", TestDataFactory.tableOf("employees"));
        rawDataSource.put("C", TestDataFactory.tableOf("shippers"));
        rawDataSource.put("D", TestDataFactory.tableOf("test"));
        rawDataSource.put("E", TestDataFactory.tableOf("products"));


        FromClause from = new FromClause();
        from.addDataSourceIds("A", "B", "C", "D", "E");
        mqlDataSource = from.makeMqlDataSources(rawDataSource);

        mqlDataStorage = new MQLDataStorage(mqlDataSource, new MQLTable());
    }

    /**
     * GROUP BY E.SupplierID, E.CategoryID
     *
     */
    @Test
    public void groupingOnlyColumnTest() {
        mqlDataStorage.setMqlTable(
                new MQLTable(
                        new HashSet<>(Collections.singletonList("E")),
                        mqlDataStorage.getMqlDataSource().dataSourceOf("E")
                )
        );

        String column1 = "E.SupplierID";
        String column2 = "E.CategoryID";

        List<MQLElement> groupingElements = new ArrayList<>();
        groupingElements.add(new ColumnElement(column1));
        groupingElements.add(new ColumnElement(column2));

        GroupByClause groupBy = new GroupByClause(groupingElements);

        MQLDataStorage result = groupBy.groupingWith(mqlDataStorage);
        print(result);

    }

    /**
     * GROUP BY E.SupplierID, LENGTH(E.Price)
     */
    @Test
    public void groupingColumnAndFunctionTest() {
        mqlDataStorage.setMqlTable(
                new MQLTable(
                        new HashSet<>(Collections.singletonList("E")),
                        mqlDataStorage.getMqlDataSource().dataSourceOf("E")
                )
        );

        String column1 = "E.SupplierID";
        String column2 = "E.Price";

        List<MQLElement> groupingElements = new ArrayList<>();
        groupingElements.add(new ColumnElement(column1));
        groupingElements.add(new LENGTH(new ColumnElement(column2)));

        GroupByClause groupBy = new GroupByClause(groupingElements);

        MQLDataStorage result = groupBy.groupingWith(mqlDataStorage);
        result.getMqlTable().getTableData().forEach(eachRow ->{
            eachRow.remove("E.CategoryID");
            eachRow.remove("E.Unit");
            eachRow.remove("E.ProductID");
            eachRow.remove("E.ProductName");
        });
        print(result);
    }

    public void print(MQLDataStorage mqlDataStorage) {
        System.out.println(mqlDataStorage.getMqlTable().getJoinSet());
        System.out.println(mqlDataStorage.getMqlTable().getTableData());
        System.out.println(mqlDataStorage.getMqlTable().getTableData().size());
    }
}
