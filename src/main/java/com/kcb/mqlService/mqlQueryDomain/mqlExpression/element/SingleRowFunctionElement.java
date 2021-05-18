package com.kcb.mqlService.mqlQueryDomain.mqlExpression.element;

import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.MQLElement;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class SingleRowFunctionElement implements MQLElement {

    private List<MQLElement> parameters;
    private String dataSourceIdForRow;

    public SingleRowFunctionElement(MQLElement ... parameters) {
        this.parameters = Arrays.asList(parameters);
        setDataSourceIdForRow();

    }

    private void setDataSourceIdForRow() {
        int columnCount = 0;
        for (MQLElement each : parameters) {
            if (each instanceof ColumnElement) {
                columnCount += 1;
                dataSourceIdForRow = ((ColumnElement) each).getDataSourceId();
            } else if (each instanceof SingleRowFunctionElement) {
                columnCount += 1;
                dataSourceIdForRow = ((SingleRowFunctionElement) each).getDataSourceIdForRow();
            }
        }

        if (columnCount > 1)
            throw new RuntimeException("Function can't have more than one column");
    }

    public Object executeAbout(Map<String, Object> singleRow) {
        return executeWith(parameters, singleRow);
    }

    protected abstract Object executeWith(List<MQLElement> parameters, Map<String, Object> singleRow);
    public  String getDataSourceIdForRow() {
        return dataSourceIdForRow;
    }
}
