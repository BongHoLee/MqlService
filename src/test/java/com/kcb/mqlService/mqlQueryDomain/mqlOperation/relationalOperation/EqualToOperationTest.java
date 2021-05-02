package com.kcb.mqlService.mqlQueryDomain.mqlOperation.relationalOperation;

import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ColumnOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.MQLOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.NumberValueOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.StringValueOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperation.MQLOperation;
import com.kcb.mqlService.mqlQueryDomain.mqlOperation.relationalOpration.EqualToOperation;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.FromClause;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class EqualToOperationTest {

    private Map<String, List<Map<String, Object>>> mqlDataSource;

    @Before
    public void makeMqlDataSource() {
        Map<String, List<Map<String, Object>>> rowDataSource = new HashMap<>();
        FromClause from = new FromClause();
        from.addDataSourceIds("A");
        from.addDataSourceIds("B");

        List<Map<String, Object>> list1 = new ArrayList<>();
        List<Map<String, Object>> list2 = new ArrayList<>();

        for (int i=0; i<10; i++) {
            Map<String, Object> map1 = new HashMap<>();
            Map<String, Object> map2 = new HashMap<>();

            map1.put("KEY", i);
            map2.put("KEY", i+5);


            map1.put("COLUMN1", i+1);
            map1.put("COLUMN2", i+2);
            map1.put("COLUMN3", String.valueOf(i+3));

            map2.put("COLUMN1", i+1);
            map2.put("COLUMN2", i+2);
            map2.put("COLUMN3", String.valueOf(i+3));

            list1.add(map1);
            list2.add(map2);
        }

        rowDataSource.put("A", list1);
        rowDataSource.put("B", list2);

        mqlDataSource = from.makeMqlDataSources(rowDataSource);
    }

    /**
     *  'A.KEY = B.KEY' test
     */
    @Test
    public void columnWithColumnTest() {
        String leftExpression = "A.KEY";
        String rightExpression = "B.KEY";

        MQLOperand leftOperand = new ColumnOperand(leftExpression);
        MQLOperand rightOperand = new ColumnOperand(rightExpression);

        MQLOperation operation = new EqualToOperation(leftOperand, rightOperand);

        List<Map<String, Object>> result = operation.operateWith(mqlDataSource);

        assertThat(5, is(result.size()));

        result.forEach(mergedRow -> assertThat(mergedRow.get("A.KEY"), equalTo(mergedRow.get("B.KEY"))));
    }

    /**
     * 'A.KEY = "KEY"' test
     */

    @Test
    public void columnWithStringValueOperandTest() {
        String leftExpression = "A.COLUMN3";
        String stringExpression = "3";

        MQLOperand leftOperand = new ColumnOperand(leftExpression);
        MQLOperand rightOperand = new StringValueOperand(stringExpression);

        MQLOperation operation = new EqualToOperation(leftOperand, rightOperand);

        List<Map<String, Object>> result = operation.operateWith(mqlDataSource);

        assertThat(1, is(result.size()));
        assertThat(stringExpression, equalTo(result.get(0).get(leftOperand.getExpressionToString())));
    }

    @Test
    public void columnWithNumberValueOperationTest() {
        String leftExpression = "A.KEY";
        int intExpression = 0;

        MQLOperand leftOperand = new ColumnOperand(leftExpression);
        MQLOperand rightOperand = new NumberValueOperand(intExpression);

        MQLOperation operation = new EqualToOperation(leftOperand, rightOperand);

        List<Map<String, Object>> result = operation.operateWith(mqlDataSource);

        assertThat(1, is(result.size()));
        assertThat(intExpression, equalTo(result.get(0).get(leftOperand.getExpressionToString())));
    }

}
