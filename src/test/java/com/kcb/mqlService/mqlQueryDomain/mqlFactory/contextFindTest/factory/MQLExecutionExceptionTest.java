package com.kcb.mqlService.mqlQueryDomain.mqlFactory.contextFindTest.factory;

import com.kcb.mqlService.mqlFactory.MQLQueryContextFactory;
import com.kcb.mqlService.mqlFactory.SqlContextStorage;
import com.kcb.mqlService.mqlFactory.exception.MQLQueryExecuteException;
import com.kcb.mqlService.mqlFactory.exception.MQLQueryNotValidException;
import com.kcb.mqlService.mqlQueryDomain.MQLQueryContext;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.FromClause;
import com.kcb.mqlService.testData.TestDataFactory;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MQLExecutionExceptionTest {
    private MQLDataStorage mqlDataStorage;
    private Map<String, List<Map<String, Object>>> rawDataSource;
    private String queryId = "testQuery";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

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
        mqlDataStorage = from.makeMqlDataSources("testQueryID", "TestQueryScript", rawDataSource);
    }

    @Test
    public void rawDataSource에_sql에서정의한_DataSourceID가_포함되어있지않을때() {

        String sql =
                "SELECT * FROM   MQL_MAIN M1 JOIN MQL_SUB1 M2 ON M1.CRDID  = M2.CRDID";

        SqlContextStorage sqlContextStorage = new SqlContextStorage(queryId, sql);
        MQLQueryContext context = MQLQueryContextFactory.getInstance().create("testQuery", sql);

        thrown.expect(MQLQueryExecuteException.class);
        context.executeQuery(rawDataSource);
    }


    public void print(List<Map<String, Object>> result) {
        System.out.println(result);
        System.out.println(result.size());
    }



}
