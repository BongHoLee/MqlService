package com.kcb.mqlService.mqlQueryDomain.mqlFactory.contextFindTest.factory;

import com.kcb.mqlService.mqlFactory.OptionalClauseFactory;
import com.kcb.mqlService.mqlFactory.SelectClauseFactory;
import com.kcb.mqlService.mqlFactory.SqlContextStorage;
import com.kcb.mqlService.mqlFactory.exception.MQLQueryNotValidException;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.ColumnOperandExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.ColumnElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.MQLElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.ValueElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.groupFunction.SUM;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.operatingVisitor.WithColumnTargetOperating;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.operatingVisitor.WithValueTargetOperating;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.relationalOperator.RelationalOperator;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.FromClause;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.OptionalClause;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.SelectClause;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.optionalClause.GeneralConditionClause;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.optionalClause.GroupByClause;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.optionalClause.JoinClause;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.optionalClause.WhereClause;
import com.kcb.mqlService.testData.TestDataFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

public class SelectClauseFactoryTest {
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



    @Test
    public void SelectClauseFactory_Join_Where_포함된경우() {

        String sql = "     SELECT  A.CategoryID, A.CategoryName, E.CategoryID, E.ProductID, E.Price, E.Unit\n" +
                "     FROM Products E\n" +
                "     JOIN Categories A ON A.CategoryID=E.CategoryID\n" +
                "     WHERE E.Price > 20 AND A.CategoryID >= 7";

        SqlContextStorage sqlContextStorage = new SqlContextStorage(sql);
        SelectClause selectClause = SelectClauseFactory.getInstance().create(sqlContextStorage);

        List<Map<String, Object>> result = selectClause.executeQueryWith(rawDataSource);

        System.out.println(result);
        result.forEach(eachRow -> {
            assertThat(eachRow.keySet(), hasItems("E.ProductID", "E.Price", "E.Unit", "A.CategoryID", "A.CategoryName", "E.CategoryID"));
            assertThat(eachRow.keySet(), hasSize(6));
            assertThat((Double)eachRow.get("E.Price"), greaterThan(20.0));
            assertThat((Double)eachRow.get("A.CategoryID"), greaterThanOrEqualTo(7.0));
            assertThat(eachRow.get("A.CategoryID"), is(equalTo(eachRow.get("E.CategoryID"))));
        });

    }


    /**
     * SELECT A.CategoryID, A.CategoryName, E.SupplierID, SUM(E.Price)
     * From Categories A
     * JOIN Products E ON A.CategoryID=E.CategoryID
     * WHERE E.SupplierID < 5
     * GROUP BY A.CategoryID, A.CategoryName, E.SupplierID
     */

    @Test
    public void SelectClauseFactory_Join_Where_GROUPBY_포함된경우() {

        String sql = "      SELECT A.CategoryID, A.CategoryName, E.SupplierID, SUM(E.Price)\n" +
                "      From Categories A\n" +
                "      JOIN Products E ON A.CategoryID=E.CategoryID\n" +
                "      WHERE E.SupplierID < 5\n" +
                "      GROUP BY A.CategoryID, A.CategoryName, E.SupplierID";

        SqlContextStorage sqlContextStorage = new SqlContextStorage(sql);
        SelectClause select = SelectClauseFactory.getInstance().create(sqlContextStorage);

        List<Map<String, Object>> result = select.executeQueryWith(rawDataSource);
        System.out.println(result);
        result.forEach(eachRow -> {
            assertThat(eachRow.keySet(), hasItems("E.SupplierID", "A.CategoryID", "A.CategoryName", "SUM(E.Price)"));
            assertThat(eachRow.keySet(), hasSize(4));
            assertThat((Double)eachRow.get("E.SupplierID"), lessThan(7.0));
        });

    }
}