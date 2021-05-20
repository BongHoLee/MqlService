package com.kcb.mqlService.mqlQueryDomain.mqlExpression;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.ColumnElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.operatingVisitor.WithTargetOperating;

public class ColumnOperandExpression implements MQLOperandExpression{

    private WithTargetOperating visitor;
    private ColumnElement columnElement;

    public ColumnOperandExpression(ColumnElement columnElement, WithTargetOperating visitor) {
        this.columnElement = columnElement;
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

    public ColumnElement getColumnElement() {
        return columnElement;
    }
}
