package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.mqlExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.*;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.ColumnElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.ValueElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.singleRowFunction.LENGTH;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.logicalOperator.OROperator;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.operatingVisitor.WithColumnTargetOperating;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.operatingVisitor.WithSingleRowFunctionTargetOperating;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.relationalOperator.RelationalOperator;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.FromClause;
import com.kcb.mqlService.testData.TestDataFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;


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
        mqlDataStorage = from.makeMqlDataSources(rawDataSource);
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
                RelationalOperator::equalTo,
                new WithColumnTargetOperating(
                        new ColumnElement(compareColumn)
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
                RelationalOperator::largerThan,
                new WithColumnTargetOperating(
                        new ColumnElement(compareColumn)

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
                RelationalOperator::lessThan,
                new WithColumnTargetOperating(
                        new ColumnElement(compareColumn)

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
                RelationalOperator::equalTo,
                new WithColumnTargetOperating(
                        new ColumnElement(compareColumnName)

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
                        RelationalOperator::equalTo,
                        new WithColumnTargetOperating(
                                new ColumnElement("C.ShipperID")

                        )
                ),
                new ValueOperandExpression(
                        new ValueElement("Speedy Express"),
                        RelationalOperator::equalTo,
                        new WithColumnTargetOperating(
                                new ColumnElement("C.ShipperName")

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
                        RelationalOperator::equalTo,
                        new WithColumnTargetOperating(
                                new ColumnElement("A.CategoryID")

                        )
                ),
                new ValueOperandExpression(
                        new ValueElement("Grains/Cereals"),
                        RelationalOperator::equalTo,
                        new WithColumnTargetOperating(
                                new ColumnElement("A.CategoryName")

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

    /**
     *  LENGTH(A.CategoryName) < B.EmployeeID
     */
    @Test
    public void singleRowFunctionHasColumnWithColumnTest() {
        String leftColumn = "A.CategoryName";
        String rightColumn = "B.EmployeeID";

        MQLOperandExpression expression = new SingleRowFunctionOperandExpression(
                new LENGTH(new ColumnElement(leftColumn)),
                RelationalOperator::lessThan,
                new WithColumnTargetOperating(
                        new ColumnElement(rightColumn)
                )
        );

        MQLDataStorage result = expression.operatingWith(mqlDataStorage);

        for (Map<String, Object> eachRow : result.getMqlTable().getTableData()) {
            assertThat(
                    (((String)eachRow.get(leftColumn)).length()),
                    is(lessThan((int)eachRow.get(rightColumn))));
        }

        print(result);
    }

    /**
     *
     *
     *LENGTH("333") <= A.CategoryID
     */

    @Test
    public void singleRowFunctionHasColumnWithValueTest() {
        String column = "A.CategoryID";
        String value = "333";

        MQLOperandExpression expression = new SingleRowFunctionOperandExpression(
                new LENGTH(new ValueElement(value)),
                RelationalOperator::lessThanEqualTo,
                new WithColumnTargetOperating(
                        new ColumnElement(column)
                )
        );

        MQLDataStorage result = expression.operatingWith(mqlDataStorage);

        for (Map<String, Object> eachRow : result.getMqlTable().getTableData()) {
            assertThat(
                    (value.length()),
                    is(lessThanOrEqualTo(((int)eachRow.get(column)))));
        }

        print(result);
    }

    public void print(MQLDataStorage mqlDataStorage) {
        System.out.println(mqlDataStorage.getMqlTable().getJoinSet());
        System.out.println(mqlDataStorage.getMqlTable().getTableData());
        System.out.println(mqlDataStorage.getMqlTable().getTableData().size());
    }
}
