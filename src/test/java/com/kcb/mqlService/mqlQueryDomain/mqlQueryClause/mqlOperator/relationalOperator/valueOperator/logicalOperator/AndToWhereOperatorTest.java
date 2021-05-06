package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.mqlOperator.relationalOperator.valueOperator.logicalOperator;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ColumnOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.NumberValueOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.StringValueOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.joinOperator.MQLJoinOperator;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.joinOperator.NoneToJoinOperator;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.whereOperator.MQLWhereOperator;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.whereOperator.eqaulTo.EqualToCV;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.whereOperator.largerThan.LargerThanCV;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.whereOperator.logicalOperator.AndToWhereOperator;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.FromClause;
import com.kcb.mqlService.testData.TestDataFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.greaterThan;

public class AndToWhereOperatorTest {

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
     *  A.CategoryID > 1 AND A.CategoryName='AAA'
     */
    @Test
    public void whereAndOperatorTest() {
        MQLJoinOperator noneJoin = new NoneToJoinOperator();

        MQLWhereOperator operator1 = new LargerThanCV(
                new ColumnOperand("A.CategoryID"),
                new NumberValueOperand(1)
        );

        MQLWhereOperator operator2 = new EqualToCV(
                new ColumnOperand("A.CategoryName"),
                new StringValueOperand("AAA")
        );

        MQLWhereOperator andOperator = new AndToWhereOperator(operator1, operator2);

        MQLTable table = andOperator.operateWith(noneJoin.operateWith(mqlDataSource));

        assertThat(new HashSet<>(Arrays.asList("A")), is(equalTo(table.getJoinSet())));
        table.getTableData().forEach(
                eachRow -> {
                    assertThat("AAA", is(equalTo(eachRow.get("A.CategoryName"))));
                    assertThat((int)eachRow.get("A.CategoryID"), is(greaterThan(1)));
                }

        );

    }

}
