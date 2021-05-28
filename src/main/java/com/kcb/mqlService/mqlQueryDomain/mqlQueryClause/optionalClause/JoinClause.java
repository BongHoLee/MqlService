package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.optionalClause;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.MQLOperatingExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.OptionalClause;

public class JoinClause implements GeneralCondition {

    private MQLOperatingExpression operatingExpression;

    public JoinClause(MQLOperatingExpression operatingExpression) {
        this.operatingExpression = operatingExpression;
    }

    @Override
    public MQLDataStorage executeClause(MQLDataStorage mqlDataStorage) {
        return operatingExpression.operatingWith(mqlDataStorage);
    }

    @Override
    public MQLOperatingExpression getOperatingExpression() {
        return operatingExpression;
    }
}
