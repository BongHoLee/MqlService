package com.kcb.mqlService.mqlQueryDomain.mqlExpression.element;

public interface MQLElement {
    String getElementExpression();
    boolean hasAlias();
    String getAlias();
}
