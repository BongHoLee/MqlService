package com.kcb.mqlService;

import com.kcb.mqlService.mqlFactory.contextFindTest.JsqlTableNamesFindTest;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.parser.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.util.TablesNamesFinder;
import net.sf.jsqlparser.util.validation.*;
import net.sf.jsqlparser.util.validation.feature.DatabaseType;
import net.sf.jsqlparser.util.validation.validator.StatementValidator;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class JsqlTest {

    private String whereCondition;
    private Select select;
    private PlainSelect plainSelect;
    private CCJSqlParserManager parserManager;

    @Before
    public void beforeSet() throws JSQLParserException {
        boolean leftColumn = false;
        boolean rightColumn = false;

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
                " OR C.NAME = 1" +
                " OR C.ID = 1.0";

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
        String sql = "SELECT A.CustomerID AS CustomerID, B.CategoryID, SUM(LENGTH(A.ID)), LENGTH(A.ID), LENGTH('hi')\n" +
                "FROM Customers A \n" +
                "INNER JOIN Categories B ON A.CustomerID = B.CategoryID\n" +
                "INNER JOIN Employees C ON A.CustomerID = C.EmployeeID\n" +
                "WHERE A.ID=B.ID AND C.ID=D.ID\n" +
                "GROUP BY A.ID, B.ID\n" +
                "HAVING SUM(LENGTH(A.NAME)) > B.ID";

        Select select = (Select) parserManager.parse(new StringReader(sql));
        TablesNamesFinder tblFinder = new TablesNamesFinder();
        PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
        List<String> tableNames = tblFinder.getTableList(select);

        String fullyNames = ((Table)plainSelect.getFromItem()).getFullyQualifiedName();
        List<Column> columns = new ArrayList<>();

        assertThat(Arrays.asList("Customers", "Categories", "Employees"), equalTo(tableNames));
    }

    @Test
    public void groupbyTest() throws JSQLParserException {
        String sql = "SELECT CustomerID, LENGTH(CustomerID), SUM(main)\n" +
                "FROM Customers A, Categories B\n" +
                "GROUP BY CustomerID, ProductID";
        Select select = (Select) parserManager.parse(new StringReader(sql));
        PlainSelect plainSelect = (PlainSelect) select.getSelectBody();

        TablesNamesFinder tblFinder = new TablesNamesFinder();
        List<String> tableNames = tblFinder.getTableList(select);



        System.out.println(plainSelect);
    }

    @Test
    public void jsqlSelectItemsFindTest() throws JSQLParserException {
        String sql = "SELECT A.CustomerID, B.CategoryID, SUM(LENGTH(A.ID)), LENGTH(A.ID), COUNT(A.ID) \n" +
                "FROM Customers A, Categories B \n" +
                "WHERE A.ID=B.ID AND C.ID=D.ID\n" +
                "GROUP BY A.ID, B.ID\n" +
                "HAVING SUM(LENGTH(A.NAME)) > B.ID";




        Select select = (Select) CCJSqlParserUtil.parse(sql);

        select.getSelectBody().accept(new SelectVisitorAdapter() {

            @Override
            public void visit(PlainSelect plainSelect) {
                for (SelectItem selectItem : plainSelect.getSelectItems()) {
                    selectItem.accept(new SelectItemVisitorAdapter() {

                        @Override
                        public void visit(AllColumns allColumns) {
                            System.out.println(allColumns);
                        }


                        @Override
                        public void visit(SelectExpressionItem selectExpressionItem) {
                            selectExpressionItem.getExpression().accept(new ExpressionVisitorAdapter() {

                                @Override
                                public void visit(Function function) {
                                    System.out.println(function);


                                }

                                @Override
                                public void visit(Column column) {
                                    System.out.println(column);

                                }
                            });
                        }
                    });
                }
            }

        });
    }

    @Test
    public void jsqlTableNamesFindTest() throws JSQLParserException {
        JsqlTableNamesFindTest test = new JsqlTableNamesFindTest();
        test.test();
    }




}
