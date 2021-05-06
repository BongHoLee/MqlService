package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.mqlOperator.relationalOperator.valueOperator.lessThanEqualTo;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ColumnOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.NumberValueOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.joinOperator.MQLJoinOperator;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.joinOperator.NoneToJoinOperator;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.whereOperator.MQLWhereOperator;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.whereOperator.lessThanEqualTo.LessThanEqualToVC;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.FromClause;
import com.kcb.mqlService.testData.TestDataFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

public class LessThanEqualToVCTest {

    private MQLDataSource mqlDataSource = new MQLDataSource();

    @Before
    public void makeMqlDataSource() {
        Map<String, List<Map<String, Object>>> rawDataSource = new HashMap<>();
        rawDataSource.put("A", TestDataFactory.tableOf("categories"));

        FromClause from = new FromClause();
        from.addDataSourceIds("A");
        mqlDataSource = from.makeMqlDataSources(rawDataSource);
    }

    /**
     * 2 <= A.CategoryID
     *
     */
    @Test
    public void valueLessThanEqualToColumnTest() {
        MQLJoinOperator noneJoin = new NoneToJoinOperator();
        String columnExpression = "A.CategoryID";
        int valueExpression = 2;

        MQLWhereOperator operator = new LessThanEqualToVC(
                new NumberValueOperand(valueExpression),
                new ColumnOperand(columnExpression)
        );

        MQLTable table = operator.operateWith(noneJoin.operateWith(mqlDataSource));
        List<Map<String, Object>> tableData = table.getTableData();

        tableData.forEach(eachRow -> {
            assertThat(2, is(lessThanOrEqualTo((int)eachRow.get("A.CategoryID"))));
        });


    }
}
