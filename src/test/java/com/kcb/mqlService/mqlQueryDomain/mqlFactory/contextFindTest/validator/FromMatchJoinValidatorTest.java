package com.kcb.mqlService.mqlQueryDomain.mqlFactory.contextFindTest.validator;

import com.kcb.mqlService.mqlFactory.SqlContextStorage;
import com.kcb.mqlService.mqlFactory.exception.MQLQueryNotValidException;
import com.kcb.mqlService.mqlFactory.validator.FromMatchJoinValidator;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class FromMatchJoinValidatorTest {
    
    private String queryId = "testQuery";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void from테이블_두개이상_조건구문_없을때() {

        thrown.expect(MQLQueryNotValidException.class);
        thrown.expectMessage("Used Table : [A, B] | Joined Table : []");

        String sql = "SELECT A.CustomerID AS CustomerID, B.CategoryID\n" +
                "FROM Customers A, Categories B \n";

        SqlContextStorage sqlContextStorage = new SqlContextStorage(queryId,sql);

        FromMatchJoinValidator fromMatchJoinValidator = new FromMatchJoinValidator();
        fromMatchJoinValidator.isValid(sqlContextStorage);
    }

    @Test
    public void from테이블_두개이상_Where조건존재_하지만_묵시적조인없을때() {
        thrown.expect(MQLQueryNotValidException.class);
        thrown.expectMessage("Used Table : [A, B] | Joined Table : []");

        String sql = "SELECT A.CustomerID AS CustomerID, B.CategoryID\n" +
                "FROM Customers A, Categories B \n" +
                "Where A.ID=1"
                ;


        SqlContextStorage sqlContextStorage = new SqlContextStorage(queryId,sql);
        FromMatchJoinValidator fromMatchJoinValidator = new FromMatchJoinValidator();

        fromMatchJoinValidator.isValid(sqlContextStorage);
    }

    @Test
    public void from테이블_두개이상_Where조건존재_묵시적조인존재할때() {

        String sql = "SELECT A.CustomerID AS CustomerID, B.CategoryID\n" +
                "FROM Customers A, Categories B \n" +
                "Where A.ID=B.ID";


        SqlContextStorage sqlContextStorage = new SqlContextStorage(queryId,sql);
        FromMatchJoinValidator fromMatchJoinValidator = new FromMatchJoinValidator();

        assertThat(fromMatchJoinValidator.isValid(sqlContextStorage), is(true));

    }

    @Test
    public void from테이블_세개_Where조건존재_묵시적조인1개존재할때() {
        thrown.expect(MQLQueryNotValidException.class);
        thrown.expectMessage("Used Table : [A, B, C] | Joined Table : [A, B]");

        String sql = "SELECT A.CustomerID AS CustomerID, B.CategoryID, C.EmployeeID\n" +
                "FROM Customers A, Categories B, Employees C \n" +
                "Where A.ID=B.ID"
                ;


        SqlContextStorage sqlContextStorage = new SqlContextStorage(queryId,sql);
        FromMatchJoinValidator fromMatchJoinValidator = new FromMatchJoinValidator();

        fromMatchJoinValidator.isValid(sqlContextStorage);

    }

    @Test
    public void from테이블_한개_조건문없을때() {
        String sql = "SELECT A.CustomerID AS CustomerID, B.CategoryID, C.EmployeeID\n" +
                "FROM Customers A";


        SqlContextStorage sqlContextStorage = new SqlContextStorage(queryId,sql);
        FromMatchJoinValidator fromMatchJoinValidator = new FromMatchJoinValidator();
        assertThat(fromMatchJoinValidator.isValid(sqlContextStorage), is(true));
    }

    @Test
    public void from테이블_세개_조인조건존재_명시적조인_모두존재() {
        String sql = "SELECT A.CustomerID AS CustomerID, B.CategoryID, C.EmployeeID\n" +
                "FROM Customers A\n" +
                "JOIN Categories B ON A.ID=B.ID\n" +
                "JOIN Employees C ON C.ID=B.ID"
                ;


        SqlContextStorage sqlContextStorage = new SqlContextStorage(queryId,sql);
        FromMatchJoinValidator fromMatchJoinValidator = new FromMatchJoinValidator();
        assertThat(fromMatchJoinValidator.isValid(sqlContextStorage), is(true));
    }


    @Test
    public void from테이블_두개AB_없는테이블C_조인에서사용할때() {
        thrown.expect(MQLQueryNotValidException.class);
        thrown.expectMessage("Used Table : [A, B] | Joined Table : [A, B, C]");

        String sql = "SELECT A.CustomerID AS CustomerID, B.CategoryID, C.EmployeeID\n" +
                "FROM Customers A\n" +
                "JOIN Categories B ON A.ID=B.ID\n" +
                "WHERE C.ID=B.ID";


        SqlContextStorage sqlContextStorage = new SqlContextStorage(queryId,sql);
        FromMatchJoinValidator fromMatchJoinValidator = new FromMatchJoinValidator();
        fromMatchJoinValidator.isValid(sqlContextStorage);
    }

    @Test
    public void fromTable_moreThanTwo_WithJoin_And_WithGroupBy() {
        thrown.expect(MQLQueryNotValidException.class);
        thrown.expectMessage("Used Table : [A, B] | Joined Table : [A, B, C]");

        String sql = "SELECT A.CustomerID AS CustomerID, B.CategoryID, C.EmployeeID, LENGTH(A.ID), SUM(A.ID)\n" +
                "FROM Customers A\n" +
                "JOIN Categories B ON A.ID=B.ID\n" +
                "WHERE C.ID=B.ID\n" +
                "GROUP BY A.ID, B.ID, LENGTH(C.ID)"
                ;


        SqlContextStorage sqlContextStorage = new SqlContextStorage(queryId,sql);
        FromMatchJoinValidator fromMatchJoinValidator = new FromMatchJoinValidator();
        fromMatchJoinValidator.isValid(sqlContextStorage);
    }

}
