package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.optionalClause;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.MQLOperatingExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.OptionalClause;

public class HavingClause implements OptionalClause {
    private MQLOperatingExpression operatingExpression;

    public HavingClause(){}

    public HavingClause(MQLOperatingExpression operatingExpression) {
        this.operatingExpression = operatingExpression;
    }

    public void setOperatingExpression(MQLOperatingExpression operatingExpression) {
        this.operatingExpression = operatingExpression;
    }

    @Override
    public MQLDataStorage executeClause(MQLDataStorage mqlDataStorage) {
        return operatingExpression.operatingWith(mqlDataStorage);
    }
}
