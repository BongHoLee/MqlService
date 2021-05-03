package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.mqlOperator.logicalOperator;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ColumnOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.NumberValueOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.MQLOperator;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.logicalOperator.OrOperator;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.relationalOperator.joinOperator.EqualToJoin;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.relationalOperator.valueOperator.eqaulTo.EqualToCV;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.FromClause;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import org.hamcrest.core.*;

public class OrOperatorTest {

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
            map2.put("COLUMN1", i+1);
            map3.put("COLUMN1", i+1);


            list1.add(map1);
            list2.add(map2);
            list3.add(map3);

        }

        rowDataSource.put("A", list1);
        rowDataSource.put("B", list2);
        rowDataSource.put("C", list3);

        mqlDataSource = from.makeMqlDataSources(rowDataSource);
    }

    /**
     * (A.KEY=B.KEY) OR (C.KEY=1)
     */
    @Test
    public void orOperatorTest1() {
        MQLOperator operator1 = new EqualToJoin(
                new ColumnOperand("A.KEY"),
                new ColumnOperand("B.KEY")
        );

        MQLOperator operator2 = new EqualToCV(
                new ColumnOperand("C.KEY"),
                new NumberValueOperand(1)
        );

        MQLOperator orOperator = new OrOperator(operator1, operator2);
        MQLTable result = orOperator.operateWith(mqlDataSource);

        assertThat(new HashSet<>(Arrays.asList("A", "B", "C")), is(equalTo(result.getJoinSet())));
        assertThat(15, is(equalTo(result.getTableData().size())));

        result.getTableData().forEach(eachRow ->{
            assertThat(true, equalTo(
                    (eachRow.get("A.KEY")).equals(eachRow.get("B.KEY")) || (int)eachRow.get("C.KEY") == 1
            ));
        });

    }


    /**
     * (A.KEY=B.KEY) OR (A.KEY=C.KEY)
     */

    @Test
    public void orOperatorTest2() {
        MQLOperator operator1 = new EqualToJoin(
                new ColumnOperand("A.KEY"),
                new ColumnOperand("B.KEY")
        );

        MQLOperator operator2 = new EqualToJoin(
                new ColumnOperand("A.KEY"),
                new ColumnOperand("C.KEY")

        );

        MQLOperator orOperator = new OrOperator(operator1, operator2);
        MQLTable result = orOperator.operateWith(mqlDataSource);

        assertThat(new HashSet<>(Arrays.asList("A", "B", "C")), is(equalTo(result.getJoinSet())));
        assertThat(15, is(equalTo(result.getTableData().size())));

        result.getTableData().forEach(eachRow ->{
            assertThat(true, equalTo(
                    (eachRow.get("A.KEY")).equals(eachRow.get("B.KEY")) || (eachRow.get("A.KEY")).equals(eachRow.get("C.KEY"))
            ));
        });
    }
}
