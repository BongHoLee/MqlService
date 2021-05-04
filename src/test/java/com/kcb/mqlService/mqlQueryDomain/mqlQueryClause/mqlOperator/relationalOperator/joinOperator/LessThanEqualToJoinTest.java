package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.mqlOperator.relationalOperator.joinOperator;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ColumnOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.MQLOperator;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.relationalOperator.joinOperator.LessThanEqualToJoin;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.relationalOperator.joinOperator.LessThanJoin;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.FromClause;
import com.kcb.mqlService.testData.TestDataFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

public class LessThanEqualToJoinTest {


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

    /**
     * A.CategoryID < B.EmployeeID
     */
    @Test
    public void lessThanEqualToJoinTest() {
        String leftExpression= "A.CategoryID";
        String rightExpression = "B.EmployeeID";

        MQLOperator operator = new LessThanEqualToJoin(
                new ColumnOperand(leftExpression),
                new ColumnOperand(rightExpression)
        );

        MQLTable table = operator.operateWith(mqlDataSource);

        table.getTableData().forEach(eachRow ->{
            assertThat(((int)eachRow.get(leftExpression)), lessThanOrEqualTo((int)eachRow.get(rightExpression)));
        });
    }
}
