package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause;

import com.kcb.mqlService.mqlFactory.SelectClauseFactory;
import com.kcb.mqlService.mqlFactory.SqlContextStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.ColumnOperandExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.GroupFunctionOperandExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.SingleRowFunctionOperandExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.ValueOperandExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.ColumnElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.MQLElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.ValueElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.groupFunction.COUNT;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.groupFunction.SUM;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.singleRowFunction.LENGTH;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.operatingVisitor.WithColumnTargetOperating;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.operatingVisitor.WithGroupFunctionTargetOperating;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.operatingVisitor.WithSingleRowFunctionTargetOperating;
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
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;




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
                "testQuery",
                "SELECT E.SupplierID, SUM(LENGTH(E.ProductName)) FROM Products E GROUP BY E.SupplierID",
                selectItems,
                new FromClause(),
                Arrays.asList(new NoneClause(), new GroupByClause(new ColumnElement("E.SupplierID")))
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
     * SELECT  A.CategoryID, A.CategoryName, E.CategoryID, E.ProductID, E.Price, E.Unit
     * FROM Products E
     * JOIN Categories A ON A.CategoryID=E.CategoryID
     * WHERE E.PRICE > 20 AND A.CategoryID >= 7
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

    /**
     * SELECT A.CategoryID, A.CategoryName, E.SupplierID, SUM(E.Price)
     * From Categories A
     * JOIN Products E ON A.CategoryID=E.CategoryID
     * WHERE E.SupplierID < 5
     * GROUP BY A.CategoryID, A.CategoryName, E.SupplierID
     */
    @Test
    public void selectGroupingDataWithJoinAndWhereAndGroupBy() {
        MQLElement element1 = new ColumnElement("A.CategoryID");
        MQLElement element2 = new ColumnElement("A.CategoryName");
        MQLElement element3 = new ColumnElement("E.SupplierID");
        MQLElement element4 = new SUM(new ColumnElement("E.Price"));
        List<MQLElement> selectItems = Arrays.asList(element1, element2, element3, element4);

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
                        ),
                        new WhereClause(
                                new ColumnOperandExpression(
                                        new ColumnElement("E.SupplierID"),
                                        RelationalOperator::lessThan,
                                        new WithValueTargetOperating(new ValueElement(5))
                                )
                        )
                ),
                new GroupByClause(
                        new ColumnElement("A.CategoryID"),
                        new ColumnElement("A.CategoryName"),
                        new ColumnElement("E.SupplierID")
                )
        );

        List<Map<String, Object>> result = select.executeQueryWith(rawDataSource);

        result.forEach(eachRow -> {
            assertThat(eachRow.keySet(), hasItems("E.SupplierID", "A.CategoryID", "A.CategoryName", "SUM(E.Price)"));
            assertThat(eachRow.keySet(), hasSize(4));
            assertThat((Double)eachRow.get("E.SupplierID"), lessThan(7.0));
        });


    }

    /**
     * SELECT A.CategoryID AS CategoryID, A.CategoryName AS CategoryName, E.SupplierID AS SupplierID, SUM(E.Price) AS PriceSum
     * From Categories A
     * JOIN Products E ON A.CategoryID=E.CategoryID
     * WHERE E.SupplierID < 5
     * GROUP BY A.CategoryID, A.CategoryName, E.SupplierID
     */
    @Test
    public void selectGroupingDataWithJoinAndWhereAndGroupByWithAlias() {
        MQLElement element1 = new ColumnElement("CategoryID", "A.CategoryID");
        MQLElement element2 = new ColumnElement("CategoryName", "A.CategoryName");
        MQLElement element3 = new ColumnElement("SupplierID", "E.SupplierID");
        MQLElement element4 = new SUM("PriceSum", new ColumnElement("E.Price"));
        List<MQLElement> selectItems = Arrays.asList(element1, element2, element3, element4);

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
                        ),
                        new WhereClause(
                                new ColumnOperandExpression(
                                        new ColumnElement("E.SupplierID"),
                                        RelationalOperator::lessThan,
                                        new WithValueTargetOperating(new ValueElement(5))
                                )
                        )
                ),
                new GroupByClause(
                        new ColumnElement("A.CategoryID"),
                        new ColumnElement("A.CategoryName"),
                        new ColumnElement("E.SupplierID")
                )
        );

        List<Map<String, Object>> result = select.executeQueryWith(rawDataSource);

        result.forEach(eachRow -> {
            assertThat(eachRow.keySet(), hasItems("CategoryID", "CategoryName", "SupplierID", "PriceSum"));
            assertThat(eachRow.keySet(), hasSize(4));
            assertThat((Double)eachRow.get("SupplierID"), lessThan(7.0));
        });


    }

    /**
     * SELECT E.SupplierID, SUM(LENGTH(E.ProductName)) SumLength
     * FROM Products E
     * GROUP BY E.SupplierID
     * HAVING SUM(LENGTH(E.ProductName)) > E.SupplierID
     */
    @Test
    public void selectGroupingDataWithHavingAndAlias() {
        Map<String, List<Map<String, Object>>> dataSource = new HashMap<>();
        dataSource.put("E", rawDataSource.get("E"));

        MQLElement element1 = new ColumnElement("SUPPLIERID", "E.SupplierID");
        MQLElement element2 = new SUM("SumLength", new LENGTH(new ColumnElement("E.ProductName")));

        List<MQLElement> selectItems = Arrays.asList(element1, element2);
        SelectClause select = new SelectClause(
                selectItems,
                new FromClause(),
                new NoneClause(),
                new GroupByClause(
                        new ColumnElement("E.SupplierID")
                )
                ,
                new HavingClause(
                        new GroupFunctionOperandExpression(
                                new SUM(new LENGTH(new ColumnElement("E.ProductName"))),
                                RelationalOperator::largerThan,
                                new WithColumnTargetOperating(new ColumnElement("E.SupplierID"))
                        )
                )
        );

        List<Map<String, Object>> result = select.executeQueryWith(dataSource);

        result.forEach(eachRow -> {
            assertThat(eachRow.keySet(), hasItems("SUPPLIERID", "SumLength"));
            assertThat(eachRow.keySet(), hasSize(2));
            assertThat((Double)eachRow.get("SumLength"), greaterThan((Double)eachRow.get("SUPPLIERID")));
        });
    }

    /**
     * SELECT E.SupplierID, SUM(LENGTH(E.ProductName)) SumLength
     * FROM Products E
     * GROUP BY E.SupplierID
     * HAVING SUM(LENGTH(E.ProductName)) > E.SupplierID
     */
    @Test
    public void selectGroupingDataWithHavingAndAlias2() {
        Map<String, List<Map<String, Object>>> dataSource = new HashMap<>();
        dataSource.put("E", rawDataSource.get("E"));

        MQLElement element1 = new ColumnElement("SUPPLIERID", "E.SupplierID");
        MQLElement element2 = new SUM("SumLength", new LENGTH(new ColumnElement("E.ProductName")));

        List<MQLElement> selectItems = Arrays.asList(element1, element2);
        SelectClause select = new SelectClause(
                selectItems,
                new FromClause(),
                new NoneClause(),
                new GroupByClause(
                        new ColumnElement("E.SupplierID")
                )
                ,
                new HavingClause(
                        new GroupFunctionOperandExpression(
                                new SUM(new LENGTH(new ColumnElement("E.ProductName"))),
                                RelationalOperator::largerThan,
                                new WithColumnTargetOperating(new ColumnElement("E.SupplierID"))
                        )
                )
        );

        List<Map<String, Object>> result = select.executeQueryWith(dataSource);

        result.forEach(eachRow -> {
            assertThat(eachRow.keySet(), hasItems("SUPPLIERID", "SumLength"));
            assertThat(eachRow.keySet(), hasSize(2));
            assertThat((Double)eachRow.get("SumLength"), greaterThan((Double)eachRow.get("SUPPLIERID")));
        });
    }

    /**
     * SELECT E.SupplierID, SUM(LENGTH(E.ProductName)) SumLength
     * FROM Products E
     * GROUP BY E.SupplierID
     * HAVING  40 < SUM(LENGTH(E.ProductName))
     */
    @Test
    public void selectGroupingDataWithHavingWithValueAndAlias() {
        Map<String, List<Map<String, Object>>> dataSource = new HashMap<>();
        dataSource.put("E", rawDataSource.get("E"));

        MQLElement element1 = new ColumnElement("SupplierID", "E.SupplierID");
        MQLElement element2 = new SUM("SumLength", new LENGTH(new ColumnElement("E.ProductName")));

        List<MQLElement> selectItems = Arrays.asList(element1, element2);
        SelectClause select = new SelectClause(
                selectItems,
                new FromClause(),
                new NoneClause(),
                new GroupByClause(
                        new ColumnElement("E.SupplierID")
                )
                ,
                new HavingClause(
                        new ValueOperandExpression(
                                new ValueElement(40),
                                RelationalOperator::lessThan,
                                new WithGroupFunctionTargetOperating(
                                        new SUM(new LENGTH(new ColumnElement("E.ProductName")))
                                )
                        )
                )
        );

        List<Map<String, Object>> result = select.executeQueryWith(dataSource);
        print(result);

        result.forEach(eachRow -> {
            assertThat(eachRow.keySet(), hasItems("SupplierID", "SumLength"));
            assertThat(eachRow.keySet(), hasSize(2));
            assertThat(40.0, is(lessThan( (Double)eachRow.get("SumLength"))));
        });
    }

    /**
     * SELECT E.SupplierID, SUM(LENGTH(E.ProductName)) SumLength
     * FROM Products E
     * GROUP BY E.SupplierID
     * HAVING   SUM(LENGTH(E.ProductName)) > 40
     */
    @Test
    public void selectGroupingDataWithHavingWithValueAndAlias2() {
        Map<String, List<Map<String, Object>>> dataSource = new HashMap<>();
        dataSource.put("E", rawDataSource.get("E"));

        MQLElement element1 = new ColumnElement("SupplierID", "E.SupplierID");
        MQLElement element2 = new SUM("SumLength", new LENGTH(new ColumnElement("E.ProductName")));

        List<MQLElement> selectItems = Arrays.asList(element1, element2);
        SelectClause select = new SelectClause(
                selectItems,
                new FromClause(),
                new NoneClause(),
                new GroupByClause(
                        new ColumnElement("E.SupplierID")
                )
                ,
                new HavingClause(
                        new GroupFunctionOperandExpression(
                                new SUM(new LENGTH(new ColumnElement("E.ProductName"))),
                                RelationalOperator::largerThan,
                                new WithValueTargetOperating(new ValueElement(40))
                        )
                )
        );

        List<Map<String, Object>> result = select.executeQueryWith(dataSource);


        result.forEach(eachRow -> {
            assertThat(eachRow.keySet(), hasItems("SupplierID", "SumLength"));
            assertThat(eachRow.keySet(), hasSize(2));
            assertThat(40.0, is(lessThan( (Double)eachRow.get("SumLength"))));
        });
    }

    /**
     * SELECT E.SupplierID SupplierID, SUM(LENGTH(E.ProductName)) SumLength, SUM(Price) SumPrice
     * FROM Products E
     * GROUP BY E.SupplierID
     * HAVING  SUM(E.Price) < SUM(LENGTH(E.ProductName))
     */
    @Test
    public void selectGroupingDataWithHavingWithGroupFunctionAndAlias() {
        Map<String, List<Map<String, Object>>> dataSource = new HashMap<>();
        dataSource.put("E", rawDataSource.get("E"));

        MQLElement element1 = new ColumnElement("SupplierID", "E.SupplierID");
        MQLElement element2 = new SUM("SumLength", new LENGTH(new ColumnElement("E.ProductName")));
        MQLElement element3 = new SUM("SumPrice", new ColumnElement("E.Price"));

        List<MQLElement> selectItems = Arrays.asList(element1, element2, element3);
        SelectClause select = new SelectClause(
                selectItems,
                new FromClause(),
                new NoneClause(),
                new GroupByClause(
                        new ColumnElement("E.SupplierID")
                )
                ,
                new HavingClause(
                        new GroupFunctionOperandExpression(
                                new SUM(new ColumnElement("E.Price")),
                                RelationalOperator::lessThan,
                                new WithGroupFunctionTargetOperating(new SUM(new LENGTH(new ColumnElement("E.ProductName"))))
                        )

                )
        );

        List<Map<String, Object>> result = select.executeQueryWith(dataSource);

        result.forEach(eachRow -> {
            assertThat(eachRow.keySet(), hasItems("SupplierID", "SumLength", "SumPrice"));
            assertThat(eachRow.keySet(), hasSize(3));
            assertThat((Double) eachRow.get("SumPrice"), is(lessThan((Double)eachRow.get("SumLength"))));
        });
    }

    /**
     SELECT E.SupplierID SupplierID, SUM(LENGTH(E.CategoryID)) SumLengthCategoryID, LENGTH(E.SupplierID) LengthSupplierID
     FROM Products E
     GROUP BY E.SupplierID
     HAVING LENGTH(E.SupplierID) > SUM(LENGTH(E.CategoryID))
     */
    @Test
    public void selectGroupingDataWithHavingWithSingleRowFunctionAndAlias() {
        Map<String, List<Map<String, Object>>> dataSource = new HashMap<>();
        dataSource.put("E", rawDataSource.get("E"));

        MQLElement element1 = new ColumnElement("SupplierID", "E.SupplierID");
        MQLElement element2 = new SUM("LengthCategoryID", new LENGTH(new ColumnElement("E.CategoryID")));
        MQLElement element3 = new LENGTH("SumLengthSupplierID", new ColumnElement("E.SupplierID"));

        List<MQLElement> selectItems = Arrays.asList(element1, element2, element3);

        SelectClause select = new SelectClause(
                selectItems,
                new FromClause(),
                new NoneClause(),
                new GroupByClause(
                        new ColumnElement("E.SupplierID")
                ),
                new HavingClause(
                        new SingleRowFunctionOperandExpression(
                                new LENGTH(new ColumnElement("E.SupplierID")),
                                RelationalOperator::largerThan,
                                new WithGroupFunctionTargetOperating(new SUM(new LENGTH(new ColumnElement("E.CategoryID"))))
                        )
                )
        );

        List<Map<String, Object>> result = select.executeQueryWith(dataSource);
        print(result);

        result.forEach(eachRow -> {
            assertThat(eachRow.keySet(), hasItems("SupplierID", "LengthCategoryID", "SumLengthSupplierID"));
            assertThat(eachRow.keySet(), hasSize(3));
            assertThat(String.valueOf(eachRow.get("LengthCategoryID")).length(), is(greaterThan(String.valueOf(eachRow.get("SumLengthSupplierID")).length())));
        });
    }

    /**
     SELECT E.SupplierID SupplierID, SUM(LENGTH(E.CategoryID)) SumLengthCategoryID, LENGTH(E.SupplierID) LengthSupplierID
     FROM Products E
     GROUP BY E.SupplierID
     HAVING SUM(LENGTH(E.CategoryID)) < LENGTH(E.SupplierID)
     */
    @Test
    public void selectGroupingDataWithHavingWithSingleRowFunctionAndAlias2() {
        Map<String, List<Map<String, Object>>> dataSource = new HashMap<>();
        dataSource.put("E", rawDataSource.get("E"));

        MQLElement element1 = new ColumnElement("SupplierID", "E.SupplierID");
        MQLElement element2 = new SUM("LengthCategoryID", new LENGTH(new ColumnElement("E.CategoryID")));
        MQLElement element3 = new LENGTH("SumLengthSupplierID", new ColumnElement("E.SupplierID"));

        List<MQLElement> selectItems = Arrays.asList(element1, element2, element3);
        SelectClause select = new SelectClause(
                selectItems,
                new FromClause(),
                new NoneClause(),
                new GroupByClause(
                        new ColumnElement("E.SupplierID")
                ),
                new HavingClause(
                        new GroupFunctionOperandExpression(
                                new SUM(new LENGTH(new ColumnElement("E.CategoryID"))),
                                RelationalOperator::lessThan,
                                new WithSingleRowFunctionTargetOperating(new LENGTH(new ColumnElement("E.SupplierID")))
                        )
                )
        );

        List<Map<String, Object>> result = select.executeQueryWith(dataSource);
        print(result);

        result.forEach(eachRow -> {
            assertThat(eachRow.keySet(), hasItems("SupplierID", "LengthCategoryID", "SumLengthSupplierID"));
            assertThat(eachRow.keySet(), hasSize(3));
            assertThat(String.valueOf(eachRow.get("LengthCategoryID")).length(), is(greaterThan(String.valueOf(eachRow.get("SumLengthSupplierID")).length())));
        });
    }

    /**
     SELECT E.ProductName AS ProductName, LENGTH(E.ProductName) AS ProductNameLength, E.Unit AS Unit, LENGTH(E.Unit) AS UnitLength
     FROM Products
     WHERE LENGTH(E.ProductName) > LENGTH(E.Unit)
     */
    @Test
    public void selectSingleRowFunctionWithSingleRowFunctionAndAlias() {
        Map<String, List<Map<String, Object>>> dataSource = new HashMap<>();
        dataSource.put("E", rawDataSource.get("E"));

        MQLElement element1 = new ColumnElement("ProductName", "E.ProductName");
        MQLElement element2 = new LENGTH("ProductNameLength", element1);
        MQLElement element3 = new ColumnElement("Unit", "E.Unit");
        MQLElement element4 = new LENGTH("UnitLength", element3);

        List<MQLElement> selectItems = Arrays.asList(element1, element2, element3, element4);
        SelectClause select = new SelectClause(
                selectItems,
                new FromClause(),
                new GeneralConditionClause(
                        new WhereClause(
                                new SingleRowFunctionOperandExpression(
                                        new LENGTH(new ColumnElement("E.ProductName")),
                                        RelationalOperator::largerThan,
                                        new WithSingleRowFunctionTargetOperating(new LENGTH(new ColumnElement("E.Unit")))
                                )
                        )
                )
        );

        List<Map<String, Object>> result = select.executeQueryWith(dataSource);
        print(result);

        result.forEach(eachRow -> {
            assertThat(eachRow.keySet(), hasItems("ProductName", "Unit", "ProductNameLength", "UnitLength"));
            assertThat(eachRow.keySet(), hasSize(4));
            assertThat(String.valueOf(eachRow.get("ProductName")).length(), is(equalTo((int)eachRow.get("ProductNameLength"))));
            assertThat(String.valueOf(eachRow.get("Unit")).length(), is(equalTo((int)eachRow.get("UnitLength"))));
            assertThat((int)eachRow.get("ProductNameLength"), is(greaterThan((int)eachRow.get("UnitLength"))));
        });
    }

    public void print(List<Map<String, Object>> result) {
        System.out.println(result);
        System.out.println(result.size());
    }


}
