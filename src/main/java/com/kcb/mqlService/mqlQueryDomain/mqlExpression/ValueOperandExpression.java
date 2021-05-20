package com.kcb.mqlService.mqlQueryDomain.mqlExpression;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.ValueElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.operatingVisitor.WithTargetOperating;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.relationalOperator.RelationalOperation;

public class ValueOperandExpression implements MQLOperandExpression{
    private WithTargetOperating targetOperating;
    private ValueElement valueElement;
    private RelationalOperation rOperation;

    public ValueOperandExpression(ValueElement valueElement, RelationalOperation rOperation, WithTargetOperating targetOperating) {
        this.targetOperating = targetOperating;
        this.valueElement = valueElement;
        this.rOperation = rOperation;
    }
    @Override
    public MQLDataStorage operatingWith(MQLDataStorage mqlDataStorage) {
        return acceptOperator(mqlDataStorage);
    }


    @Override
    public MQLDataStorage acceptOperator(MQLDataStorage mqlDataStorage) {
        return targetOperating.operate(valueElement, rOperation, mqlDataStorage);
    }
}
