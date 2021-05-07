package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.mqlOperator.relationalOperator.valueOperator.logicalOperator;


import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ColumnOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.NumberValueOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.joinOperator.MQLJoinOperator;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.joinOperator.NoneToJoinOperator;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.whereOperator.MQLWhereOperator;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.whereOperator.eqaulTo.EqualToCV;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.whereOperator.largerThan.LargerThanCV;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.whereOperator.largerThanEqualTo.LargerThanEqualToCV;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.whereOperator.logicalOperator.OrToWhereOperator;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.FromClause;
import com.kcb.mqlService.testData.TestDataFactory;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.greaterThan;

public class OrToWhereOperatorTest {

    private MQLDataSource mqlDataSource = new MQLDataSource();

    @Before
    public void makeMqlDataSource() {
        Map<String, List<Map<String, Object>>> rawDataSource = new HashMap<>();
        rawDataSource.put("A", TestDataFactory.tableOf("test"));

        FromClause from = new FromClause();
        from.addDataSourceIds("A");
        mqlDataSource = from.makeMqlDataSources(rawDataSource);
    }

    /**
     * A.CategoryID=1 OR A.CategoryID > 6
     */
    @Test
    public void orToWhereOperatorTest() {
        MQLJoinOperator noneJoin = new NoneToJoinOperator();

        MQLWhereOperator operator1 = new EqualToCV(
                new ColumnOperand("A.CategoryID"),
                new NumberValueOperand(1)
        );

        MQLWhereOperator operator2 = new LargerThanCV(
                new ColumnOperand("A.CategoryID"),
                new NumberValueOperand(6)
        );

        MQLWhereOperator orOperator = new OrToWhereOperator(operator1, operator2);
        MQLTable table = orOperator.operateWith(noneJoin.operateWith(mqlDataSource));

        table.getTableData().forEach(
                eachRow -> {
                   assertThat((int)eachRow.get("A.CategoryID"), anyOf(is(1), greaterThan(6)));
                }

        );
    }
}
