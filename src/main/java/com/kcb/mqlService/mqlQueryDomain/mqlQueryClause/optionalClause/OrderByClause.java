package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.optionalClause;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.MQLOperatingExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.MQLElement;

import java.util.List;

public class OrderByClause implements GeneralCondition {
    private List<MQLElement> orderByElements;

    public OrderByClause(){}

    public OrderByClause(MQLOperatingExpression expression) {
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
