package com.kcb.mqlService.mqlQueryDomain.mqlExpression;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;

public interface MQLOperatingExpression {
    MQLDataStorage operatingWith(MQLDataStorage mqlDataStorage);
}
