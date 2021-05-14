package com.kcb.mqlService.mqlQueryDomain.mqlExpression.operatingVisitor;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.ColumnOperandExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.FunctionOperandExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.MQLOperandExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.ValueOperandExpression;

public interface WithOperatingVisitor {
    MQLDataStorage visit(ColumnOperandExpression standardExpression, MQLDataStorage mqlDataStorage);
    MQLDataStorage visit(FunctionOperandExpression standardExpression, MQLDataStorage mqlDataStorage);
    MQLDataStorage visit(ValueOperandExpression standardExpression, MQLDataStorage mqlDataStorage);

}
