package com.kcb.mqlService.mqlQueryDomain.mqlExpression;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.operatingVisitor.WithOperatingVisitor;

public class GroupFunctionOperandExpression implements MQLOperandExpression{
    private WithOperatingVisitor visitor;



    @Override
    public MQLDataStorage acceptForVisitor(MQLDataStorage mqlDataStorage) {
        return null;
    }

    @Override
    public MQLDataStorage operatingWith(MQLDataStorage mqlDataStorage) {
        return null;
    }
}