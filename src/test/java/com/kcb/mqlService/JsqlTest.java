package com.kcb.mqlService;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.parser.*;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class JsqlTest {

    private String whereCondition;
    private Select select;
    private PlainSelect plainSelect;
    private CCJSqlParserManager parserManager;

    @Before
    public void beforeSet() throws JSQLParserException {

        parserManager = new CCJSqlParserManager();

        String selectClause = "SELECT DS1.column, DS2.column FROM DS1, DS2 WHERE DS1.key = DS2.key";
        whereCondition = "(a.key=b.key)";


        select = (Select) CCJSqlParserUtil.parse(selectClause);
        plainSelect = (PlainSelect) select.getSelectBody();

    }

    @Test
    public void whereConditionTest() throws JSQLParserException {
        Expression whereExpr = CCJSqlParserUtil.parseCondExpression(whereCondition);

        whereExpr.accept(new ExpressionVisitorAdapter() {
            @Override
            protected  void visitBinaryExpression(BinaryExpression expr) {
                System.out.println(expr.toString());
                expr.getLeftExpression().accept(this);
                expr.getRightExpression().accept(this);

                //super.visitBinaryExpression(expr);
            }
        });
    }

    @Test
    public void getFromTablesFromSelect() {
        TablesNamesFinder tbsFinder = new TablesNamesFinder();
        List<String> tables = tbsFinder.getTableList(select);
        assertThat(tables, equalTo(Arrays.asList("DS1", "DS2")));
    }

    @Test
    public void oneLeftOuterJoinTest() throws JSQLParserException {
        String sql = "SELECT A.*, B.* " +
                "FROM Customers A  " +
                "INNER JOIN Categories B ON A.CustomerID=B.CategoryID" +
                " AND A.CITY!='Berlin'" +
                " OR C.CITY != 'Seoul'" +
                " OR C.NAME = 1";
        Select select = (Select) parserManager.parse(new StringReader(sql));
        PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
        assertThat(1, equalTo(plainSelect.getJoins().size()));

    }

    @Test
    public void doubleJoinTestWithoutOnCondition() throws JSQLParserException {
        String sql = "SELECT A.CustomerID, B.CategoryID\n" +
                "FROM Customers A\n" +
                "INNER JOIN Categories B ON A.CustomerID = B.CategoryID\n" +
                "LEFT OUTER JOIN Employees C ON A.CustomerID = C.EmployeeID";

        Select select = (Select) parserManager.parse(new StringReader(sql));
        PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
        assertThat(2, equalTo(plainSelect.getJoins().size()));

    }

    @Test
    public void getTableListTest() throws JSQLParserException {
        String sql = "SELECT A.CustomerID, B.CategoryID\n" +
                "FROM Customers A\n" +
                "INNER JOIN Categories B ON A.CustomerID = B.CategoryID\n" +
                "INNER JOIN Employees C ON A.CustomerID = C.EmployeeID";

        Select select = (Select) parserManager.parse(new StringReader(sql));
        TablesNamesFinder tblFinder = new TablesNamesFinder();
        List<String> tableNames = tblFinder.getTableList(select);

        assertThat(Arrays.asList("Customers", "Categories", "Employees"), equalTo(tableNames));
    }



}
