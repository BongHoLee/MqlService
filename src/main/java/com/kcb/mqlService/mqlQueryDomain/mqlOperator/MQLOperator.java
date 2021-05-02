package com.kcb.mqlService.mqlQueryDomain.mqlOperator;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;

public interface MQLOperator {
    MQLTable operateWith(MQLDataSource mqlDataSource);
}
