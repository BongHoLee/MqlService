package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.mqlExpression;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.ColumnOperandExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.MQLOperandExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.ColumnElement;
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
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;

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
        from.addDataSourceIds("A", "B", "C", "D");
        mqlDataSource = from.makeMqlDataSources(rawDataSource);

        mqlDataStorage = new MQLDataStorage(mqlDataSource, new MQLTable());
    }

    /**
     * B.EmployeeID >= LENGTH(A.CategoryName)
     */
    @Test
    public void columnWithSingleRowFunctionTest() {
        MQLOperandExpression expression = new ColumnOperandExpression(
                new ColumnElement("B.EmployeeID"),
                new WithSingleRowFunctionTargetOperating(
                        new LENGTH(new ColumnElement("A.CategoryName")),
                        RelationalOperator::largerThanEqualTo
                )
        );

    }


}
