package com.kcb.mqlService.mqlFactory.validator;

import com.kcb.mqlService.mqlFactory.SqlContextStorage;
import com.kcb.mqlService.mqlFactory.exception.MQLQueryNotValidException;
import com.kcb.mqlService.utils.DefinedFunction;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.AllTableColumns;
import net.sf.jsqlparser.statement.select.PlainSelect;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * Columns And Function Validate For 'Join', 'Where', 'Group By'
 *
 */

public class ItemsOfGeneralClauseValidator implements MQLValidator{
    private static final Logger logger = LoggerFactory.getLogger(ItemsOfGeneralClauseValidator.class);

    @Override
    public boolean isValid(SqlContextStorage sqlContextStorage) {

        ExpressionVisitorAdapter visitor = new ExpressionVisitorAdapter() {
            @Override
            public void visit(Function function) {
                parameterValidCheck(sqlContextStorage.getQueryId(), function);
                functionValidCheck(function, sqlContextStorage);
            }

            @Override
            public void visit(Column column) {
                validColumnCheck(sqlContextStorage.getQueryId(), column, sqlContextStorage);
            }

            @Override
            public void visit(AllColumns allColumns) {
                Column allColumn = new Column(allColumns.toString());
                validColumnCheck(sqlContextStorage.getQueryId(), allColumn, sqlContextStorage);
            }

            @Override
            public void visit(AllTableColumns allTableColumns) {
                Column allColumnTables = new Column(allTableColumns.getTable(), "*");
                validColumnCheck(sqlContextStorage.getQueryId(), allColumnTables, sqlContextStorage);
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

    private void validColumnCheck(String queryId, Column column, SqlContextStorage sqlContextStorage) {
        Map<String, String> tableAliasAndNames = sqlContextStorage.getUsedTableAliasWithName();

        if (column.getTable() == null || !tableAliasAndNames.containsKey(column.getTable().getName())) {
            logger.error("Query ID : {}, Column {} is not valid. check out defined Table : {}", queryId, column.toString(), tableAliasAndNames);
            throw new MQLQueryNotValidException(queryId + "is not valid query");
        }

    }

    private void functionValidCheck(Function function, SqlContextStorage sqlContextStorage) {
        if (DefinedFunction.GROUP_FUNCTION.getDefinedFunctionList().contains(function.getName())) {
            logger.error("Query ID : {}, Group Function {} can't used in clause", sqlContextStorage.getQueryId(), function);
            throw new MQLQueryNotValidException(sqlContextStorage.getQueryId() + "is not valid query");
        } else if (!DefinedFunction.SINGLE_ROW_FUNCTION.getDefinedFunctionList().contains(function.getName())) {
            logger.error("Query ID : {}, {} is can't use. defined function : {}, {}", sqlContextStorage.getQueryId(), function.toString(), DefinedFunction.GROUP_FUNCTION.getDefinedFunctionList(), DefinedFunction.SINGLE_ROW_FUNCTION.getDefinedFunctionList());
            throw new MQLQueryNotValidException(sqlContextStorage.getQueryId() + "is not valid query");
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
                            logger.error("Query ID : {}, item {} is not valid. table {} is not defined", sqlContextStorage.getQueryId(), function.toString(), column.getTable());
                            throw new MQLQueryNotValidException(sqlContextStorage.getQueryId() + "is not valid query");
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

    private void parameterValidCheck(String queryID, Function function) {
        List<String> temp = new ArrayList<>();

        if (DefinedFunction.GROUP_FUNCTION.getDefinedFunctionList().contains(function.getName())) {
            if (function.getParameters().getExpressions().size() > 1) {
                logger.error("Query ID : {}, Group Function can have only one parameter ", queryID);
                throw new MQLQueryNotValidException(queryID + "is not valid query");
            }
        }

        function.getParameters().getExpressions().forEach(expression -> {

            expression.accept(new ExpressionVisitorAdapter() {

                @Override
                public void visit(Column column) {
                    temp.add(column.getName(true));
                    super.visit(column);
                }
            });
        });

        if (temp.size() > 1) {
            logger.error("Query ID : {}, Function couldn't more than one Column Parameters : Function : {}, Columns : {} ", queryID, function.getName(), temp);
            throw new MQLQueryNotValidException(queryID + "is not valid query");
        }
    }


}
