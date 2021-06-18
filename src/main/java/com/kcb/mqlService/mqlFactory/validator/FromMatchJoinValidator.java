package com.kcb.mqlService.mqlFactory.validator;

import com.kcb.mqlService.mqlFactory.SqlContextStorage;
import com.kcb.mqlService.mqlFactory.exception.MQLQueryNotValidException;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectVisitorAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FromMatchJoinValidator implements MQLValidator {
    private static final Logger logger = LogManager.getLogger(FromMatchJoinValidator.class);

    @Override
    public boolean isValid(SqlContextStorage sqlContextStorage) {
        return tableCountWithJoinAndWhereIsValid(sqlContextStorage);
    }


    private boolean tableCountWithJoinAndWhereIsValid(SqlContextStorage sqlContextStorage) {
        Select select = sqlContextStorage.getSelect();

        select.getSelectBody().accept(new SelectVisitorAdapter() {

            Set<String> usedJoinSet = new HashSet<>();
            Column leftColumn = null;
            Column rightColumn = null;

            @Override
            public void visit(PlainSelect plainSelect) {

                if (plainSelect.getJoins() != null) {
                    plainSelect.getJoins().forEach(eachJoin -> {


                        if (eachJoin.getOnExpression() != null) {
                            eachJoin.getOnExpression().accept(new ExpressionVisitorAdapter() {
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

                                        leftColumn = null;
                                        rightColumn = null;
                                    }
                                }
                            });
                        }
                    });
                }

                if (plainSelect.getWhere() != null) {
                    plainSelect.getWhere().accept(new ExpressionVisitorAdapter() {

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


                                leftColumn = null;
                                rightColumn = null;
                            }
                        }
                    });
                }

                validCheck(sqlContextStorage.getUsedTableAliasWithName(), usedJoinSet);
            }

        });

        return true;
    }

    private void validCheck(Map<String, String> tableAliasName, Set<String> usedJoin) {
        if (!usedJoin.equals(tableAliasName.keySet()) && tableAliasName.keySet().size() > 1) {
            logger.error("Used Table : {} | Joined Table: {}", tableAliasName.keySet(), usedJoin);
            throw new MQLQueryNotValidException("Used Table : " + tableAliasName.keySet() + " | Joined Table : " + usedJoin);
        }

    }
}
