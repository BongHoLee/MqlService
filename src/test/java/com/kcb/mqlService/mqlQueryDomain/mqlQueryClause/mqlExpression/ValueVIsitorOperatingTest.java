package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.mqlExpression;


import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.ColumnOperandExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.MQLOperandExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.MQLOperatingExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.SingleRowFunctionOperandExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.ColumnElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.ValueElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.singleRowFunction.LENGTH;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.singleRowFunction.SUBSTR;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.logicalOperator.ANDOperator;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.logicalOperator.OROperator;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.operatingVisitor.WithValueTargetOperating;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.relationalOperator.RelationalOperator;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.FromClause;
import com.kcb.mqlService.testData.TestDataFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;


public class ValueVIsitorOperatingTest {
    private MQLDataStorage mqlDataStorage;

    @Before
    public void makeMqlDataSource() {
        MQLDataSource mqlDataSource = new MQLDataSource();

        Map<String, List<Map<String, Object>>> rawDataSource = new HashMap<>();
        rawDataSource.put("A", TestDataFactory.tableOf("categories"));
        rawDataSource.put("B", TestDataFactory.tableOf("employees"));
        rawDataSource.put("C", TestDataFactory.tableOf("shippers"));
        rawDataSource.put("D", TestDataFactory.tableOf("test"));


        FromClause from = new FromClause();
        from.addDataSourceIds("A", "B", "C", "D");
        mqlDataSource = from.makeMqlDataSources(rawDataSource);

        mqlDataStorage = new MQLDataStorage(mqlDataSource, new MQLTable());
    }

    /**
     * A.CategoryID = 1
     */
    @Test
    public void columnWithValueEqualToOperatingTest() {
        String column = "A.CategoryID";
        int value = 1;

        MQLOperandExpression operandExpression = new ColumnOperandExpression(
                new ColumnElement(column),
                RelationalOperator::equalTo,
                new WithValueTargetOperating(
                        new ValueElement(value)
                )
        );

        MQLDataStorage result = operandExpression.operatingWith(mqlDataStorage);

        result.getMqlTable().getTableData().forEach(eachRow -> {
            assertThat(value, is(equalTo(eachRow.get(column))));
        });
    }

    /**
     *
     * A.CategoryID=1 AND B.EmployeeID= 2
     */
    @Test
    public void columnWithValueANDcolumnWithValueEqualToTest() {
        MQLOperatingExpression andOperator = new ANDOperator(
                new ColumnOperandExpression(
                        new ColumnElement("A.CategoryID"),
                        RelationalOperator::equalTo,
                        new WithValueTargetOperating(
                                new ValueElement(1)

                        )
                ),
                new ColumnOperandExpression(
                        new ColumnElement("B.EmployeeID"),
                        RelationalOperator::equalTo,
                        new WithValueTargetOperating(
                                new ValueElement(2)

                        )
                )
        );
        MQLDataStorage result = andOperator.operatingWith(mqlDataStorage);
        result.getMqlTable().getTableData().forEach(eachRow -> {
            assertThat(true, allOf(
                    is(equalTo(eachRow.get("A.CategoryID").equals(1))),
                    is(equalTo(eachRow.get("B.EmployeeID").equals(2)))
            ));
        });
    }

    /**
     *
     * A.CategoryID=1 OR B.EmployeeID= 2
     */
    @Test
    public void columnWithValueORcolumnWithValueEqualToTest() {
        MQLOperatingExpression orOperator = new OROperator(
                new ColumnOperandExpression(
                        new ColumnElement("A.CategoryID"),
                        RelationalOperator::equalTo,
                        new WithValueTargetOperating(
                                new ValueElement(1)

                        )
                ),
                new ColumnOperandExpression(
                        new ColumnElement("B.EmployeeID"),
                        RelationalOperator::equalTo,
                        new WithValueTargetOperating(
                                new ValueElement(2)

                        )
                )
        );
        MQLDataStorage result = orOperator.operatingWith(mqlDataStorage);
        result.getMqlTable().getTableData().forEach(eachRow -> {
            assertThat(true, anyOf(
                    is(equalTo(eachRow.get("A.CategoryID").equals(1))),
                    is(equalTo(eachRow.get("B.EmployeeID").equals(2)))
            ));
        });
    }

    /**
     * LENGTH(SUBSTR(B.FirstName, 1, 3)) > 1
     *
     */
    @Test
    public void substrTest() {
        MQLOperandExpression expression = new SingleRowFunctionOperandExpression(
                new LENGTH(new SUBSTR(new ColumnElement("B.FirstName"), new ValueElement(1), new ValueElement(3))),
                RelationalOperator::largerThan,
                new WithValueTargetOperating(
                        new ValueElement(1)
                )
        );

        MQLDataStorage result = expression.operatingWith(mqlDataStorage);
        print(result);
    }


    public void print(MQLDataStorage mqlDataStorage) {
        System.out.println(mqlDataStorage.getMqlTable().getJoinSet());
        System.out.println(mqlDataStorage.getMqlTable().getTableData());
        System.out.println(mqlDataStorage.getMqlTable().getTableData().size());
    }



}
