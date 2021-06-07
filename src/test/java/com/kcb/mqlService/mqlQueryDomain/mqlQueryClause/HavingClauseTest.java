package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.ColumnOperandExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.ValueOperandExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.ColumnElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.MQLElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.ValueElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.groupFunction.SUM;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.singleRowFunction.LENGTH;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.operatingVisitor.WithGroupFunctionTargetOperating;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.relationalOperator.RelationalOperator;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.optionalClause.GroupByClause;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.optionalClause.HavingClause;
import com.kcb.mqlService.testData.TestDataFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

public class HavingClauseTest {
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
        mqlDataStorage = from.makeMqlDataSources(rawDataSource);
    }

    /**
     * SELECT E.SupplierID, SUM(LENGTH(E.ProductName))
     * FROM Products E
     * GROUP BY E.SupplierID
     * HAVING E.SupplierID < SUM(LENGTH(E.ProductName))
     *
     */
    @Test
    public void havingWithSumAndColumnTest() {
        mqlDataStorage.setMqlTable(
                new MQLTable(
                        new HashSet<>(Collections.singletonList("E")),
                        mqlDataStorage.getMqlDataSource().dataSourceOf("E")
                )
        );

        String column1 = "E.SupplierID";

        List<MQLElement> groupingElements = new ArrayList<>();
        groupingElements.add(new ColumnElement(column1));
        GroupByClause groupBy = new GroupByClause(groupingElements);

        HavingClause having = new HavingClause(
                new ColumnOperandExpression(
                        new ColumnElement(column1),
                        RelationalOperator::lessThan,
                        new WithGroupFunctionTargetOperating(new SUM(new LENGTH(new ColumnElement("E.ProductName"))))
                )
        );

        MQLDataStorage result = having.executeClause(groupBy.executeClause(mqlDataStorage));
        print(result);
    }

    /**
     * SELECT E.SupplierID, SUM(LENGTH(E.ProductName))
     * FROM Products E
     * GROUP BY E.SupplierID
     * HAVING 40 < SUM(LENGTH(E.ProductName))
     *
     */
    @Test
    public void havingWithSumAndValueTest() {
        mqlDataStorage.setMqlTable(
                new MQLTable(
                        new HashSet<>(Collections.singletonList("E")),
                        mqlDataStorage.getMqlDataSource().dataSourceOf("E")
                )
        );

        String column1 = "E.SupplierID";

        List<MQLElement> groupingElements = new ArrayList<>();
        groupingElements.add(new ColumnElement(column1));
        GroupByClause groupBy = new GroupByClause(groupingElements);

        HavingClause having = new HavingClause(
                new ValueOperandExpression(
                        new ValueElement(40),
                        RelationalOperator::lessThan,
                        new WithGroupFunctionTargetOperating(
                                new SUM(new LENGTH(new ColumnElement("E.ProductName")))
                        )
                )
        );

        MQLDataStorage result = having.executeClause(groupBy.executeClause(mqlDataStorage));
        print(result);
    }



    public void print(MQLDataStorage mqlDataStorage) {
        System.out.println(mqlDataStorage.getMqlTable().getJoinSet());
        System.out.println(mqlDataStorage.getMqlTable().getTableData());
        System.out.println(mqlDataStorage.getMqlTable().getTableData().size());
        System.out.println(mqlDataStorage.getMqlTable().getGroupingIdxs());
        System.out.println(mqlDataStorage.getMqlTable().getGroupingIdxs().size());
    }
}
