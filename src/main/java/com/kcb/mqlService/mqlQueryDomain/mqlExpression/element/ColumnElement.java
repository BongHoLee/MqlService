package com.kcb.mqlService.mqlQueryDomain.mqlExpression.element;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;

public class ColumnElement implements MQLElement{
    private String columnName;
    private String dataSourceId;
    private String alias;
    private boolean hasAlias;

    public ColumnElement(String alias, String columnName) {
        this.columnName = columnName;
        this.alias = alias;
        splitExpression();
        setHasAlias();
    }

    public ColumnElement(String columnName) {
        this("", columnName);

    }

    public String getColumnName() {
        return columnName;
    }

    public String getDataSourceId() {
        return dataSourceId;
    }

    private void splitExpression() {
        if (!columnName.equals("*")) {
            String[] splited = columnName.split("\\.");
            if (splited.length > 0) {
                dataSourceId = splited[0];
            } else {
                throw new RuntimeException("Column MUST HAVE DataSource ID ex : A.KEY");
            }
        }
    }

    @Override
    public String getElementExpression() {
        return columnName;
    }

    @Override
    public boolean hasAlias() {
        return hasAlias;
    }

    @Override
    public String getAlias() {
        return alias;
    }


    private void setHasAlias() {
        hasAlias = !alias.isEmpty();
    }
}
