package com.kcb.mqlService.mqlQueryDomain.mqlOperation.relationalOperation;


import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ColumnOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.MQLOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.NumberValueOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.StringValueOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperation.MQLOperation;
import com.kcb.mqlService.mqlQueryDomain.mqlOperation.relationalOpration.EqualToOperation;
import com.kcb.mqlService.mqlQueryDomain.mqlOperation.relationalOpration.LargerThanOperation;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.FromClause;
import com.kcb.mqlService.utils.DateFormatUtil;
import jdk.vm.ci.meta.Value;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.*;

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

        int date1Idx = 10;
        int date2Idx = 0;
        for (int i=0; i<10; i++) {
            Map<String, Object> map1 = new HashMap<>();
            Map<String, Object> map2 = new HashMap<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar time1 = Calendar.getInstance();
            Calendar time2 = Calendar.getInstance();

            time1.add(Calendar.MONTH, date1Idx - i);
            time2.add(Calendar.MONTH, date2Idx + i);

            map1.put("KEY", i);
            map2.put("KEY", i+5);

            map1.put("COLUMN1", i+1);
            map1.put("COLUMN2", i+2);
            map1.put("COLUMN3", String.valueOf(i+3));
            map1.put("date", sdf.format(time1.getTime()));

            map2.put("COLUMN1", i+1);
            map2.put("COLUMN2", i+2);
            map2.put("COLUMN3", String.valueOf(i+3));
            map2.put("date", sdf.format(time2.getTime()));

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
        System.out.println(result);


        result.forEach(mergedRow -> assertThat(true, is((int)mergedRow.get("A.KEY") > (int)mergedRow.get("B.KEY"))));

        String leftDateColumnExpression = "A.date";
        String rightDateColumnExpression = "B.date";

        leftOperand = new ColumnOperand(leftDateColumnExpression);
        rightOperand = new ColumnOperand(rightDateColumnExpression);

        operation = new LargerThanOperation(leftOperand, rightOperand);
        result = operation.operateWith(mqlDataSource);

        result.forEach(mergedRow -> assertThat(
                true,
                is( DateFormatUtil.largerThan(
                        (String)mergedRow.get(leftDateColumnExpression),
                        (String)mergedRow.get(rightDateColumnExpression)
                        )
                )));

    }

    @Test
    public void columnLargerThanValueAndValueLargerThanColumnTest() {
        String columnExpression = "A.KEY";
        int valueExpression = 5;

        MQLOperand columnOperand = new ColumnOperand(columnExpression);
        MQLOperand valueOperand = new NumberValueOperand(valueExpression);

        MQLOperation operation = new LargerThanOperation(columnOperand, valueOperand);

        List<Map<String, Object>> result = operation.operateWith(mqlDataSource);
        assertThat(4, is(result.size()));
        result.forEach(
                eachRow -> assertThat(true, equalTo((int)eachRow.get(columnExpression) > valueExpression))
        );

        operation = new LargerThanOperation(valueOperand, columnOperand);
        result = operation.operateWith(mqlDataSource);
        assertThat(5, is(result.size()));

        result.forEach(
                eachRow -> assertThat(true, equalTo((int)eachRow.get(columnExpression) < valueExpression))
        );

        String dateColumnExpression = "A.date";
        String dateValue = "2021-07-29 21:30:56";

        columnOperand = new ColumnOperand(dateColumnExpression);
        valueOperand = new StringValueOperand(dateValue);

        operation = new LargerThanOperation(columnOperand, valueOperand);
        result = operation.operateWith(mqlDataSource);

        result.forEach(
                eachRow -> assertThat(true, equalTo((DateFormatUtil.largerThan((String)eachRow.get(dateColumnExpression), dateValue))))
        );

    }
}
