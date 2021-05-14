package com.kcb.mqlService.mqlQueryDomain.mqlExpression;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.ValueElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.operatingVisitor.WithOperatingVisitor;

public class ValueOperandExpression implements MQLOperandExpression{
    private WithOperatingVisitor visitor;
    private ValueElement valueElement;

    public ValueOperandExpression(ValueElement valueElement, WithOperatingVisitor visitor) {
        this.visitor = visitor;
        this.valueElement = valueElement;
    }

    @Override
    public MQLDataStorage acceptForVisitor(MQLDataStorage mqlDataStorage) {
        return visitor.visit(this, mqlDataStorage);
    }

    @Override
    public MQLDataStorage operatingWith(MQLDataStorage mqlDataStorage) {
        return acceptForVisitor(mqlDataStorage);
    }

    public ValueElement getValueElement() {
        return valueElement;
    }
}
