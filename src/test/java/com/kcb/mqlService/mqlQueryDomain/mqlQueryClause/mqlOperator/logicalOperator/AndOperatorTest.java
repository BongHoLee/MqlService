package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.mqlOperator.logicalOperator;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ColumnOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.NumberValueOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.MQLOperator;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.logicalOperator.AndOperator;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.relationalOperator.joinOperator.EqualToJoin;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.relationalOperator.joinOperator.NotEqualToJoin;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.relationalOperator.valueOperator.eqaulTo.EqualToCV;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.relationalOperator.valueOperator.notEqualTo.NotEqualToCV;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.FromClause;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class AndOperatorTest {

    private MQLDataSource mqlDataSource = new MQLDataSource();

    @Before
    public void makeMqlDataSource() {
        Map<String, List<Map<String, Object>>> rowDataSource = new HashMap<>();
        FromClause from = new FromClause();
        from.addDataSourceIds("A");
        from.addDataSourceIds("B");
        from.addDataSourceIds("C");

        List<Map<String, Object>> list1 = new ArrayList<>();
        List<Map<String, Object>> list2 = new ArrayList<>();
        List<Map<String, Object>> list3 = new ArrayList<>();

        for (int i=0; i<3; i++) {
            Map<String, Object> map1 = new HashMap<>();
            Map<String, Object> map2 = new HashMap<>();
            Map<String, Object> map3 = new HashMap<>();

            map1.put("KEY", i);
            map2.put("KEY", i);
            map3.put("KEY", i);


            map1.put("COLUMN1", i+1);
            map1.put("COLUMN2", i+2);
            map1.put("COLUMN3", String.valueOf(i+3));

            map2.put("COLUMN1", i+1);
            map2.put("COLUMN2", i+2);
            map2.put("COLUMN3", String.valueOf(i+3));

            map3.put("COLUMN1", i+1);
            map3.put("COLUMN2", i+2);
            map3.put("COLUMN3", String.valueOf(i+3));

            list1.add(map1);
            list2.add(map2);
            list3.add(map3);
        }

        rowDataSource.put("A", list1);
        rowDataSource.put("B", list2);
        rowDataSource.put("C", list3);

        mqlDataSource = from.makeMqlDataSources(rowDataSource);
    }

    // (A.KEY = B.KEY) AND (A.KEY != C.KEY)
    @Test
    public void andOperatorWithSameColumnTest() {
        MQLOperator operator1 = new EqualToJoin(
                new ColumnOperand("A.KEY"),
                new ColumnOperand("B.KEY")
        );

        MQLOperator operator2 = new NotEqualToJoin(
                new ColumnOperand("A.KEY"),
                new ColumnOperand("C.KEY")
        );

        MQLOperator andOperator = new AndOperator(operator1, operator2);

        MQLTable resultTable = andOperator.operateWith(mqlDataSource);

        assertThat(resultTable.getJoinSet(), equalTo(new HashSet<>(Arrays.asList("A", "B", "C"))));
        resultTable.getTableData().forEach(mergedRow ->{
            assertThat(mergedRow.get("A.KEY"), is(mergedRow.get("B.KEY")));
            assertThat(mergedRow.get("A.KEY"), not(mergedRow.get("C.KEY")));

        });

    }
}
