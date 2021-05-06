package com.kcb.mqlService.mqlQueryDomain.mqlOperator.whereOperator;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;

public interface MQLWhereOperator {
    MQLTable operateWith(MQLTable table);
}
