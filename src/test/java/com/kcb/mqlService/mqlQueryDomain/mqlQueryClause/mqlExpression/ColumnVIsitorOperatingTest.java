package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.mqlExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.ColumnOperandExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.MQLOperandExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.MQLOperatingExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.ValueOperandExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.ColumnElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.ValueElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.logicalOperator.OROperator;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.operatingVisitor.WithColumnOperatingVisitor;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.relationalOperator.RelationalOperator;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.FromClause;
import com.kcb.mqlService.testData.TestDataFactory;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;

public class ColumnVIsitorOperatingTest {
    private MQLDataStorage mqlDataStorage;

    @Before
    public void makeMqlDataSource() {
        MQLDataSource mqlDataSource = new MQLDataSource();

        Map<String, List<Map<String, Object>>> rawDataSource = new HashMap<>();
        rawDataSource.put("A", TestDataFactory.tableOf("categories"));
        rawDataSource.put("B", TestDataFactory.tableOf("employees"));
        rawDataSource.put("C", TestDataFactory.tableOf("shippers"));
        rawDataSource.put("D", TestDataFactory.tableOf("test"));


        FromClause from = new FromClause();
        from.addDataSourceIds("A", "B", "C", "D");
        mqlDataSource = from.makeMqlDataSources(rawDataSource);

        mqlDataStorage = new MQLDataStorage(mqlDataSource, new MQLTable());
    }

    /**
     * A.CategoryID = B.EmployeeID
     */
    @Test
    public void columnWithColumnEqualToOperatingTest() {
        String standardColumn = "A.CategoryID";
        String compareColumn = "B.EmployeeID";

        MQLOperandExpression expression = new ColumnOperandExpression(
                new ColumnElement(standardColumn),
                new WithColumnOperatingVisitor(
                        new ColumnElement(compareColumn),
                        RelationalOperator::equalTo
                )
        );

        MQLDataStorage resultStorage = expression.operatingWith(mqlDataStorage);
        MQLTable resultMQLTable = resultStorage.getMqlTable();

        resultMQLTable.getTableData().forEach(eachRow -> {
            assertThat(eachRow.get(standardColumn), is(equalTo(eachRow.get(compareColumn))));
        });
    }

    /**
     *  A.CategoryID > B.EmployeeID
     */

    @Test
    public void columnWithColumnLargerThanOperatingTest() {
        String standardColumn = "A.CategoryID";
        String compareColumn = "B.EmployeeID";

        MQLOperandExpression expression = new ColumnOperandExpression(
                new ColumnElement(standardColumn),
                new WithColumnOperatingVisitor(
                        new ColumnElement(compareColumn),
                        RelationalOperator::largerThan
                )
        );

        MQLDataStorage resultStorage = expression.operatingWith(mqlDataStorage);
        resultStorage.getMqlTable().getTableData().forEach(
                eachRow -> {
                    assertThat((int)eachRow.get(standardColumn), is(greaterThan((int)eachRow.get(compareColumn))));
                }
        );
    }

    /**
     *  A.CategoryID < B.EmployeeID
     */
    @Test
    public void columnWithColumnLessThanOperatingTest() {
        String standardColumn = "A.CategoryID";
        String compareColumn = "B.EmployeeID";

        MQLOperandExpression expression = new ColumnOperandExpression(
                new ColumnElement(standardColumn),
                new WithColumnOperatingVisitor(
                        new ColumnElement(compareColumn),
                        RelationalOperator::lessThan
                )
        );

        MQLDataStorage resultStorage = expression.operatingWith(mqlDataStorage);
        resultStorage.getMqlTable().getTableData().forEach(
                eachRow -> {
                    assertThat((int)eachRow.get(standardColumn), is(lessThan((int)eachRow.get(compareColumn))));
                }
        );
    }

    /**
     * "AAA" = CategoryName
     */
    @Test
    public void valueWithColumnEqualToOperatingTest() {
        String standardValue = "AAA";
        String compareColumnName = "D.CategoryName";

        MQLOperandExpression expression = new ValueOperandExpression(
                new ValueElement(standardValue),
                new WithColumnOperatingVisitor(
                        new ColumnElement(compareColumnName),
                        RelationalOperator::equalTo
                )
        );

        MQLDataStorage resultStorage = expression.operatingWith(mqlDataStorage);
        resultStorage.getMqlTable().getTableData().forEach(eachRow -> {
            assertThat(standardValue, is(equalTo(eachRow.get(compareColumnName))));
        });
    }

    /**
     * (A.CategoryID = C.ShipperID) OR ("Speedy Express" = C.ShipperName)
     */
    @Test
    public void columnWithColumnORColumnWithValueTest() {
        MQLOperatingExpression orOperator = new OROperator(
                new ColumnOperandExpression(
                        new ColumnElement("A.CategoryID"),
                        new WithColumnOperatingVisitor(
                                new ColumnElement("C.ShipperID"),
                                RelationalOperator::equalTo
                        )
                ),
                new ValueOperandExpression(
                        new ValueElement("Speedy Express"),
                        new WithColumnOperatingVisitor(
                                new ColumnElement("C.ShipperName"),
                                RelationalOperator::equalTo
                        )
                )
        );

        MQLDataStorage result = orOperator.operatingWith(mqlDataStorage);
        result.getMqlTable().getTableData().forEach(eachRow -> {
            assertThat(true, anyOf(
                    is(eachRow.get("A.CategoryID").equals(eachRow.get("C.ShipperID"))),
                    is(eachRow.get("C.ShipperName").equals("Speedy Express"))
            ));
        });

    }

    /**
     *
     *  (1 = A.CategoryID) OR ('Grains/Cereals' = A.CategoryName)
     */
    @Test
    public void valueWithColumnORvalueWithColumnTest() {
        MQLOperatingExpression orOperator = new OROperator(
                new ValueOperandExpression(
                        new ValueElement(1),
                        new WithColumnOperatingVisitor(
                                new ColumnElement("A.CategoryID"),
                                RelationalOperator::equalTo
                        )
                ),
                new ValueOperandExpression(
                        new ValueElement("Grains/Cereals"),
                        new WithColumnOperatingVisitor(
                                new ColumnElement("A.CategoryName"),
                                RelationalOperator::equalTo
                        )
                )
        );

        MQLDataStorage result = orOperator.operatingWith(mqlDataStorage);
        result.getMqlTable().getTableData().forEach(eachRow -> {
            assertThat(true, anyOf(
                    is(eachRow.get("A.CategoryID").equals(1)),
                    is(eachRow.get("A.CategoryName").equals("Grains/Cereals"))
            ));
        });
    }


    public void print(MQLDataStorage mqlDataStorage) {
        System.out.println(mqlDataStorage.getMqlTable().getJoinSet());
        System.out.println(mqlDataStorage.getMqlTable().getTableData());
    }
}
