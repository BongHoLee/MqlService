package com.kcb.mqlService.mqlQueryDomain.mqlExpression.operatingVisitor;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.ColumnOperandExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.GroupFunctionOperandExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.SingleRowFunctionOperandExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.ValueOperandExpression;

public interface WithOperatingVisitor {
    MQLDataStorage visit(ColumnOperandExpression standardExpression, MQLDataStorage mqlDataStorage);
    MQLDataStorage visit(SingleRowFunctionOperandExpression standardExpression, MQLDataStorage mqlDataStorage);
    MQLDataStorage visit(ValueOperandExpression standardExpression, MQLDataStorage mqlDataStorage);
    MQLDataStorage visit(GroupFunctionOperandExpression standardExpression, MQLDataStorage mqlDataStorage);

}
