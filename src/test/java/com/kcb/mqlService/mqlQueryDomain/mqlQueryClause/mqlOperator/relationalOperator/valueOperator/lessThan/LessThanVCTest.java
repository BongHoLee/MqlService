package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.mqlOperator.relationalOperator.valueOperator.lessThan;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ColumnOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.NumberValueOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.MQLOperator;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.relationalOperator.valueOperator.lessThan.LessThanCV;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.relationalOperator.valueOperator.lessThan.LessThanVC;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.FromClause;
import com.kcb.mqlService.testData.TestDataFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;

public class LessThanVCTest {

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
     * 2 <= A.CategoryID
     *
     */
    @Test
    public void valueLessThanColumnTest() {
        String columnExpression = "A.CategoryID";
        int valueExpression = 2;

        MQLOperator operator = new LessThanVC(
                new NumberValueOperand(valueExpression),
                new ColumnOperand(columnExpression)
        );

        MQLTable table = operator.operateWith(mqlDataSource);
        List<Map<String, Object>> tableData = table.getTableData();

        tableData.forEach(eachRow -> {
            assertThat(2, is(lessThan((int)eachRow.get("A.CategoryID"))));
        });


    }

}
