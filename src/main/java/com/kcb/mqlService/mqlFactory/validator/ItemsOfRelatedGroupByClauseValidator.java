package com.kcb.mqlService.mqlFactory.validator;

import com.kcb.mqlService.mqlFactory.SqlContextStorage;
import com.kcb.mqlService.mqlFactory.exception.MQLQueryNotValidException;
import com.kcb.mqlService.utils.DefinedFunction;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.*;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * Columns and Functions validate for 'Select Items', 'Having'
 *
 */

public class ItemsOfRelatedGroupByClauseValidator implements MQLValidator{
    private static final Logger logger = LoggerFactory.getLogger(ItemsOfRelatedGroupByClauseValidator.class);

    @Override
    public boolean isValid(SqlContextStorage sqlContextStorage) {
        Map<String, String> tableAliasAndNames = sqlContextStorage.getUsedTableAliasWithName();
        List<String> groupByNames = sqlContextStorage.getGroupByElementsNames();

        ExpressionVisitorAdapter visitor = new ExpressionVisitorAdapter() {

            @Override
            public void visit(Function function) {
                parameterValidCheck(sqlContextStorage.getQueryId(), function);
                functionValidCheck(function, rootFunctionIsGroupFunction(function), sqlContextStorage);
            }

            @Override
            public void visit(Column column) {
                validColumnCheck(sqlContextStorage.getQueryId(), column, tableAliasAndNames, groupByNames);
            }

            @Override
            public void visit(AllColumns allColumns) {
                Column allColumn = new Column(allColumns.toString());
                validColumnCheck(sqlContextStorage.getQueryId(), allColumn, tableAliasAndNames, groupByNames);
            }

            @Override
            public void visit(AllTableColumns allTableColumns) {
                Column allColumnTables = new Column(allTableColumns.getTable(), "*");
                validColumnCheck(sqlContextStorage.getQueryId(), allColumnTables, tableAliasAndNames, groupByNames);
            }

        };

        return selectItemsValid(visitor, sqlContextStorage) && havingItemsValid(visitor, sqlContextStorage);
    }


    // 1. selectItem validate
    private boolean selectItemsValid(ExpressionVisitorAdapter visitor, SqlContextStorage sqlContextStorage) {
        sqlContextStorage.getPlainSelect().getSelectItems().forEach(each -> {
            each.accept(new SelectItemVisitorAdapter() {
                @Override
                public void visit(SelectExpressionItem item) {
                    item.getExpression().accept(visitor);
                }

                @Override
                public void visit(AllColumns columns) {
                    visitor.visit(columns);
                }

                @Override
                public void visit(AllTableColumns columns) {
                    visitor.visit(columns);
                }
            });
        });

        return true;

    }

    // 2. having column and function validation
    private boolean havingItemsValid(ExpressionVisitor visitor, SqlContextStorage sqlContextStorage) {
        PlainSelect plainSelect = sqlContextStorage.getPlainSelect();
        if (plainSelect.getHaving() != null) {
            plainSelect.getHaving().accept(visitor);
        }

        return true;
    }

    private void functionValidCheck(Function function, boolean rootIsGroup, SqlContextStorage sqlContextStorage) {
        List<String> groupByNames = sqlContextStorage.getGroupByElementsNames();

        if (DefinedFunction.GROUP_FUNCTION.getDefinedFunctionList().contains(function.getName())) {
            functionParameterDefinedCheck(function, rootIsGroup, sqlContextStorage);

        } else if (DefinedFunction.SINGLE_ROW_FUNCTION.getDefinedFunctionList().contains(function.getName())) {

            functionParameterDefinedCheck(function, rootIsGroup, sqlContextStorage);

            if (groupByNames != null && groupByNames.size() > 0) {

                if (!groupByNames.contains(function.toString())) {

                    if (function.getParameters().getExpressions() != null) {
                        function.getParameters().getExpressions().forEach(eachParam -> {
                            eachParam.accept(new ExpressionVisitorAdapter() {

                                @Override
                                public void visit(Column column) {
                                    if (!rootIsGroup && !groupByNames.contains(column.toString())) {
                                        logger.error("Query ID : {}, item {} is not valid. check out group by : {}", sqlContextStorage.getQueryId(), function.toString(), groupByNames);
                                        throw new MQLQueryNotValidException(sqlContextStorage.getQueryId() + "is not valid query");
                                    }
                                }
                            });
                        });
                    }
                }
            }

        } else {
            logger.error("Query ID : {}, {} is can't use. defined function : {}, {}",sqlContextStorage.getQueryId(), function.toString(), DefinedFunction.GROUP_FUNCTION.getDefinedFunctionList(), DefinedFunction.SINGLE_ROW_FUNCTION.getDefinedFunctionList());
            throw new MQLQueryNotValidException(sqlContextStorage.getQueryId() + "is not valid query");
        }
    }

    private void functionParameterDefinedCheck(Function function, boolean rootIsGroup, SqlContextStorage sqlContextStorage) {
        Map<String, String> tableAliasAndNames = sqlContextStorage.getUsedTableAliasWithName();

        if (function.getParameters() != null && function.getParameters().getExpressions() != null) {
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
                        functionValidCheck(function, rootIsGroup, sqlContextStorage);
                    }
                });
            });
        }
    }

    private void validColumnCheck(String queryId, Column column, Map<String, String> tableAliasAndNames, List<String> groupByNames) {
        // '*' 표현식일 때
        if (column.getName(false).equals("*") || column.getName(false).contains(".*")) {
            if (!groupByNames.isEmpty()) {
                logger.error("Query ID : {} => '*' cannot be used with 'GROUP BY'", queryId);
                throw new MQLQueryNotValidException(queryId + "is not valid query");
            } else if (column.getName(false).contains(".*")) {
                String columnTable = column.getTable().getName();
                if (!tableAliasAndNames.containsKey(columnTable)) {
                    logger.error("Query ID : {} => table alias({}) of {} is not defined", queryId, columnTable, column.getFullyQualifiedName());
                    throw new MQLQueryNotValidException(queryId + "is not valid query");
                }
            }

        // '*' 외 표현식 일 때
        } else {
            if (column.getTable() == null || !tableAliasAndNames.containsKey(column.getTable().getName())) {
                logger.error("Query ID : {} => table alias({}) of {} is not defined", queryId, column.getTable().getName(), column.getFullyQualifiedName());
                throw new MQLQueryNotValidException(queryId + "is not valid query");
            } else if (!(groupByNames.size() == 0 || groupByNames.contains(column.toString()))) {
                logger.error("Query ID : {}, Column {} is not valid. check out group by : {}", queryId, column, groupByNames);
                throw new MQLQueryNotValidException(queryId + "is not valid query");
            }
        }
    }

    private void parameterValidCheck(String queryId, Function function) {
        List<String> temp = new ArrayList<>();

        if (DefinedFunction.GROUP_FUNCTION.getDefinedFunctionList().contains(function.getName())) {
            if (!function.isAllColumns()) {
                if (function.getParameters() != null) {
                    if (function.getParameters().getExpressions().size() > 1) {
                        logger.error("Query ID : {}, Group Function can have only one parameter ", queryId);
                        throw new MQLQueryNotValidException(queryId + "is not valid query");
                    }

                    if (function.getParameters().getExpressions().size() < 1) {
                        logger.error("Query ID : {}, Group Function must have parameter ", queryId);
                        throw new MQLQueryNotValidException(queryId + "is not valid query");
                    }
                } else {
                    logger.error("Query ID : {}, Group Function must have parameter ", queryId);
                    throw new MQLQueryNotValidException(queryId + "is not valid query");
                }
            }
        }

        if (function.getParameters() != null) {
            function.getParameters().getExpressions().forEach(expression -> {
                expression.accept(new ExpressionVisitorAdapter() {
                    @Override
                    public void visit(Column column) {
                        temp.add(column.getName(true));
                        super.visit(column);
                    }
                });
            });
        }

        if (temp.size() > 1) {
            logger.error("Query ID : {}, Function couldn't more than one Column Parameters : Function : {}, Columns : {} ", queryId, function.getName(), temp);
            throw new MQLQueryNotValidException(queryId + "is not valid query");
        }
    }

    private boolean rootFunctionIsGroupFunction(Function function) {
        if (DefinedFunction.GROUP_FUNCTION.getDefinedFunctionList().contains(function.getName())) {
            return true;
        }

        return false;
    }


}
