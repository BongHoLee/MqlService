package com.kcb.mqlService.mqlQueryDomain.mqlFactory.contextFindTest.validator;

import com.kcb.mqlService.mqlFactory.SqlContextStorage;
import com.kcb.mqlService.mqlFactory.exception.MQLQueryNotValidException;
import com.kcb.mqlService.mqlFactory.validator.ItemsOfGeneralClauseValidator;
import com.kcb.mqlService.mqlFactory.validator.ItemsOfRelatedGroupByClauseValidator;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ColumnAndFunctionValidatorTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void selectItem_From절에없는_테이블컬럼사용할때() {
        String sql = "SELECT E.SuppleirID, C.ID\n" +
                "FROM Products E\n"
                ;

        SqlContextStorage sqlContextStorage = new SqlContextStorage(sql);
        ItemsOfRelatedGroupByClauseValidator columnAndFunctionValidator = new ItemsOfRelatedGroupByClauseValidator();

        thrown.expect(MQLQueryNotValidException.class);
        columnAndFunctionValidator.isValid(sqlContextStorage);
    }

    @Test
    public void selectItem_함수에서_From절에없는_테이블컬럼사용할때() {
        String sql = "SELECT A.ID, B.ID, LENGTH(C.ID)\n" +
                "FROM table1 A, table2 B\n" +
                "WHERE A.ID = B.ID"
                ;

        SqlContextStorage sqlContextStorage = new SqlContextStorage(sql);
        ItemsOfRelatedGroupByClauseValidator columnAndFunctionValidator = new ItemsOfRelatedGroupByClauseValidator();

        thrown.expect(MQLQueryNotValidException.class);
        columnAndFunctionValidator.isValid(sqlContextStorage);
    }

    @Test
    public void selectItem_함수에서_정의되지않은_함수사용할때() {
        String sql = "SELECT A.ID, B.ID, LENGTH(TEMP(A.ID))\n" +
                "FROM table1 A, table2 B\n" +
                "WHERE A.ID = B.ID"
                ;

        SqlContextStorage sqlContextStorage = new SqlContextStorage(sql);
        ItemsOfRelatedGroupByClauseValidator columnAndFunctionValidator = new ItemsOfRelatedGroupByClauseValidator();

        thrown.expect(MQLQueryNotValidException.class);
        columnAndFunctionValidator.isValid(sqlContextStorage);
    }

    @Test
    public void join에서_From절에없는_테이블컬럼사용할때() {
        String sql = "SELECT A.ID, B.ID\n" +
                "FROM table1 A\n" +
                "JOIN table2 B ON B.ID=C.ID"
                ;

        SqlContextStorage sqlContextStorage = new SqlContextStorage(sql);
        ItemsOfGeneralClauseValidator columnAndFunctionValidator = new ItemsOfGeneralClauseValidator();

        thrown.expect(MQLQueryNotValidException.class);
        columnAndFunctionValidator.isValid(sqlContextStorage);
    }

    @Test
    public void join절_함수에서_정의되지않은_테이블컬럼사용할때() {
        String sql = "SELECT A.ID, B.ID\n" +
                "FROM table1 A\n" +
                "JOIN table2 B ON A.ID=B.ID AND LENGTH(C.ID)=1"
                ;

        SqlContextStorage sqlContextStorage = new SqlContextStorage(sql);
        ItemsOfGeneralClauseValidator columnAndFunctionValidator = new ItemsOfGeneralClauseValidator();

        thrown.expect(MQLQueryNotValidException.class);
        columnAndFunctionValidator.isValid(sqlContextStorage);
    }

    @Test
    public void join절_그룹함수사용할때() {
        String sql = "SELECT A.ID, B.ID\n" +
                "FROM table1 A\n" +
                "JOIN table2 B ON A.ID=B.ID AND LENGTH(SUM(C.ID))>1"
                ;

        SqlContextStorage sqlContextStorage = new SqlContextStorage(sql);
        ItemsOfGeneralClauseValidator columnAndFunctionValidator = new ItemsOfGeneralClauseValidator();

        thrown.expect(MQLQueryNotValidException.class);
        columnAndFunctionValidator.isValid(sqlContextStorage);
    }

    @Test
    public void where절에서_From절에없는_테이블컬럼사용할때() {
        String sql = "SELECT A.ID, B.ID\n" +
                "FROM table1 A\n" +
                "JOIN table2 B ON A.ID=B.ID\n" +
                "WHERE A.ID=C.ID"
                ;

        SqlContextStorage sqlContextStorage = new SqlContextStorage(sql);
        ItemsOfGeneralClauseValidator columnAndFunctionValidator = new ItemsOfGeneralClauseValidator();

        thrown.expect(MQLQueryNotValidException.class);
        columnAndFunctionValidator.isValid(sqlContextStorage);
    }

    @Test
    public void where절_함수에서_정의되지않은_테이블컬럼사용할때() {
        String sql = "SELECT A.ID, B.ID\n" +
                "FROM table1 A\n" +
                "JOIN table2 B ON A.ID=B.ID\n" +
                "WHERE A.ID=LENGTH(TEMP(A.ID))"
                ;

        SqlContextStorage sqlContextStorage = new SqlContextStorage(sql);
        ItemsOfGeneralClauseValidator columnAndFunctionValidator = new ItemsOfGeneralClauseValidator();

        thrown.expect(MQLQueryNotValidException.class);
        columnAndFunctionValidator.isValid(sqlContextStorage);
    }

    @Test
    public void where절_그룹함수사용할때() {
        String sql = "SELECT A.ID, B.ID\n" +
                "FROM table1 A\n" +
                "WHERE LENGTH(SUM(A.ID)) > 1"
                ;

        SqlContextStorage sqlContextStorage = new SqlContextStorage(sql);
        ItemsOfGeneralClauseValidator columnAndFunctionValidator = new ItemsOfGeneralClauseValidator();

        thrown.expect(MQLQueryNotValidException.class);
        columnAndFunctionValidator.isValid(sqlContextStorage);
    }

    @Test
    public void groupBy절_from절에없는_테이블컬럼사용할때() {
        String sql = "SELECT A.ID, B.ID\n" +
                "FROM table1 A\n" +
                "JOIN table2 B ON A.ID=B.ID AND B.AGE=A.AGE\n" +
                "WHERE A.NAME='lee'\n" +
                "GROUP BY A.ID, B.ID, C.ID"
                ;

        SqlContextStorage sqlContextStorage = new SqlContextStorage(sql);
        ItemsOfGeneralClauseValidator columnAndFunctionValidator = new ItemsOfGeneralClauseValidator();

        thrown.expect(MQLQueryNotValidException.class);
        columnAndFunctionValidator.isValid(sqlContextStorage);
    }

    @Test
    public void groupBy절_함수에서_정의되지않은_함수사용할때() {
        String sql = "SELECT A.ID, B.ID\n" +
                "FROM table1 A\n" +
                "JOIN table2 B ON A.ID=B.ID AND B.AGE=A.AGE\n" +
                "WHERE A.NAME='lee'\n" +
                "GROUP BY A.ID, B.ID, LENGTH(TEMP(A.ID))"
                ;

        SqlContextStorage sqlContextStorage = new SqlContextStorage(sql);
        ItemsOfGeneralClauseValidator columnAndFunctionValidator = new ItemsOfGeneralClauseValidator();

        thrown.expect(MQLQueryNotValidException.class);
        columnAndFunctionValidator.isValid(sqlContextStorage);
    }

    @Test
    public void groupBy절_함수에서_그룹함수사용할때() {
        String sql = "SELECT A.ID, B.ID\n" +
                "FROM table1 A\n" +
                "JOIN table2 B ON A.ID=B.ID AND B.AGE=A.AGE\n" +
                "WHERE A.NAME='lee'\n" +
                "GROUP BY A.ID, B.ID, LENGTH(SUM(A.ID))"
                ;

        SqlContextStorage sqlContextStorage = new SqlContextStorage(sql);
        ItemsOfGeneralClauseValidator columnAndFunctionValidator = new ItemsOfGeneralClauseValidator();

        thrown.expect(MQLQueryNotValidException.class);
        columnAndFunctionValidator.isValid(sqlContextStorage);
    }

    @Test
    public void selecItem에서_함수에서_groupBy절의_아이템중_함수파라미터를_선언할때() {
        String sql = "SELECT A.ID, B.ID, A.AGE\n" +
                "FROM table1 A\n" +
                "JOIN table2 B ON A.ID=B.ID AND B.AGE=A.AGE\n" +
                "WHERE A.NAME='lee'\n" +
                "GROUP BY A.ID, B.ID, LENGTH(A.AGE)"
                ;

        SqlContextStorage sqlContextStorage = new SqlContextStorage(sql);
        ItemsOfRelatedGroupByClauseValidator columnAndFunctionValidator = new ItemsOfRelatedGroupByClauseValidator();

        thrown.expect(MQLQueryNotValidException.class);
        columnAndFunctionValidator.isValid(sqlContextStorage);
    }

    @Test
    public void selecItem에서_함수에서_groupBy절의_아이템을_함수파라미터로_선언할때() {
        String sql = "SELECT A.ID, B.ID, LENGTH(A.AGE)\n" +
                "FROM table1 A\n" +
                "JOIN table2 B ON A.ID=B.ID AND B.AGE=A.AGE\n" +
                "WHERE A.NAME='lee'\n" +
                "GROUP BY A.ID, B.ID, A.AGE"
                ;

        SqlContextStorage sqlContextStorage = new SqlContextStorage(sql);
        ItemsOfRelatedGroupByClauseValidator columnAndFunctionValidator = new ItemsOfRelatedGroupByClauseValidator();

        columnAndFunctionValidator.isValid(sqlContextStorage);
    }

    @Test
    public void having절에서_groupby에정의되지않은아이템을선언할때() {
        String sql = "SELECT A.ID, B.ID, LENGTH(A.AGE)\n" +
                "FROM table1 A\n" +
                "JOIN table2 B ON A.ID=B.ID AND B.AGE=A.AGE\n" +
                "WHERE A.NAME='lee'\n" +
                "GROUP BY A.ID, B.ID, A.AGE\n" +
                "HAVING SUM(A.ID)>LENGTH(B.AGE)"
                ;

        SqlContextStorage sqlContextStorage = new SqlContextStorage(sql);
        ItemsOfRelatedGroupByClauseValidator columnAndFunctionValidator = new ItemsOfRelatedGroupByClauseValidator();

        thrown.expect(MQLQueryNotValidException.class);
        columnAndFunctionValidator.isValid(sqlContextStorage);
    }

    @Test
    public void having절에서_from에정의되지않은_테이블의컬럼을사용할때() {
        String sql = "SELECT A.ID, B.ID, LENGTH(A.AGE)\n" +
                "FROM table1 A\n" +
                "JOIN table2 B ON A.ID=B.ID AND B.AGE=A.AGE\n" +
                "WHERE A.NAME='lee'\n" +
                "GROUP BY A.ID, B.ID, A.AGE\n" +
                "HAVING SUM(A.ID)>LENGTH(C.AGE)"
                ;

        SqlContextStorage sqlContextStorage = new SqlContextStorage(sql);
        ItemsOfRelatedGroupByClauseValidator columnAndFunctionValidator = new ItemsOfRelatedGroupByClauseValidator();

        thrown.expect(MQLQueryNotValidException.class);
        columnAndFunctionValidator.isValid(sqlContextStorage);
    }

    @Test
    public void 컬럼_파라미터_두개이상_함수일때() {
        String sql = "SELECT A.ID, LENGTH(A.AGE, B.ID)\n" +
                "FROM table1 A\n"
                ;

        SqlContextStorage sqlContextStorage = new SqlContextStorage(sql);
        ItemsOfRelatedGroupByClauseValidator columnAndFunctionValidator = new ItemsOfRelatedGroupByClauseValidator();

        thrown.expect(MQLQueryNotValidException.class);
        columnAndFunctionValidator.isValid(sqlContextStorage);
    }

}
