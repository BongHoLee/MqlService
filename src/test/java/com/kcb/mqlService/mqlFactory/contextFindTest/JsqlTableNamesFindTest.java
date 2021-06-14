package com.kcb.mqlService.mqlFactory.contextFindTest;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.FromItemVisitorAdapter;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectVisitorAdapter;

import java.util.*;

public class JsqlTableNamesFindTest {
    private boolean leftIsColumn = false;
    private boolean rightIsColumn = false;





    public void test() throws JSQLParserException {
        String sql = "SELECT A.CustomerID AS CustomerID, B.CategoryID, SUM(LENGTH(A.ID)), LENGTH(A.ID), LENGTH('hi')\n" +
                "FROM Customers A \n" +
                "INNER JOIN Categories B ON (A.CustomerID = B.CategoryID) AND (A.CustomerID2 = B.CategoryID2) \n" +
                "INNER JOIN Employees C ON A.CustomerID = C.EmployeeID\n" +
                "INNER JOIN Employees D ON D.CustomerID = LENGTH(C.EmployeeID)\n" +
                "INNER JOIN Employees E ON LENGTH(A.CustomerID) = B.Column\n" +
                "INNER JOIN Employees E ON A.CustomerID = 1\n" +
                "WHERE A.ID=B.ID AND C.ID=D.ID\n" +
                "GROUP BY A.ID, B.ID\n" +
                "HAVING SUM(LENGTH(A.NAME)) > B.ID";




        Select select = (Select) CCJSqlParserUtil.parse(sql);

        select.getSelectBody().accept(new SelectVisitorAdapter() {
            Map<String, String> tableAliasName = new HashMap<>();
            Set<String> usedJoinSet = new HashSet<>();
            Column leftColumn = null;
            Column rightColumn = null;

            @Override
            public void visit(PlainSelect plainSelect) {

                plainSelect.getFromItem().accept(new FromItemVisitorAdapter() {

                    @Override
                    public void visit(Table table) {
                        tableAliasName.put(table.getAlias().getName(), table.getFullyQualifiedName());
                    }
                });


                plainSelect.getJoins().forEach(eachJoin -> {

                    eachJoin.getRightItem().accept(new FromItemVisitorAdapter() {
                        @Override
                        public void visit(Table table) {
                            tableAliasName.put(table.getAlias().getName(), table.getFullyQualifiedName());
                        }
                    });

                    eachJoin.getOnExpression().accept(new ExpressionVisitorAdapter(){

                        protected void visitBinaryExpression(BinaryExpression expr) {
                            if (expr instanceof AndExpression || expr instanceof OrExpression) {
                                expr.getLeftExpression().accept(this);
                                expr.getRightExpression().accept(this);
                            } else {

                                expr.getLeftExpression().accept(new ExpressionVisitorAdapter() {
                                    @Override
                                    public void visit(Column column) {
                                        leftColumn = column;

                                    }
                                });

                                expr.getRightExpression().accept(new ExpressionVisitorAdapter() {
                                    @Override
                                    public void visit(Column column) {
                                        if (leftColumn != null) {
                                            rightColumn = column;
                                            usedJoinSet.add(leftColumn.getTable().getName());
                                            usedJoinSet.add(rightColumn.getTable().getName());
                                        }
                                    }
                                });

                                System.out.println("left : " + leftIsColumn + ", right : " + rightIsColumn);
                                leftColumn = null;
                                rightColumn = null;
                            }


                        }

                    });


                });


                System.out.println(tableAliasName);
                System.out.println(usedJoinSet);
                System.out.println(tableAliasName.keySet().containsAll(usedJoinSet));
            }

        });
    }

    private void setLeftColumn(boolean leftColumn) {
        this.leftIsColumn = leftColumn;
    }

    private void setRightColumn(boolean rightColumn) {
        this.rightIsColumn = rightColumn;
    }
}
