package com.kcb.mqlService.mqlFactory.validator;

import com.kcb.mqlService.mqlFactory.SqlContextStorage;
import com.kcb.mqlService.mqlFactory.exception.MQLQueryNotValidException;
import com.kcb.mqlService.utils.DefinedFunction;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItemVisitorAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

public class ItemsOfRelatedGroupByClauseValidator implements MQLValidator{
    private static final Logger logger = LogManager.getLogger(ItemsOfRelatedGroupByClauseValidator.class);

    @Override
    public boolean isValid(SqlContextStorage sqlContextStorage) {
        Map<String, String> tableAliasAndNames = sqlContextStorage.getUsedTableAliasWithName();
        List<String> groupByNames = sqlContextStorage.getGroupByElementsNames();

        ExpressionVisitor visitor = new ExpressionVisitorAdapter() {

            @Override
            public void visit(Function function) {
                functionValidCheck(function, sqlContextStorage);
            }

            @Override
            public void visit(Column column) {
                validColumnCheck(column, tableAliasAndNames, groupByNames);
            }
        };

        return selectItemsValid(visitor, sqlContextStorage) && havingItemValidCheck(visitor, sqlContextStorage);
    }


    // 1. selectItem validate
    private boolean selectItemsValid(ExpressionVisitor visitor, SqlContextStorage sqlContextStorage) {
        sqlContextStorage.getPlainSelect().getSelectItems().forEach(each -> {
            each.accept(new SelectItemVisitorAdapter() {
                @Override
                public void visit(SelectExpressionItem item) {
                    item.getExpression().accept(visitor);
                }
            });
        });

        return true;

    }

    // 2. having column and function validation
    private boolean havingItemValidCheck(ExpressionVisitor visitor, SqlContextStorage sqlContextStorage) {
        PlainSelect plainSelect = sqlContextStorage.getPlainSelect();
        if (plainSelect.getHaving() != null) {
            plainSelect.getHaving().accept(visitor);
        }

        return true;
    }

    private void functionValidCheck(Function function, SqlContextStorage sqlContextStorage) {
        List<String> groupByNames = sqlContextStorage.getGroupByElementsNames();

        if (DefinedFunction.GROUP_FUNCTION.getDefinedFunctionList().contains(function.getName())) {
            functionParameterDefinedCheck(function, sqlContextStorage);

        } else if (DefinedFunction.SINGLE_ROW_FUNCTION.getDefinedFunctionList().contains(function.getName())) {

            functionParameterDefinedCheck(function, sqlContextStorage);

            if (groupByNames != null && groupByNames.size() > 0) {

                if (!groupByNames.contains(function.toString())) {

                    if (function.getParameters().getExpressions() != null) {
                        function.getParameters().getExpressions().forEach(eachParam -> {
                            eachParam.accept(new ExpressionVisitorAdapter() {

                                @Override
                                public void visit(Column column) {
                                    if (!groupByNames.contains(column.toString())) {
                                        logger.error("item {} is not valid. check out group by : {}", function.toString(), groupByNames);
                                        throw new MQLQueryNotValidException(function + " not valid in select item");
                                    }
                                }
                            });
                        });
                    }
                }
            }

        } else {
            logger.error("{} is can't use. defined function : {}, {}", function.toString(), DefinedFunction.GROUP_FUNCTION.getDefinedFunctionList(), DefinedFunction.SINGLE_ROW_FUNCTION.getDefinedFunctionList());
            throw new MQLQueryNotValidException(function + " can't use");
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

    private void validColumnCheck(Column column, Map<String, String> tableAliasAndNames, List<String> groupByNames) {
        if (!tableAliasAndNames.containsKey(column.getTable().getName())) {
            logger.error("Column {} is not valid. check out defined Table : {}", column.toString(), tableAliasAndNames);
            throw new MQLQueryNotValidException();
        } else if (!(groupByNames.size() == 0 || groupByNames.contains(column.toString()))) {
            logger.error("Column {} is not valid. check out group by : {}", column.toString(), groupByNames);
            throw new MQLQueryNotValidException();
        }
    }


}
