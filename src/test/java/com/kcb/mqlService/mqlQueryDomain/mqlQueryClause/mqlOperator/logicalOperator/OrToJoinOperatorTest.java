package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.mqlOperator.logicalOperator;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ColumnOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.StringValueOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.joinOperator.MQLJoinOperator;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.joinOperator.logicalOperator.OrToJoinOperator;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.joinOperator.EqualToJoin;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.whereOperator.MQLWhereOperator;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.whereOperator.eqaulTo.EqualToCV;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.FromClause;
import com.kcb.mqlService.testData.TestDataFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class OrToJoinOperatorTest {

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
     * (A.CategoryID = B.EmployeeID) OR (A.CategoryID=C.ShipperID)
     */
    @Test
    public void columnEqualColumnOrColumnEqualValueTest() {
        MQLJoinOperator leftOperator = new EqualToJoin(
                new ColumnOperand("A.CategoryID"),
                new ColumnOperand("B.EmployeeID")
        );

        MQLJoinOperator rightOperator = new EqualToJoin(
                new ColumnOperand("A.CategoryID"),
                new ColumnOperand("C.ShipperID")
        );

        MQLJoinOperator orOperator = new OrToJoinOperator(
                leftOperator,
                rightOperator
        );

        MQLTable resultTable = orOperator.operateWith(mqlDataSource);
        List<Map<String, Object>> tableData = resultTable.getTableData();
        tableData.sort(Comparator.comparing(m -> (int)m.get("A.CategoryID")));

        tableData.forEach(eachRow ->  {
            assertThat(true, anyOf(
                    equalTo((int)eachRow.get("A.CategoryID")==(int)eachRow.get("B.EmployeeID")),
                    equalTo(eachRow.get("A.CategoryName").equals("C.ShipperID"))
                    ));
        });
    }
}
