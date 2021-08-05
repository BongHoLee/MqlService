package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.optionalClause;

import com.kcb.mqlService.mqlFactory.exception.MQLQueryExecuteException;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.ColumnElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.MQLElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.OrderByMQLElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.SingleRowFunctionElement;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.OptionalClause;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class OrderByClause implements OptionalClause {
    private static final Logger logger = LoggerFactory.getLogger(OrderByClause.class);

    private List<OrderByMQLElement> orderByMQLElements;

    public OrderByClause(){}

    public OrderByClause(List<OrderByMQLElement> orderByMQLElements) {
        this.orderByMQLElements = orderByMQLElements;

    }

    public void setOrderByElements(List<OrderByMQLElement> orderByMQLElements) {
        this.orderByMQLElements = orderByMQLElements;
    }


    @Override
    public MQLDataStorage executeClause(MQLDataStorage mqlDataStorage) {
        MQLTable table = mqlDataStorage.getMqlTable();
        List<Map<String, Object>> tableData = table.getTableData();

        tableData.sort(
                (row1, row2) -> {
                    int compareValue = 0;
                    for (int i = 0; i< orderByMQLElements.size(); i++) {
                        MQLElement element = orderByMQLElements.get(i).getElement();
                        int ascValue = orderByMQLElements.get(i).getAsc() ? 1 : -1;

                        // 1. element is COLUMN
                        if (element instanceof ColumnElement) {
                            if (!row1.containsKey(((ColumnElement) element).getColumnName()) || !row2.containsKey(((ColumnElement) element).getColumnName())) {
                                logger.error("Query Id : {}, ORDER BY Element {} is not valid : ", mqlDataStorage.getQueryID(), ((ColumnElement) element).getColumnName());
                                throw new MQLQueryExecuteException("ORDER BY Element is not valid Exception");
                            }

                            // value is number
                            if (row1.get(((ColumnElement) element).getColumnName()) instanceof Number && row2.get(((ColumnElement) element).getColumnName()) instanceof Number) {
                                Number row1Number = ((Number) row1.get(((ColumnElement) element).getColumnName()));
                                Number row2Number = ((Number) row2.get(((ColumnElement) element).getColumnName()));

                                if (row1Number.doubleValue() > row2Number.doubleValue()) {
                                    compareValue = ascValue;
                                    break;
                                } else if (row1Number.doubleValue() < row2Number.doubleValue()) {
                                    compareValue = ascValue * -1;
                                    break;
                                } else {
                                    continue;
                                }
                            // value is String
                            } else if(row1.get(((ColumnElement) element).getColumnName()) instanceof String && row2.get(((ColumnElement) element).getColumnName()) instanceof String) {
                                String row1String = (String) row1.get(((ColumnElement) element).getColumnName());
                                String row2String = (String) row2.get(((ColumnElement) element).getColumnName());

                                compareValue = row1String.compareTo(row2String) * ascValue;
                                break;
                            }
                        }

                        // 2. element is SINGLE_ROW_FUNCTION
                        if (element instanceof SingleRowFunctionElement) {
                            if (row1.containsKey(element.getElementExpression()) && row2.containsKey(element.getElementExpression())) {
                                if (row1.get(element.getElementExpression()) instanceof Number && row1.get(element.getElementExpression()) instanceof Number) {
                                    Number row1Number = ((Number) row1.get(element.getElementExpression()));
                                    Number row2Number = ((Number) row1.get(element.getElementExpression()));

                                    if (row1Number.doubleValue() > row2Number.doubleValue()) {
                                        compareValue = ascValue;
                                        break;
                                    } else if (row1Number.doubleValue() < row2Number.doubleValue()) {
                                        compareValue = ascValue * -1;
                                        break;
                                    } else {
                                        continue;
                                    }
                                } else if (row1.get(element.getElementExpression()) instanceof String && row1.get(element.getElementExpression()) instanceof String) {
                                    String row1String = (String) row1.get(element.getElementExpression());
                                    String row2String = (String) row2.get(element.getElementExpression());

                                    compareValue = row1String.compareTo(row2String) * ascValue;
                                    break;
                                }
                            } else {
                                if (((SingleRowFunctionElement) element).executeAbout(row1) instanceof Number && ((SingleRowFunctionElement) element).executeAbout(row2) instanceof Number) {
                                    Number functionResult1 = ((Number) ((SingleRowFunctionElement) element).executeAbout(row1));
                                    Number functionResult2 = ((Number) ((SingleRowFunctionElement) element).executeAbout(row2));

                                    if (functionResult1.doubleValue() > functionResult2.doubleValue()) {
                                        compareValue = ascValue;
                                        break;
                                    } else if (functionResult1.doubleValue() < functionResult2.doubleValue()) {
                                        compareValue = ascValue * -1;
                                        break;
                                    } else {
                                        continue;
                                    }
                                } else if (((SingleRowFunctionElement) element).executeAbout(row1) instanceof String && ((SingleRowFunctionElement) element).executeAbout(row2) instanceof String) {
                                    String functionResult1 = ((String) ((SingleRowFunctionElement) element).executeAbout(row1));
                                    String functionResult2 = ((String) ((SingleRowFunctionElement) element).executeAbout(row2));

                                    compareValue = functionResult1.compareTo(functionResult2) * ascValue;
                                    break;
                                }
                            }
                        }

                    }

                    return compareValue;
                }
        );

        return mqlDataStorage;

    }





}
