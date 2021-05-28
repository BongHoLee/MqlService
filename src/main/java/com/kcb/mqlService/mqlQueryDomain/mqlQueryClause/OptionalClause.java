package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;

public interface OptionalClause {
    MQLDataStorage executeClause(MQLDataStorage mqlDataStorage);
}
