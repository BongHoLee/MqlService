package com.kcb.mqlService.mqlQueryDomain.mqlOperation.relationalOperation;


import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ColumnOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.MQLOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperation.MQLOperation;
import com.kcb.mqlService.mqlQueryDomain.mqlOperation.relationalOpration.EqualToOperation;
import com.kcb.mqlService.mqlQueryDomain.mqlOperation.relationalOpration.LargerThanOperation;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.FromClause;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;



public class LargerThanOperationTest {

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

    @Test
    public void columnLargerThanColumnTest() {
        String leftExpression = "A.KEY";
        String rightExpression = "B.KEY";

        MQLOperand leftOperand = new ColumnOperand(leftExpression);
        MQLOperand rightOperand = new ColumnOperand(rightExpression);

        MQLOperation operation = new LargerThanOperation(leftOperand, rightOperand);

        List<Map<String, Object>> result = operation.operateWith(mqlDataSource);

        assertThat(10, is(result.size()));
        result.forEach(mergedRow -> assertThat(true, is((int)mergedRow.get("A.KEY") > (int)mergedRow.get("B.KEY"))));
    }
}
