package com.kcb.mqlService.mqlQueryDomain.mqlFactory.contextFindTest.validator;

import com.kcb.mqlService.mqlFactory.SqlContextStorage;
import com.kcb.mqlService.mqlFactory.validator.ColumnAndFunctionValidator;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ColumnAndFunctionValidatorTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void selectItemColumnValidatorTest() {


        String sql = "SELECT E.SupplierID SupplierID, SUM(LENGTH(E.CategoryID)) SumLengthCategoryID, LENGTH(E.SupplierID) LengthSupplierID, C.ID\n" +
                "FROM Products E\n" +
                "GROUP BY E.SupplierID\n" +
                "HAVING SUM(LENGTH(E.CategoryID)) < LENGTH(E.SupplierID)"
                ;


        SqlContextStorage sqlContextStorage = new SqlContextStorage(sql);
        ColumnAndFunctionValidator columnAndFunctionValidator = new ColumnAndFunctionValidator();
        columnAndFunctionValidator.isValid(sqlContextStorage);
    }
}
