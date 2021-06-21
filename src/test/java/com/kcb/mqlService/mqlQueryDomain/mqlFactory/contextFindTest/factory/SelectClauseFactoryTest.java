package com.kcb.mqlService.mqlQueryDomain.mqlFactory.contextFindTest.factory;

import com.kcb.mqlService.mqlFactory.SelectClauseFactory;
import com.kcb.mqlService.mqlFactory.SqlContextStorage;
import com.kcb.mqlService.mqlFactory.exception.MQLQueryNotValidException;
import org.junit.Test;

public class SelectClauseFactoryTest {

    @Test
    public void from절_없는경우() {

        String sql = "SELECT LENGTH(A.ID, A.NAME) AS LENA, SUBSTR(LENGTH(A.ID), 1, 2) AS SUBSTR, SUM(LENGTH(A.ID)) AS SUMM\n" +
                "FROM table1 A\n" +
                "GROUP BY A.ID, A.NAME"
                ;




    }
}
