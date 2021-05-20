package com.kcb.mqlService.mqlQueryDomain.mqlExpression;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;

public interface MQLOperandExpression extends MQLOperatingExpression{
    MQLDataStorage acceptOperator(MQLDataStorage mqlDataStorage);
}
