package com.kcb.mqlService.mqlQueryDomain.mqlExpression;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.SingleRowFunctionElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.operatingVisitor.WithTargetOperating;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.relationalOperator.RelationalOperation;

public class SingleRowFunctionOperandExpression implements MQLOperandExpression{

    private SingleRowFunctionElement singleRowFunctionElement;
    private RelationalOperation rOperation;
    private WithTargetOperating targetOperating;


    public SingleRowFunctionOperandExpression(SingleRowFunctionElement singleRowFunctionElement, RelationalOperation rOperation, WithTargetOperating targetOperating) {
        this.singleRowFunctionElement = singleRowFunctionElement;
        this.rOperation = rOperation;
        this.targetOperating = targetOperating;
    }

    @Override
    public MQLDataStorage operatingWith(MQLDataStorage mqlDataStorage) {
        return acceptOperator(mqlDataStorage);
    }


    @Override
    public MQLDataStorage acceptOperator(MQLDataStorage mqlDataStorage) {
        return targetOperating.operate(singleRowFunctionElement, rOperation, mqlDataStorage);
    }
}
