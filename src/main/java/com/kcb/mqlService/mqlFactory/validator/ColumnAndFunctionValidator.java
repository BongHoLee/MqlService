package com.kcb.mqlService.mqlFactory.validator;

import com.kcb.mqlService.mqlFactory.SqlContextStorage;
import com.kcb.mqlService.mqlFactory.exception.MQLQueryNotValidException;
import com.kcb.mqlService.utils.DefinedFunction;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsListVisitorAdapter;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

public class ColumnAndFunctionValidator implements MQLValidator {
    private static final Logger logger = LogManager.getLogger(ColumnAndFunctionValidator.class);

    @Override
    public boolean isValid(SqlContextStorage sqlContextStorage) {
        return selectItemsValid(sqlContextStorage) && joinAndWhereItemsValid(sqlContextStorage);
    }


    // 1. selectItem validate
    private boolean selectItemsValid(SqlContextStorage sqlContextStorage) {
        List<String> groupByNames = sqlContextStorage.getGroupByElementsNames();
        Map<String, String> tableAliasAndNames = sqlContextStorage.getUsedTableAliasWithName();

        sqlContextStorage.getPlainSelect().getSelectItems().forEach(each -> {
            each.accept(new SelectItemVisitorAdapter() {
                @Override
                public void visit(SelectExpressionItem item) {
                    item.getExpression().accept(new ExpressionVisitorAdapter() {

                        @Override
                        public void visit(Function function) {

                            if (DefinedFunction.GROUP_FUNCTION.getDefinedFunctionList().contains(function.getName())) {
                                // can use

                            } else if (DefinedFunction.SINGLE_ROW_FUNCTION.getDefinedFunctionList().contains(function.getName())) {

                                // element of group by
                                if (!groupByNames.contains(function.toString())) {

                                    function.getParameters().getExpressions().forEach(eachParam -> {
                                        eachParam.accept(new ExpressionVisitorAdapter() {

                                            @Override
                                            public void visit(Column column) {
                                                if (!groupByNames.contains(column.toString())) {
                                                    logger.error("Select item {} is not valid. check out group by : {}", function.toString(), groupByNames);
                                                    throw new MQLQueryNotValidException(function + " not valid in select item");
                                                }
                                            }
                                        });
                                    });
                                }

                            } else {
                                logger.error("{} is can't use. defined function : {}, {}", function.toString(), DefinedFunction.GROUP_FUNCTION.getDefinedFunctionList(), DefinedFunction.SINGLE_ROW_FUNCTION.getDefinedFunctionList());
                                throw new MQLQueryNotValidException(function + " can't use");
                            }
                        }

                        @Override
                        public void visit(Column column) {
                            validColumnCheck("select items", column, tableAliasAndNames, groupByNames);
                        }
                    });

                }
            });
        });

        return true;

    }

    // 2. Join and Where item check
    private boolean joinAndWhereItemsValid(SqlContextStorage sqlContextStorage) {
        Map<String, String> tableAliasAndNames = sqlContextStorage.getUsedTableAliasWithName();

        PlainSelect plainSelect = sqlContextStorage.getPlainSelect();

        if (plainSelect.getJoins() != null) {
            plainSelect.getJoins().forEach(eachJoin -> {
                if (eachJoin.getOnExpression() != null) {
                    eachJoin.getOnExpression().accept(new ExpressionVisitorAdapter() {

                        @Override
                        public void visit(Function function) {
                            if (DefinedFunction.GROUP_FUNCTION.getDefinedFunctionList().contains(function.getName())) {
                                logger.error("Group Function {} can't used in Join clause", function);
                                throw new MQLQueryNotValidException();
                            } else if (!DefinedFunction.SINGLE_ROW_FUNCTION.getDefinedFunctionList().contains(function.getName())) {
                                logger.error("Function {} can't use", function);
                                throw new MQLQueryNotValidException();
                            }

                        }

                        @Override
                        public void visit(Column column) {
                            if (!isDefinedTablesColumn(column, tableAliasAndNames)) {
                                logger.error("Column {} is not valid. check out defined Table : {}", column.toString(), tableAliasAndNames);
                                throw new MQLQueryNotValidException();
                            }
                        }
                    });
                }
            });

        }


        if (plainSelect.getWhere() != null) {

        }

        return true;
    }


    private void validColumnCheck(String point, Column column, Map<String, String> tableAliasAndNames, List<String> groupByNames) {
        if (!isDefinedTablesColumn(column, tableAliasAndNames)) {
            logger.error("Column {} is not valid in {} check out defined Table : {}", column.toString(), point, tableAliasAndNames);
            throw new MQLQueryNotValidException();
        } else if (!isGroupByElementColumn(column, groupByNames)) {
            logger.error("Column {} is not valid in {} check out group by : {}", column.toString(),point, groupByNames);
            throw new MQLQueryNotValidException();
        }
    }

    private boolean isGroupByElementColumn(Column column, List<String> groupByElements) {
        return groupByElements.size() == 0 || groupByElements.contains(column.toString());
    }

    private boolean isDefinedTablesColumn(Column column, Map<String, String> tableAliasAndNames) {
        return tableAliasAndNames.containsKey(column.getTable().getName());
    }



}
