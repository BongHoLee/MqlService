package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.ColumnOperandExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.ColumnElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.MQLElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.ValueElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.groupFunction.COUNT;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.groupFunction.SUM;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.singleRowFunction.LENGTH;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.operatingVisitor.WithColumnTargetOperating;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.operatingVisitor.WithValueTargetOperating;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.relationalOperator.RelationalOperator;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.optionalClause.*;
import com.kcb.mqlService.testData.TestDataFactory;
import org.junit.Before;
import org.junit.Test;


import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.greaterThan;




import java.util.*;

public class SelectClauseTest {

    private MQLDataStorage mqlDataStorage;
    private Map<String, List<Map<String, Object>>> rawDataSource;

    @Before
    public void makeMqlDataSource() {
        MQLDataSource mqlDataSource = new MQLDataSource();

        rawDataSource = new HashMap<>();
        rawDataSource.put("A", TestDataFactory.tableOf("categories"));
        rawDataSource.put("B", TestDataFactory.tableOf("employees"));
        rawDataSource.put("C", TestDataFactory.tableOf("shippers"));
        rawDataSource.put("D", TestDataFactory.tableOf("test"));
        rawDataSource.put("E", TestDataFactory.tableOf("products"));
        FromClause from = new FromClause();
        mqlDataStorage = from.makeMqlDataSources(rawDataSource);


    }

    /**
     * SELECT E.SupplierID, SUM(LENGTH(E.ProductName))
     * FROM Products E
     * GROUP BY E.SupplierID
     *
     */

    @Test
    public void noConditionAndGroupingDataSelectTest() {
        Map<String, List<Map<String, Object>>> dataSource = new HashMap<>();
        dataSource.put("E", rawDataSource.get("E"));

        List<MQLElement> selectItems = Arrays.asList(
                new ColumnElement("E.SupplierID"),
                new SUM(new LENGTH(new ColumnElement("E.ProductName")))
        );


        SelectClause select = new SelectClause(
                selectItems,
                new FromClause(),
                new NoneClause(),
                new GroupByClause(new ColumnElement("E.SupplierID"))
        );

        List<Map<String, Object>> result = select.executeQueryWith(dataSource);

        result.forEach(eachRow -> {
            assertThat(eachRow.keySet(), hasItems("E.SupplierID", "SUM(LENGTH(E.ProductName))"));
            assertThat(eachRow.keySet(), hasSize(2));
        });

    }

    /**
     * SELECT  E.ProductID, E.SupplierID, E.Price, E.Unit
     * FROM Products E
     *
     */
    @Test
    public void selectPlainWithNoOptionalClauseTest() {
        Map<String, List<Map<String, Object>>> dataSource = new HashMap<>();
        dataSource.put("E", rawDataSource.get("E"));

        List<MQLElement> selectItems = Arrays.asList(
                new ColumnElement("E.ProductID"),
                new ColumnElement("E.SupplierID"),
                new ColumnElement("E.Price"),
                new ColumnElement("E.Unit")
        );

        SelectClause select = new SelectClause(
                selectItems,
                new FromClause(),
                new NoneClause()
        );

        List<Map<String, Object>> result = select.executeQueryWith(dataSource);

        result.forEach(eachRow -> {
            assertThat(eachRow.keySet(), hasItems("E.ProductID", "E.SupplierID", "E.Price", "E.Unit"));
            assertThat(eachRow.keySet(), hasSize(4));
        });
    }

    /**
     * SELECT  E.ProductID, E.SupplierID, E.Price, E.Unit
     * FROM Products E
     * WHERE E.Price > 20
     */
    @Test
    public void selectPlainWithWhereConditionClauseTest() {
        Map<String, List<Map<String, Object>>> dataSource = new HashMap<>();
        dataSource.put("E", rawDataSource.get("E"));

        List<MQLElement> selectItems = Arrays.asList(
                new ColumnElement("E.ProductID"),
                new ColumnElement("E.SupplierID"),
                new ColumnElement("E.Price"),
                new ColumnElement("E.Unit")
        );

        SelectClause select = new SelectClause(
                selectItems,
                new FromClause(),
                new GeneralConditionClause(
                        new WhereClause(
                                new ColumnOperandExpression(
                                        new ColumnElement("E.Price"),
                                        RelationalOperator::largerThan,
                                        new WithValueTargetOperating(new ValueElement(20))
                                )
                        )
                )
        );

        List<Map<String, Object>> result = select.executeQueryWith(dataSource);

        result.forEach(eachRow -> {
            assertThat(eachRow.keySet(), hasItems("E.ProductID", "E.SupplierID", "E.Price", "E.Unit"));
            assertThat(eachRow.keySet(), hasSize(4));
            assertThat((Double)eachRow.get("E.Price"), greaterThan(20.0));
        });

    }

    /**
     * SELECT  E.ProductID, E.SupplierID, E.Price, E.Unit
     * FROM Products E
     * WHERE (E.Price > 20) AND (E.SupplierID > 7)
     */
    @Test
    public void selectPlainWithWhereAndWhereConditionClauseTest() {
        Map<String, List<Map<String, Object>>> dataSource = new HashMap<>();
        dataSource.put("E", rawDataSource.get("E"));

        List<MQLElement> selectItems = Arrays.asList(
                new ColumnElement("E.ProductID"),
                new ColumnElement("E.SupplierID"),
                new ColumnElement("E.Price"),
                new ColumnElement("E.Unit")
        );

        SelectClause select = new SelectClause(
                selectItems,
                new FromClause(),
                new GeneralConditionClause(
                        new WhereClause(
                                new ColumnOperandExpression(
                                        new ColumnElement("E.Price"),
                                        RelationalOperator::largerThan,
                                        new WithValueTargetOperating(new ValueElement(20))
                                )
                        ),
                        new WhereClause(
                                new ColumnOperandExpression(
                                        new ColumnElement("E.SupplierID"),
                                        RelationalOperator::largerThanEqualTo,
                                        new WithValueTargetOperating(new ValueElement(7))
                                )
                        )
                )
        );

        List<Map<String, Object>> result = select.executeQueryWith(dataSource);

        result.forEach(eachRow -> {
            assertThat(eachRow.keySet(), hasItems("E.ProductID", "E.SupplierID", "E.Price", "E.Unit"));
            assertThat(eachRow.keySet(), hasSize(4));
            assertThat((Double)eachRow.get("E.Price"), greaterThan(20.0));
            assertThat((Double)eachRow.get("E.SupplierID"), greaterThanOrEqualTo(7.0));
        });


    }

    /**
     * SELECT  E.ProductID, E.Price, E.Unit, A.CategoryID, A.CategoryName
     * FROM Products E
     * JOIN Categories A ON E.CategoryID=A.CategoryID
     * WHERE E.PRICE > 20 AND E.CategoryID >= 7
     */
    @Test
    public void selectPlainWithJoinAndWhereAndWhereConditionClauseTest() {

        List<MQLElement> selectItems = Arrays.asList(
                new ColumnElement("A.CategoryID"),
                new ColumnElement("A.CategoryName"),
                new ColumnElement("E.CategoryID"),
                new ColumnElement("E.ProductID"),
                new ColumnElement("E.Price"),
                new ColumnElement("E.Unit")
        );

        SelectClause select = new SelectClause(
                selectItems,
                new FromClause(),
                new GeneralConditionClause(
                        new JoinClause(
                                new ColumnOperandExpression(
                                        new ColumnElement("A.CategoryID"),
                                        RelationalOperator::equalTo,
                                        new WithColumnTargetOperating(new ColumnElement("E.CategoryID"))
                                )
                        )
                        ,  
                        new WhereClause(
                                new ColumnOperandExpression(
                                        new ColumnElement("E.Price"),
                                        RelationalOperator::largerThan,
                                        new WithValueTargetOperating(new ValueElement(20))
                                )
                        ),
                        new WhereClause(
                                new ColumnOperandExpression(
                                        new ColumnElement("A.CategoryID"),
                                        RelationalOperator::largerThanEqualTo,
                                        new WithValueTargetOperating(new ValueElement(7))
                                )
                        )
                )
        );

        List<Map<String, Object>> result = select.executeQueryWith(rawDataSource);

        result.forEach(eachRow -> {
            assertThat(eachRow.keySet(), hasItems("E.ProductID", "E.Price", "E.Unit", "A.CategoryID", "A.CategoryName", "E.CategoryID"));
            assertThat(eachRow.keySet(), hasSize(6));
            assertThat((Double)eachRow.get("E.Price"), greaterThan(20.0));
            assertThat((Double)eachRow.get("A.CategoryID"), greaterThanOrEqualTo(7.0));
            assertThat(eachRow.get("A.CategoryID"), is(equalTo(eachRow.get("E.CategoryID"))));
        });

        print(result);
    }

    /**
     * SELECT COUNT(*)
     */
    @Test
    public void selectGroupOnlyCountElement() {
        Map<String, List<Map<String, Object>>> dataSource = new HashMap<>();
        dataSource.put("E", rawDataSource.get("E"));

        List<MQLElement> selectItems = Arrays.asList(
                new COUNT(new ValueElement("*"))
        );

        SelectClause select = new SelectClause(
                selectItems,
                new FromClause(),
                new NoneClause()
        );

        List<Map<String, Object>> result = select.executeQueryWith(dataSource);
        assertThat(result, hasSize(1));
        assertThat(dataSource.get("E"), hasSize((int)result.get(0).get("COUNT('*')")));
    }

    public void print(List<Map<String, Object>> result) {
        System.out.println(result);
        System.out.println(result.size());
    }
}
