package com.kcb.mqlService.mqlQueryDomain.mqlExpression.element;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;

public class ColumnElement implements MQLElement{
    private String columnName;
    private String dataSourceId;

    public ColumnElement(String columnName) {
        this.columnName = columnName;
        splitExpression();
    }

    public String getColumnName() {
        return columnName;
    }

    public String getDataSourceId() {
        return dataSourceId;
    }

    private void splitExpression() {
        String[] splited = columnName.split("\\.");
        if (splited.length > 0) {
            dataSourceId = splited[0];
        } else {
            throw new RuntimeException("Column MUST HAVE DataSource ID ex : A.KEY");
        }
    }

    @Override
    public String getElementExpression() {
        return columnName;
    }
}
