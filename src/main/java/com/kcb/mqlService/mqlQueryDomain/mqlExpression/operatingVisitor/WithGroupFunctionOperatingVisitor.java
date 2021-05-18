package com.kcb.mqlService.mqlQueryDomain.mqlExpression.operatingVisitor;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.ColumnOperandExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.GroupFunctionOperandExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.SingleRowFunctionOperandExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.ValueOperandExpression;

public class WithGroupFunctionOperatingVisitor implements WithOperatingVisitor{

    @Override
    public MQLDataStorage visit(ColumnOperandExpression standardExpression, MQLDataStorage mqlDataStorage) {
        return null;
    }

    @Override
    public MQLDataStorage visit(SingleRowFunctionOperandExpression standardExpression, MQLDataStorage mqlDataStorage) {
        return null;
    }

    @Override
    public MQLDataStorage visit(ValueOperandExpression standardExpression, MQLDataStorage mqlDataStorage) {
        return null;
    }

    @Override
    public MQLDataStorage visit(GroupFunctionOperandExpression standardExpression, MQLDataStorage mqlDataStorage) {
        return null;
    }
}
