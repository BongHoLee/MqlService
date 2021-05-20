package com.kcb.mqlService.mqlQueryDomain.mqlExpression;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.ColumnElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.operatingVisitor.WithTargetOperating;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.relationalOperator.RelationalOperation;

public class ColumnOperandExpression implements MQLOperandExpression{

    private WithTargetOperating targetOperating;
    private ColumnElement columnElement;
    private RelationalOperation rOperation;

    public ColumnOperandExpression(ColumnElement columnElement, RelationalOperation rOperation, WithTargetOperating targetOperating) {
        this.columnElement = columnElement;
        this.targetOperating = targetOperating;
        this.rOperation = rOperation;
    }

    @Override
    public MQLDataStorage acceptOperator(MQLDataStorage mqlDataStorage) {
        return targetOperating.operate(columnElement, rOperation, mqlDataStorage);
    }


    @Override
    public MQLDataStorage operatingWith(MQLDataStorage mqlDataStorage) {
        return acceptOperator(mqlDataStorage);
    }

}
