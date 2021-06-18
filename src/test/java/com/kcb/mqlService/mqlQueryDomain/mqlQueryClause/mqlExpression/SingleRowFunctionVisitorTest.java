package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.mqlExpression;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.ColumnOperandExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.MQLOperandExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.ColumnElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.ValueElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.singleRowFunction.LENGTH;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.operatingVisitor.WithSingleRowFunctionTargetOperating;
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
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

public class SingleRowFunctionVisitorTest {

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
        mqlDataStorage = from.makeMqlDataSources(rawDataSource);
    }

    /**
     * B.EmployeeID >= LENGTH(A.CategoryName)
     */
    @Test
    public void columnWithSingleRowFunctionHasColumnTest() {
        String leftColumn = "B.EmployeeID";
        String rightColumn = "A.CategoryName";

        MQLOperandExpression expression = new ColumnOperandExpression(
                new ColumnElement(leftColumn),
                RelationalOperator::largerThanEqualTo,
                new WithSingleRowFunctionTargetOperating(
                        new LENGTH(new ColumnElement(rightColumn))

                )
        );

        MQLDataStorage result = expression.operatingWith(mqlDataStorage);

        for (Map<String, Object> eachRow : result.getMqlTable().getTableData()) {
            assertThat(
                    (((Double)eachRow.get(leftColumn)).intValue()),
                    is(greaterThanOrEqualTo(((String) eachRow.get(rightColumn)).length())));
        }

    }

    /**
     *
     *
     * A.CategoryID > LENGTH("333")
     */
    @Test
    public void columnWithSingleRowFunctionHasValueTest() {
        String column = "A.CategoryID";
        String value = "333";
        MQLOperandExpression expression = new ColumnOperandExpression(
                new ColumnElement(column),
                RelationalOperator::largerThan,
                new WithSingleRowFunctionTargetOperating(
                        new LENGTH(new ValueElement(value))
                )
        );

        MQLDataStorage result = expression.operatingWith(mqlDataStorage);

        for (Map<String, Object> eachRow : result.getMqlTable().getTableData()) {
            assertThat(
                    (((Double) eachRow.get(column)).intValue()),
                    is(greaterThanOrEqualTo(value.length())));
        }
    }

    public void print(MQLDataStorage mqlDataStorage) {
        System.out.println(mqlDataStorage.getMqlTable().getJoinSet());
        System.out.println(mqlDataStorage.getMqlTable().getTableData());
        System.out.println(mqlDataStorage.getMqlTable().getTableData().size());
    }



}
