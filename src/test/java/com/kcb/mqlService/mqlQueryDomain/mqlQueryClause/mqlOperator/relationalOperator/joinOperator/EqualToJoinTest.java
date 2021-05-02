package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.mqlOperator.relationalOperator.joinOperator;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ColumnOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.MQLOperator;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.relationalOperator.joinOperator.EqualToJoin;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.FromClause;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class EqualToJoinTest {

    private MQLDataSource mqlDataSource = new MQLDataSource();

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
    public void StringEqualToStringTest() {
        String leftColumn = "A.KEY";
        String rightColumn = "B.KEY";
        MQLOperator operator = new EqualToJoin(
                new ColumnOperand(leftColumn),
                new ColumnOperand(rightColumn)
        );

        MQLTable table = operator.operateWith(mqlDataSource);
        assertThat(table.getJoinSet(), equalTo(new HashSet<>(Arrays.asList("A", "B"))));
        table.getTableData().forEach(eachRow -> assertThat(eachRow.get("A.KEY"), equalTo(eachRow.get("B.KEY"))));

    }
}
