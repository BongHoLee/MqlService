package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.mqlOperator.relationalOperator.joinOperator;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ColumnOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.joinOperator.MQLJoinOperator;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.joinOperator.LargerThanJoin;
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


public class LargerThanJoinTest {

    private MQLDataSource mqlDataSource = new MQLDataSource();

    @Before
    public void makeMqlDataSource() {
        Map<String, List<Map<String, Object>>> rawDataSource = new HashMap<>();
        rawDataSource.put("A", TestDataFactory.tableOf("categories"));
        rawDataSource.put("B", TestDataFactory.tableOf("employees"));
        rawDataSource.put("C", TestDataFactory.tableOf("shippers"));

        FromClause from = new FromClause();
        from.addDataSourceIds("A", "B", "C");
        mqlDataSource = from.makeMqlDataSources(rawDataSource);
    }

    // A.CategoryID > B.EmployeeID
    @Test
    public void integerLargerThanJoinTest() {
        String leftExpression= "A.CategoryID";
        String rightExpression = "B.EmployeeID";

        MQLJoinOperator operator = new LargerThanJoin(
                new ColumnOperand(leftExpression),
                new ColumnOperand(rightExpression)
        );

        MQLTable table = operator.operateWith(mqlDataSource);

        assertThat(28, is(table.getTableData().size()));
        table.getTableData().forEach(eachRow ->{
            assertThat(((int)eachRow.get(leftExpression)), greaterThan((int)eachRow.get(rightExpression)));
        });

    }


}
