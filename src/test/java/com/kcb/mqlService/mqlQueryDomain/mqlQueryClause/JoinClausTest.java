package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ColumnOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.joinOperator.EqualToJoin;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.joinOperator.MQLJoinOperator;
import com.kcb.mqlService.testData.TestDataFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.greaterThan;


public class JoinClausTest {
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
     *
     * FROM Categories A
     * JOIN Employees B ON A.CategoryID=B.EmployeeID
     * JOIN Shippers C ON A.CategoryID=C.ShipperID
     */
    @Test
    public void multipleJoinTest() {
        MQLJoinOperator join1 = new EqualToJoin(
                new ColumnOperand("A.CategoryID"),
                new ColumnOperand("B.EmployeeID")
        );

        MQLJoinOperator join2 = new EqualToJoin(
                new ColumnOperand("A.CategoryID"),
                new ColumnOperand("C.ShipperID")
        );

        JoinClaus joinClaus = new JoinClaus(join1, join2);
        MQLTable table = joinClaus.join(mqlDataSource);
        System.out.println(table.getJoinSet());
        System.out.println(table.getTableData());
    }

}
