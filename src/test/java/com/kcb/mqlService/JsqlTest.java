package com.kcb.mqlService;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class JsqlTest {

    private String selectClause;
    private String whereCondition;
    private Select select;
    private PlainSelect plainSelect;

    @Before
    public void beforeSet() throws JSQLParserException {
        //selectClause = "SELECT DS1.key, DS2.key FROM DS1, DS2 WHERE (DS1.a=3 AND DS2.=3) AND DS1.key=5";
        selectClause = "SELECT DS1.column, DS2.column FROM DS1, DS2 WHERE (DS1.key = DS2.key) AND DS1.a = 1";
        whereCondition = "(a.key=3 AND b.key=4) OR c=5";

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

}
