package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.ColumnOperandExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.ColumnElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.operatingVisitor.WithColumnTargetOperating;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.relationalOperator.RelationalOperator;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.optionalClause.GeneralConditionClause;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.optionalClause.GroupByClause;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.optionalClause.JoinClause;
import com.kcb.mqlService.testData.TestDataFactory;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

public class GeneralConditionClauseTest {
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
     *
     *  FROM Categories A
     *  JOIN Employees B ON A.CategoryID = B.EmployeeID
     */
    @Test
    public void singleEquiJoinTest() {
        String column1 = "A.CategoryID";
        String column2 = "B.EmployeeID";

        JoinClause joinClause = new JoinClause(
                new ColumnOperandExpression(
                        new ColumnElement(column1),
                        RelationalOperator::equalTo,
                        new WithColumnTargetOperating(new ColumnElement(column2))
                        )
        );

        MQLDataStorage result = joinClause.executeClause(mqlDataStorage);

        result.getMqlTable().getTableData().forEach(eachRow -> {
            assertThat(eachRow.get(column1), is(equalTo(eachRow.get(column2))));
        });

    }

    /**
     *
     *  FROM Categories A
     *  JOIN Employees B ON A.CategoryID = B.EmployeeID
     */

    @Test
    public void singleNotEquiJoinTest() {
        String column1 = "A.CategoryID";
        String column2 = "B.EmployeeID";

        JoinClause join = new JoinClause(
                new ColumnOperandExpression(
                        new ColumnElement(column1),
                        RelationalOperator::largerThan,
                        new WithColumnTargetOperating(
                                new ColumnElement(column2)
                        ))
        );

        MQLDataStorage result = join.executeClause(mqlDataStorage);
        result.getMqlTable().getTableData().forEach(eachRow -> {
            assertThat((int)eachRow.get(column1), is(greaterThan((int)eachRow.get(column2))));
        });
    }

    /**
     *
     * FROM Categories A
     * JOIN Employees B ON A.CategoryID = B.EmployeeID
     * JOIN Products E ON A.CategoryID = E.SupplierID
     */
    @Test
    public void multipleEuqiJoinTest() {
        String column1 = "A.CategoryID";
        String column2 = "B.EmployeeID";
        String column3 = "E.SupplierID";

        GeneralConditionClause multiJoin = new GeneralConditionClause(
                new JoinClause(new ColumnOperandExpression(new ColumnElement(column1), RelationalOperator::equalTo, new WithColumnTargetOperating(new ColumnElement(column2)))),
                new JoinClause(new ColumnOperandExpression(new ColumnElement(column1), RelationalOperator::equalTo, new WithColumnTargetOperating(new ColumnElement(column3))))
        );

        MQLDataStorage result = multiJoin.executeClause(mqlDataStorage);
        result.getMqlTable().getTableData().forEach(eachRow -> {
            assertThat((int)eachRow.get(column1), allOf(is(equalTo((int)eachRow.get(column2))), is(equalTo((int)eachRow.get(column3)))));
        });

    }

    /**
     *
     * FROM Categories A
     * JOIN Employees B ON A.CategoryID = B.EmployeeID
     * JOIN Products E ON A.CategoryID > E.ProductID
     */
    @Test
    public void multipleNotEquiJoinTest() {
        String column1 = "A.CategoryID";
        String column2 = "B.EmployeeID";
        String column3 = "E.ProductID";

        GeneralConditionClause multiJoin = new GeneralConditionClause(
                new JoinClause(new ColumnOperandExpression(new ColumnElement(column1), RelationalOperator::equalTo, new WithColumnTargetOperating(new ColumnElement(column2)))),
                new JoinClause(new ColumnOperandExpression(new ColumnElement(column1), RelationalOperator::largerThan, new WithColumnTargetOperating(new ColumnElement(column3))))
        );

        MQLDataStorage result = multiJoin.executeClause(mqlDataStorage);
        List<Map<String, Object>> tmp = result.getMqlTable().getTableData().stream()
                .map(eachRow -> {
                    Map<String, Object> tmpMap = new HashMap<>();
                    tmpMap.put(column1, eachRow.get(column1));
                    tmpMap.put(column2, eachRow.get(column2));
                    tmpMap.put(column3, eachRow.get(column3));

                    return tmpMap;
                }).collect(Collectors.toList());

        result.getMqlTable().setTableData(tmp);

        print(result);

    }

    /**
     *
     * FROM Categories A
     * JOIN Employees B ON A.CategoryID = B.EmployeeID
     * JOIN Products E ON A.CategoryID > E.ProductID
     * GROUP BY E.SupplierID
     */
    @Test
    public void multipleJoinWithGroupBy() {
        String column1 = "A.CategoryID";
        String column2 = "B.EmployeeID";
        String column3 = "E.ProductID";
        String groupByColumn = "E.SupplierID";

        GeneralConditionClause multiJoin = new GeneralConditionClause(
                new JoinClause(new ColumnOperandExpression(new ColumnElement(column1), RelationalOperator::equalTo, new WithColumnTargetOperating(new ColumnElement(column2)))),
                new JoinClause(new ColumnOperandExpression(new ColumnElement(column1), RelationalOperator::largerThan, new WithColumnTargetOperating(new ColumnElement(column3))))
        );

        GroupByClause groupByClause = new GroupByClause(new ColumnElement(groupByColumn), new ColumnElement(column3));

        MQLDataStorage result = groupByClause.executeClause(multiJoin.executeClause(mqlDataStorage));
        List<Map<String, Object>> tmp = result.getMqlTable().getTableData().stream()
                .map(eachRow -> {
                    Map<String, Object> tmpMap = new HashMap<>();
                    tmpMap.put(column1, eachRow.get(column1));
                    tmpMap.put(column2, eachRow.get(column2));
                    tmpMap.put(column3, eachRow.get(column3));
                    tmpMap.put(groupByColumn, eachRow.get(groupByColumn));

                    return tmpMap;
                }).collect(Collectors.toList());

        result.getMqlTable().setTableData(tmp);

        print(result);

    }

    public void print(MQLDataStorage mqlDataStorage) {
        System.out.println(mqlDataStorage.getMqlTable().getJoinSet());
        System.out.println(mqlDataStorage.getMqlTable().getTableData());
        System.out.println(mqlDataStorage.getMqlTable().getTableData().size());
    }
}
