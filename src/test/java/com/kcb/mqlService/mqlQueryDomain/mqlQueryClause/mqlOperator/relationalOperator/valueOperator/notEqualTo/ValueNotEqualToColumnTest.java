package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.mqlOperator.relationalOperator.valueOperator.notEqualTo;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ColumnOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.NumberValueOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.MQLOperator;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.relationalOperator.valueOperator.eqaulTo.EqualToVC;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.relationalOperator.valueOperator.notEqualTo.NotEqualToVC;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.FromClause;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class ValueNotEqualToColumnTest {

    private MQLDataSource mqlDataSource = new MQLDataSource();

    @Before
    public void makeMqlDataSource() {
        Map<String, List<Map<String, Object>>> rowDataSource = new HashMap<>();
        FromClause from = new FromClause();
        from.addDataSourceIds("A");
        from.addDataSourceIds("B");

        List<Map<String, Object>> list1 = new ArrayList<>();
        List<Map<String, Object>> list2 = new ArrayList<>();

        for (int i=0; i<3; i++) {
            Map<String, Object> map1 = new HashMap<>();
            Map<String, Object> map2 = new HashMap<>();

            map1.put("KEY", i);
            map2.put("KEY", i);


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
    public void numberValueNotEqualToColumnTest() {
        String columnExpression = "A.KEY";
        int intValueExpression = 2;

        MQLOperator operator = new NotEqualToVC(
                new NumberValueOperand(intValueExpression),
                new ColumnOperand(columnExpression)
        );


        MQLTable table = operator.operateWith(mqlDataSource);
        assertThat(2, is(table.getTableData().size()));
        assertThat(table.getJoinSet(), equalTo(new HashSet<>(Arrays.asList("A"))));
        table.getTableData().forEach(eachRow ->
                assertThat(eachRow.get(columnExpression), not(intValueExpression))
        );


    }
}
