package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.optionalClause;

import com.kcb.mqlService.mqlQueryDomain.mqlExpression.MQLOperatingExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.OptionalClause;

public interface GeneralCondition extends OptionalClause {
    MQLOperatingExpression getOperatingExpression();
}
