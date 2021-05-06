package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.mqlOperator.logicalOperator;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ColumnOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.StringValueOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.MQLOperator;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.logicalOperator.OrOperator;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.relationalOperator.joinOperator.EqualToJoin;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.relationalOperator.valueOperator.eqaulTo.EqualToCV;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.FromClause;
import com.kcb.mqlService.testData.TestDataFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class OrOperatorTest {

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
     * (A.CategoryID = B.EmployeeID) OR (A.CategoryName='Beverages')
     */
    @Test
    public void columnEqualColumnOrColumnEqualValueTest() {
        MQLOperator leftOperator = new EqualToJoin(
                new ColumnOperand("A.CategoryID"),
                new ColumnOperand("B.EmployeeID")
        );

        MQLOperator rightOperator = new EqualToCV(
                new ColumnOperand("A.CategoryName"),
                new StringValueOperand("Beverages")
        );

        MQLOperator orOperator = new OrOperator(
                leftOperator,
                rightOperator
        );

        MQLTable resultTable = orOperator.operateWith(mqlDataSource);
        List<Map<String, Object>> tableData = resultTable.getTableData();
        tableData.sort(Comparator.comparing(m -> (int)m.get("A.CategoryID")));

        assertThat(17, is(equalTo(tableData.size())));
        tableData.forEach(eachRow ->  {
            assertThat(true, anyOf(
                    equalTo((int)eachRow.get("A.CategoryID")==(int)eachRow.get("B.EmployeeID")),
                    equalTo(eachRow.get("A.CategoryName").equals("Beverages"))
                    ));
        });
    }
}
