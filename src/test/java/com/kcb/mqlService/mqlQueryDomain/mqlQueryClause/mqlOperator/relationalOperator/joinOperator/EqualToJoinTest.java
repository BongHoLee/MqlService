package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.mqlOperator.relationalOperator.joinOperator;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ColumnOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.joinOperator.MQLJoinOperator;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.joinOperator.EqualToJoin;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.FromClause;
import com.kcb.mqlService.testData.TestDataFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class EqualToJoinTest {

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

    @Test
    public void StringEqualToStringTest() {
        String leftColumn = "A.CategoryID";
        String rightColumn = "B.EmployeeID";
        MQLJoinOperator operator = new EqualToJoin(
                new ColumnOperand(leftColumn),
                new ColumnOperand(rightColumn)
        );

        MQLTable table = operator.operateWith(mqlDataSource);
        assertThat(table.getJoinSet(), equalTo(new HashSet<>(Arrays.asList("A", "B"))));
        table.getTableData().forEach(eachRow -> assertThat(eachRow.get(leftColumn), equalTo(eachRow.get(rightColumn))));

    }
}
