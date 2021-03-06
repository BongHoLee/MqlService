package com.kcb.mqlService.mqlQueryDomain.mqlFactory.contextFindTest.factory;


import com.kcb.mqlService.mqlFactory.MQLQueryContextFactory;
import com.kcb.mqlService.mqlFactory.SelectClauseFactory;
import com.kcb.mqlService.mqlFactory.SqlContextStorage;
import com.kcb.mqlService.mqlQueryDomain.MQLQueryContext;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.FromClause;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.SelectClause;
import com.kcb.mqlService.testData.TestDataFactory;
import org.junit.Before;
import org.junit.Test;

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
    private String queryId = "testQuery";

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
        //mqlDataStorage = from.makeMqlDataSources("testQueryID", "TestQueryScript", rawDataSource);
    }

    /**
     * SELECT A.CategoryID, A.CategoryName, E.SupplierID, SUM(E.Price)
     * From Categories A
     * JOIN Products E ON A.CategoryID=E.CategoryID
     * WHERE E.SupplierID < 5
     * GROUP BY A.CategoryID, A.CategoryName, E.SupplierID
     */
    @Test
    public void SelectClauseFactory_Join_Where_GROUPBY_???????????????() {

        String sql = "      SELECT A.CategoryID, A.CategoryName, E.SupplierID, SUM(E.Price)\n" +
                "      From Categories A\n" +
                "      JOIN Products E ON A.CategoryID=E.CategoryID\n" +
                "      WHERE E.SupplierID < 5\n" +
                "      GROUP BY A.CategoryID, A.CategoryName, E.SupplierID";

        SqlContextStorage sqlContextStorage = new SqlContextStorage(queryId, sql);
        SelectClause select = SelectClauseFactory.getInstance().create(sqlContextStorage);

        List<Map<String, Object>> result = select.executeQueryWith(rawDataSource);

        result.forEach(eachRow -> {
            assertThat(eachRow.keySet(), hasItems("E.SupplierID", "A.CategoryID", "A.CategoryName", "SUM(E.Price)"));
            assertThat(eachRow.keySet(), hasSize(4));
            assertThat((Double)eachRow.get("E.SupplierID"), lessThan(7.0));
        });

    }

    @Test
    public void selectClauseFactory_Join_Where_???????????????() {

        String sql = "     SELECT  A.CategoryID, A.CategoryName, E.CategoryID, E.ProductID, E.Price, E.Unit\n" +
                "     FROM Products E\n" +
                "     JOIN Categories A ON A.CategoryID=E.CategoryID\n" +
                "     WHERE E.Price > 20 AND A.CategoryID >= 7";

        SqlContextStorage sqlContextStorage = new SqlContextStorage(queryId, sql);
        SelectClause selectClause = SelectClauseFactory.getInstance().create(sqlContextStorage);

        List<Map<String, Object>> result = selectClause.executeQueryWith(rawDataSource);

        result.forEach(eachRow -> {
            assertThat(eachRow.keySet(), hasItems("E.ProductID", "E.Price", "E.Unit", "A.CategoryID", "A.CategoryName", "E.CategoryID"));
            assertThat(eachRow.keySet(), hasSize(6));
            assertThat((Double)eachRow.get("E.Price"), greaterThan(20.0));
            assertThat((Double)eachRow.get("A.CategoryID"), greaterThanOrEqualTo(7.0));
            assertThat(eachRow.get("A.CategoryID"), is(equalTo(eachRow.get("E.CategoryID"))));
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
    public void selectClauseFactory_Join_Where_GroupBy_???????????????() {
        String sql = "      SELECT A.CategoryID AS CategoryID, A.CategoryName AS CategoryName, E.SupplierID AS SupplierID, SUM(E.Price) AS PriceSum\n" +
                "      From Categories A\n" +
                "      JOIN Products E ON A.CategoryID=E.CategoryID\n" +
                "      WHERE E.SupplierID < 5\n" +
                "      GROUP BY A.CategoryID, A.CategoryName, E.SupplierID";

        SqlContextStorage sqlContextStorage = new SqlContextStorage(queryId, sql);
        sqlContextStorage.isValid();
        SelectClause selectClause = SelectClauseFactory.getInstance().create(sqlContextStorage);

        List<Map<String, Object>> result = selectClause.executeQueryWith(rawDataSource);

        result.forEach(eachRow -> {
            assertThat(eachRow.keySet(), hasItems("CategoryID", "CategoryName", "SupplierID", "PriceSum"));
            assertThat(eachRow.keySet(), hasSize(4));
            assertThat((Double)eachRow.get("SupplierID"), lessThan(7.0));
        });
    }

    /**
    SELECT A.*, E.*
    From Categories A
    JOIN Products E ON A.CategoryID=E.CategoryID
    WHERE E.SupplierID < 5
     */

    @Test
    public void allTableColumns_??????() {

        String sql = "      SELECT A.*, E.*\n" +
                "      From Categories A\n" +
                "      JOIN Products E ON A.CategoryID=E.CategoryID\n" +
                "      WHERE E.SupplierID < 5\n";

        SqlContextStorage sqlContextStorage = new SqlContextStorage(queryId, sql);
        sqlContextStorage.isValid();
        SelectClause selectClause = SelectClauseFactory.getInstance().create(sqlContextStorage);

        List<Map<String, Object>> result = selectClause.executeQueryWith(rawDataSource);

        result.forEach(eachRow -> {
            assertThat(eachRow.keySet(), hasItems("A.Description",
                    "E.SupplierID",
                    "E.CategoryID",
                    "E.Unit",
                    "E.ProductID",
                    "E.Price",
                    "A.CategoryID",
                    "E.ProductName",
                    "A.CategoryName"));
            assertThat(eachRow.keySet(), hasSize(9));
        });

    }

    @Test
    public void ???????????????_allColumns?????????() {

        String sql = "      SELECT *\n" +
                "      From Categories A\n" +
                "      JOIN Products E ON A.CategoryID=E.CategoryID\n" +
                "      WHERE E.SupplierID < 5\n";

        SqlContextStorage sqlContextStorage = new SqlContextStorage(queryId, sql);
        sqlContextStorage.isValid();
        SelectClause selectClause = SelectClauseFactory.getInstance().create(sqlContextStorage);

        List<Map<String, Object>> result = selectClause.executeQueryWith(rawDataSource);

        result.forEach(eachRow -> {
            assertThat(eachRow.keySet(), hasItems("A.Description",
                    "E.SupplierID",
                    "E.CategoryID",
                    "E.Unit",
                    "E.ProductID",
                    "E.Price",
                    "A.CategoryID",
                    "E.ProductName",
                    "A.CategoryName"));
            assertThat(eachRow.keySet(), hasSize(9));
        });

    }

    /**
     * SELECT E.ProductID, E.SupplierID, A.CategoryID, E.Price
     * FROM Categories A, Products E
     * WHERE A.CategoryID=E.ProductID AND E.SupplierID < A.CategoryID AND E.Price < 100
     */

    @Test
    public void ????????????_??????????????????() {

        String sql =
                " SELECT E.ProductID, E.SupplierID, A.CategoryID, E.Price\n" +
                " FROM Categories A, Products E\n" +
                " WHERE A.CategoryID=E.ProductID AND E.SupplierID < A.CategoryID AND E.Price < 100";

        SqlContextStorage sqlContextStorage = new SqlContextStorage(queryId, sql);
        sqlContextStorage.isValid();
        SelectClause selectClause = SelectClauseFactory.getInstance().create(sqlContextStorage);

        List<Map<String, Object>> result = selectClause.executeQueryWith(rawDataSource);
        result.forEach(eachRow -> {
            assertThat(eachRow.keySet(), hasItems("E.SupplierID", "E.ProductID", "E.Price", "A.CategoryID"));
            assertThat(eachRow.keySet(), hasSize(4));
            assertThat((Double)eachRow.get("A.CategoryID"), is(equalTo((Double)eachRow.get("E.ProductID"))));
            assertThat((Double)eachRow.get("E.SupplierID"), is(lessThan((Double)eachRow.get("A.CategoryID"))));
            assertThat((Double)eachRow.get("E.Price"), is(lessThan(100.0)));
        });

    }

    /**
     * SELECT E.ProductID, E.SupplierID
     * FROM Products E
     * GROUP BY E.ProductID
     */

    @Test
    public void ???????????????_GROUPBY??????() {

        String sql =
                " SELECT E.ProductID, E.SupplierID\n" +
                        "     FROM Products E\n" +
                        "     GROUP BY E.ProductID, E.SupplierID";

        SqlContextStorage sqlContextStorage = new SqlContextStorage(queryId, sql);
        sqlContextStorage.isValid();
        SelectClause selectClause = SelectClauseFactory.getInstance().create(sqlContextStorage);
        List<Map<String, Object>> result = selectClause.executeQueryWith(rawDataSource);

        result.forEach(eachRow -> {
            assertThat(eachRow.keySet(), hasItems("E.SupplierID", "E.ProductID"));
            assertThat(eachRow.keySet(), hasSize(2));
        });

    }

    @Test
    public void ORDERBY_TEST() {

        String sql =
                " SELECT E.ProductName AS ProductName, LENGTH(E.ProductName) AS ProductNameLength, E.Unit AS Unit, LENGTH(E.Unit) AS UnitLength\n" +
                        "     FROM Products E\n" +
                        "     ORDER BY LENGTH(E.ProductName) DESC, LENGTH(E.Unit) ";

        SqlContextStorage sqlContextStorage = new SqlContextStorage(queryId, sql);
        sqlContextStorage.isValid();
        SelectClause selectClause = SelectClauseFactory.getInstance().create(sqlContextStorage);

        List<Map<String, Object>> result = selectClause.executeQueryWith(rawDataSource);
        print(result);

    }

    @Test
    public void LIKE_TEST() {

        String sql =
                " SELECT *\n" +
                        "FROM TEMP1 M1 JOIN TEMP2 M2\n" +
                        "ON M1.CRDID = M2.CRDID\n" +
                        "AND M1.AP_AMT > 57429398\n" +
                        "AND M1.REG_USR_ID IS NULL\n" +
                        "WHERE M1.REG = 1 AND M2.CHG_USR_ID  LIKE '%l'";

        SqlContextStorage sqlContextStorage = new SqlContextStorage(queryId, sql);
        sqlContextStorage.isValid();
        SelectClause selectClause = SelectClauseFactory.getInstance().create(sqlContextStorage);

        List<Map<String, Object>> result = selectClause.executeQueryWith(rawDataSource);
        print(result);

    }



    public void print(List<Map<String, Object>> result) {
        System.out.println(result);
        System.out.println(result.size());
    }

}
