package com.kcb.mqlService.mqlFactory.validator;

import com.kcb.mqlService.mqlFactory.SqlContextStorage;
import com.kcb.mqlService.mqlFactory.exception.MQLQueryNotValidException;
import com.kcb.mqlService.utils.DefinedFunction;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.PlainSelect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class ItemsOfGeneralClauseValidator implements MQLValidator{
    private static final Logger logger = LogManager.getLogger(ItemsOfGeneralClauseValidator.class);

    @Override
    public boolean isValid(SqlContextStorage sqlContextStorage) {

        ExpressionVisitor visitor = new ExpressionVisitorAdapter() {
            @Override
            public void visit(Function function) {
                functionValidCheck(function, sqlContextStorage);
            }

            @Override
            public void visit(Column column) {
                validColumnCheck(column, sqlContextStorage);
            }
        };

        return joinAndWhereItemsValid(visitor, sqlContextStorage) && groupByItemValidCheck(visitor, sqlContextStorage);
    }

    // 1. Join and Where item check
    private boolean joinAndWhereItemsValid(ExpressionVisitor visitor, SqlContextStorage sqlContextStorage) {
        PlainSelect plainSelect = sqlContextStorage.getPlainSelect();
        if (plainSelect.getJoins() != null) {
            plainSelect.getJoins().forEach(eachJoin -> {
                if (eachJoin.getOnExpression() != null) {
                    eachJoin.getOnExpression().accept(visitor);
                }
            });

        }

        if (plainSelect.getWhere() != null) {
            plainSelect.getWhere().accept(visitor);
        }

        return true;
    }

    // 2. group by column and function validation
    private boolean groupByItemValidCheck(ExpressionVisitor visitor, SqlContextStorage sqlContextStorage) {
        PlainSelect plainSelect = sqlContextStorage.getPlainSelect();
        if (plainSelect.getGroupBy() != null) {
            if (plainSelect.getGroupBy().getGroupByExpressions() != null) {
                plainSelect.getGroupBy().getGroupByExpressions().forEach(eachExpression -> {
                    eachExpression.accept(visitor);
                });
            }
        }

        return true;
    }

    private void validColumnCheck(Column column, SqlContextStorage sqlContextStorage) {
        Map<String, String> tableAliasAndNames = sqlContextStorage.getUsedTableAliasWithName();

        if (!tableAliasAndNames.containsKey(column.getTable().getName())) {
            logger.error("Column {} is not valid. check out defined Table : {}", column.toString(), tableAliasAndNames);
            throw new MQLQueryNotValidException();
        }
    }

    private void functionValidCheck(Function function, SqlContextStorage sqlContextStorage) {
        if (DefinedFunction.GROUP_FUNCTION.getDefinedFunctionList().contains(function.getName())) {
            logger.error("Group Function {} can't used in clause", function);
            throw new MQLQueryNotValidException();
        } else if (!DefinedFunction.SINGLE_ROW_FUNCTION.getDefinedFunctionList().contains(function.getName())) {
            logger.error("{} is can't use. defined function : {}, {}", function.toString(), DefinedFunction.GROUP_FUNCTION.getDefinedFunctionList(), DefinedFunction.SINGLE_ROW_FUNCTION.getDefinedFunctionList());
            throw new MQLQueryNotValidException();
        } else if (DefinedFunction.SINGLE_ROW_FUNCTION.getDefinedFunctionList().contains(function.getName())) {
            functionParameterDefinedCheck(function, sqlContextStorage);
        }
    }

    private void functionParameterDefinedCheck(Function function, SqlContextStorage sqlContextStorage) {
        Map<String, String> tableAliasAndNames = sqlContextStorage.getUsedTableAliasWithName();

        if (function.getParameters().getExpressions() != null) {
            function.getParameters().getExpressions().forEach(eachParam -> {
                eachParam.accept(new ExpressionVisitorAdapter() {

                    @Override
                    public void visit(Column column) {
                        if (!tableAliasAndNames.containsKey(column.getTable().getName())) {
                            logger.error("item {} is not valid. table {} is not defined", function.toString(), column.getTable());
                            throw new MQLQueryNotValidException(function + " not valid");
                        }
                    }

                    @Override
                    public void visit(Function function) {
                        functionValidCheck(function, sqlContextStorage);
                    }
                });
            });
        }
    }


}
