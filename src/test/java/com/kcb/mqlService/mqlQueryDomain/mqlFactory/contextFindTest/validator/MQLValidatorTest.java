package com.kcb.mqlService.mqlQueryDomain.mqlFactory.contextFindTest.validator;

import com.kcb.mqlService.mqlFactory.SqlContextStorage;
import com.kcb.mqlService.mqlFactory.exception.MQLQueryNotValidException;
import com.kcb.mqlService.mqlFactory.validator.FromMatchJoinValidator;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class MQLValidatorTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void from절_없는경우() {
        thrown.expect(MQLQueryNotValidException.class);
        String sql = "SELECT A.CustomerID AS CustomerID, B.CategoryID\n";

        SqlContextStorage sqlContextStorage = new SqlContextStorage(sql);
        sqlContextStorage.isValid();
    }

    @Test
    public void groupBy절없이_having절존재() {
        thrown.expect(MQLQueryNotValidException.class);
        String sql = "SELECT A.CustomerID AS CustomerID, B.CategoryID\n" +
                "FROM table1 A, table2 B\n" +
                "HAVING A.ID=B.ID";

        SqlContextStorage sqlContextStorage = new SqlContextStorage(sql);
        sqlContextStorage.isValid();
    }
}
