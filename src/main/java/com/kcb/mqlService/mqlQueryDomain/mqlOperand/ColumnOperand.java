package com.kcb.mqlService.mqlQueryDomain.mqlOperand;

public class ColumnOperand implements MQLOperand {

    private String expression;
    private String dataSourceId;

    public ColumnOperand(String expression) {
        this.expression = expression;
    }

    @Override
    public String getExpression() {
        return expression;
    }

    private void splitExpression() {
        String[] splited = expression.split(".");
        if (splited.length > 0) {
            dataSourceId = splited[0];
        } else {
            throw new RuntimeException("Column MUST HAVE DataSource ID ex : A.KEY");
        }
    }


    public String getDataSourceId() {
        return dataSourceId;
    }

}
