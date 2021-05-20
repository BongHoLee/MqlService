package com.kcb.mqlService.mqlQueryDomain.mqlExpression;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.SingleRowFunctionElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.operatingVisitor.WithTargetOperating;

public class SingleRowFunctionOperandExpression implements MQLOperandExpression{

    private SingleRowFunctionElement singleRowFunctionElement;
    private WithTargetOperating visitor;


    public SingleRowFunctionOperandExpression(SingleRowFunctionElement singleRowFunctionElement, WithTargetOperating visitor) {
        this.singleRowFunctionElement = singleRowFunctionElement;
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

    public SingleRowFunctionElement getSingleRowFunctionElement() {
        return singleRowFunctionElement;
    }
}
