package com.kcb.mqlService.mqlQueryDomain.mqlExpression.operatingVisitor;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.ColumnOperandExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.FunctionOperandExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.ValueOperandExpression;

public class WithFunctionOperatingVisitor implements WithOperatingVisitor {


    @Override
    public MQLDataStorage visit(ColumnOperandExpression standardExpression, MQLDataStorage mqlDataStorage) {
        return null;
    }

    @Override
    public MQLDataStorage visit(FunctionOperandExpression standardExpression, MQLDataStorage mqlDataStorage) {
        return null;
    }

    @Override
    public MQLDataStorage visit(ValueOperandExpression standardExpression, MQLDataStorage mqlDataStorage) {
        return null;
    }
}
