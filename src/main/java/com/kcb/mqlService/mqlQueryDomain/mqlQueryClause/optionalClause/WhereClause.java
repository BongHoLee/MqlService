package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.optionalClause;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.MQLOperatingExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.OptionalClause;

public class WhereClause implements GeneralCondition {
    private MQLOperatingExpression expression;

    public WhereClause(){}

    public WhereClause(MQLOperatingExpression expression) {
        this.expression = expression;
    }

    public void setOperatingExpression(MQLOperatingExpression expression) {
        this.expression = expression;
    }

    @Override
    public MQLDataStorage executeClause(MQLDataStorage mqlDataStorage) {
        return expression.operatingWith(mqlDataStorage);
    }

    @Override
    public MQLOperatingExpression getOperatingExpression() {
        return expression;
    }
}
