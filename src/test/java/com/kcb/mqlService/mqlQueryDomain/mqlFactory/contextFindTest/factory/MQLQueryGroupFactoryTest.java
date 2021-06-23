package com.kcb.mqlService.mqlQueryDomain.mqlFactory.contextFindTest.factory;


import com.kcb.mqlService.mqlFactory.MQLQueryGroupFactory;
import com.kcb.mqlService.mqlQueryDomain.MQLQueryGroup;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.FromClause;
import com.kcb.mqlService.testData.TestDataFactory;
import com.kcb.mqlService.utils.MQLResourceConfiguration;
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

public class MQLQueryGroupFactoryTest {

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
    public void MQLGroupFactory_생성_및_실행_테스트() {
        MQLResourceConfiguration.addFilePath("src/test/resources");
        MQLQueryGroup queryGroup = MQLQueryGroupFactory.getInstance().create();

        List<Map<String, Object>> query1Result = queryGroup.executeQuery("Query1", rawDataSource);
        query1Result.forEach(eachRow -> {
            assertThat(eachRow.keySet(), hasItems("E.SupplierID", "A.CategoryID", "A.CategoryName", "SUM(E.Price)"));
            assertThat(eachRow.keySet(), hasSize(4));
            assertThat((Double)eachRow.get("E.SupplierID"), lessThan(7.0));
        });

        List<Map<String, Object>> query2Result = queryGroup.executeQuery("Query2", rawDataSource);
        query2Result.forEach(eachRow -> {
            assertThat(eachRow.keySet(), hasItems("E.ProductID", "E.Price", "E.Unit", "A.CategoryID", "A.CategoryName", "E.CategoryID"));
            assertThat(eachRow.keySet(), hasSize(6));
            assertThat((Double)eachRow.get("E.Price"), greaterThan(20.0));
            assertThat((Double)eachRow.get("A.CategoryID"), greaterThanOrEqualTo(7.0));
            assertThat(eachRow.get("A.CategoryID"), is(equalTo(eachRow.get("E.CategoryID"))));
        });

        List<Map<String, Object>> query3Result = queryGroup.executeQuery("Query3", rawDataSource);
        query3Result.forEach(eachRow -> {
            assertThat(eachRow.keySet(), hasItems("CategoryID", "CategoryName", "SupplierID", "PriceSum"));
            assertThat(eachRow.keySet(), hasSize(4));
            assertThat((Double)eachRow.get("SupplierID"), lessThan(7.0));
        });

        List<Map<String, Object>> query4Result = queryGroup.executeQuery("Query4", rawDataSource);
        query4Result.forEach(eachRow -> {
            assertThat(eachRow.keySet(), hasItems("ProductName", "Unit", "ProductNameLength", "UnitLength"));
            assertThat(eachRow.keySet(), hasSize(4));
            assertThat(String.valueOf(eachRow.get("ProductName")).length(), is(equalTo((int)eachRow.get("ProductNameLength"))));
            assertThat(String.valueOf(eachRow.get("Unit")).length(), is(equalTo((int)eachRow.get("UnitLength"))));
            assertThat((int)eachRow.get("ProductNameLength"), is(greaterThan((int)eachRow.get("UnitLength"))));
        });

        List<Map<String, Object>> query5Result = queryGroup.executeQuery("Query5", rawDataSource);
        query5Result.forEach(eachRow -> {
            assertThat(eachRow.keySet(), hasItems("SupplierID", "LengthCategoryID", "SumLengthSupplierID"));
            assertThat(eachRow.keySet(), hasSize(3));
            assertThat(String.valueOf(eachRow.get("LengthCategoryID")).length(), is(greaterThan(String.valueOf(eachRow.get("SumLengthSupplierID")).length())));
        });
    }

}
