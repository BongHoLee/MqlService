package com.kcb.mqlService.mqlQueryDomain.mqlOperator.joinOperator;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;

public interface MQLJoinOperator {
    MQLTable operateWith(MQLDataSource mqlDataSource);
}
