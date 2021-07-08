package com.kcb.mqlService.mqlQueryDomain.mqlFactory.contextFindTest.validator;

import com.kcb.mqlService.mqlFactory.SelectClauseFactory;
import com.kcb.mqlService.mqlFactory.SqlContextStorage;
import com.kcb.mqlService.mqlFactory.exception.MQLQueryNotValidException;
import com.kcb.mqlService.mqlFactory.validator.FromMatchJoinValidator;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.SelectClause;
import org.apache.log4j.BasicConfigurator;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class MQLValidatorTest {
    private String queryId = "testQuery";
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void from절_없는경우() {
        thrown.expect(MQLQueryNotValidException.class);
        String sql = "SELECT A.CustomerID AS CustomerID, B.CategoryID\n";

        SqlContextStorage sqlContextStorage = new SqlContextStorage(queryId, sql);
        sqlContextStorage.isValid();
    }

    @Test
    public void groupBy절없이_having절존재() {
        thrown.expect(MQLQueryNotValidException.class);
        String sql = "SELECT A.CustomerID AS CustomerID, B.CategoryID\n" +
                "FROM table1 A, table2 B\n" +
                "HAVING A.ID=B.ID";

        SqlContextStorage sqlContextStorage = new SqlContextStorage(queryId, sql);
        sqlContextStorage.isValid();
    }

    @Test
    public void groupby있을때_allColums_존재시_exception() {
        thrown.expect(MQLQueryNotValidException.class);
        String sql = "SELECT   A.*, B.*\n" +
                "FROM table1 A, table2 B\n" +
                "WHERE A.ID=B.ID\n" +
                "GROUP BY A.ID"
                ;

        SqlContextStorage sqlContextStorage = new SqlContextStorage(queryId, sql);
        sqlContextStorage.isValid();
    }

    @Test
    public void groupby없을때_group함수_일반컬럼_함께존재시_exception() {
        thrown.expect(MQLQueryNotValidException.class);
        String sql = "SELECT COUNT(T1.CRDID), T2.CRDID \n" +
                "FROM TEMP1 T1, TEMP2 T2 \n" +
                "WHERE T1.CRDID > T2.CRDID \n"
                ;

        SqlContextStorage sqlContextStorage = new SqlContextStorage(queryId, sql);
        sqlContextStorage.isValid();
    }



}
