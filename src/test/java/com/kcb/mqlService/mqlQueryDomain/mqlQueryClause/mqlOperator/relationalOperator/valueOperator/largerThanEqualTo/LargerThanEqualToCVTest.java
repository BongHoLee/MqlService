package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.mqlOperator.relationalOperator.valueOperator.largerThanEqualTo;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ColumnOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.NumberValueOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.MQLOperator;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.relationalOperator.valueOperator.largerThan.LargerThanCV;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.FromClause;
import com.kcb.mqlService.testData.TestDataFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;

public class LargerThanEqualToCVTest {

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
     * A.CategoryID >= 2
     *
     */
    @Test
    public void columnLargerThanEqualToValueTest() {
        String columnExpression = "A.CategoryID";
        int valueExpression = 2;

        MQLOperator operator = new LargerThanCV(
                new ColumnOperand(columnExpression),
                new NumberValueOperand(valueExpression)
        );

        MQLTable table = operator.operateWith(mqlDataSource);
        List<Map<String, Object>> tableData = table.getTableData();

        tableData.forEach(eachRow -> {
            assertThat((int)eachRow.get("A.CategoryID"), is(greaterThanOrEqualTo(2)));
        });


    }
}
