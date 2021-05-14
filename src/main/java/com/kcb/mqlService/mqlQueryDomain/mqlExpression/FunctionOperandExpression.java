package com.kcb.mqlService.mqlQueryDomain.mqlExpression;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.operatingVisitor.WithOperatingVisitor;

public class FunctionOperandExpression implements MQLOperandExpression{

    private WithOperatingVisitor visitor;

    public FunctionOperandExpression(WithOperatingVisitor visitor) {
        this.visitor = visitor;
    }


    @Override
    public MQLDataStorage acceptForVisitor(MQLDataStorage mqlDataStorage) {
        return visitor.visit(this, mqlDataStorage);
    }


    @Override
    public MQLDataStorage operatingWith(MQLDataStorage mqlDataStorage) {
        return acceptForVisitor(mqlDataStorage);
    }
}
