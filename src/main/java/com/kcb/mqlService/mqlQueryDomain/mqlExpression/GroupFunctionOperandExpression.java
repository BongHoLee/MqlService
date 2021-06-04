package com.kcb.mqlService.mqlQueryDomain.mqlExpression;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.GroupFunctionElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.operatingVisitor.WithTargetOperating;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.relationalOperator.RelationalOperation;

public class GroupFunctionOperandExpression implements MQLOperandExpression{

    private WithTargetOperating targetOperating;
    private GroupFunctionElement groupFunctionElement;
    private RelationalOperation rOperation;

    public GroupFunctionOperandExpression(GroupFunctionElement element, RelationalOperation rOperation, WithTargetOperating targetOperating) {
        this.groupFunctionElement = element;
        this.rOperation = rOperation;
        this.targetOperating = targetOperating;
    }

    @Override
    public MQLDataStorage acceptOperator(MQLDataStorage mqlDataStorage) {
        return targetOperating.operate(groupFunctionElement, rOperation, mqlDataStorage);
    }

    @Override
    public MQLDataStorage operatingWith(MQLDataStorage mqlDataStorage) {
        return acceptOperator(mqlDataStorage);
    }
}
