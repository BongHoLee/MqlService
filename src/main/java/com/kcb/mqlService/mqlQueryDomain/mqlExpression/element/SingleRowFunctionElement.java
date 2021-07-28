package com.kcb.mqlService.mqlQueryDomain.mqlExpression.element;

import com.kcb.mqlService.mqlFactory.exception.MQLQueryExecuteException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class SingleRowFunctionElement implements MQLElement {

    private List<MQLElement> parameters;
    private String dataSourceIdForRow;
    private SingleRowFunctionParameterType singleRowFunctionParameterType;
    private String columnParameterName;

    public SingleRowFunctionElement(){}

    public SingleRowFunctionElement(MQLElement ... parameters) {
        this.parameters = Arrays.asList(parameters);
        setFunctionType();
    }

    public SingleRowFunctionElement(List<MQLElement> parameters) {
        this.parameters = parameters;
        setFunctionType();
    }

    public void setParameters(List<MQLElement> parameters) {
        this.parameters = parameters;
        setFunctionType();
    }

    private void setFunctionType() {
        int columnCount = 0;
        for (MQLElement each : parameters) {
            if (each instanceof ColumnElement) {
                columnCount += 1;
                dataSourceIdForRow = ((ColumnElement) each).getDataSourceId();
                singleRowFunctionParameterType = SingleRowFunctionParameterType.COLUMN;
                columnParameterName = ((ColumnElement) each).getColumnName();
            } else if (each instanceof SingleRowFunctionElement) {
                columnCount += 1;
                dataSourceIdForRow = ((SingleRowFunctionElement) each).getDataSourceIdForRow();
                columnParameterName = ((SingleRowFunctionElement) each).getColumnParameterName();
                if (dataSourceIdForRow != null && !dataSourceIdForRow.isEmpty()) {
                    singleRowFunctionParameterType = SingleRowFunctionParameterType.COLUMN;
                }
            }
        }

        if (columnCount > 1)
            throw new RuntimeException("Function can't have more than one column");
        else if (columnCount == 0)
            singleRowFunctionParameterType = SingleRowFunctionParameterType.VALUE;
    }

    public Object executeAbout(Map<String, Object> singleRow) {
        return executeWith(parameters, singleRow);
    }

    protected abstract Object executeWith(List<MQLElement> parameters, Map<String, Object> singleRow);

    public  String getDataSourceIdForRow() {
        return dataSourceIdForRow;
    }

    public boolean hasColumn() {
        return singleRowFunctionParameterType.isColumn();
    }

    public String getColumnParameterName() {
        return columnParameterName;
    }

    protected boolean isNullParam(Object param, Map<String, Object> singleRow) {
         if (param instanceof ValueElement && ((ValueElement) param).getValue() == null) {
             return true;
        }

         if (param instanceof ColumnElement && singleRow.get(((ColumnElement) param).getColumnName()) == null) {
             return true;
         }

         if (param == null) {
             return true;
         }

         return false;
    }
}
