package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.optionalClause;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.MQLOperatingExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.MQLElement;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.OptionalClause;

import java.util.List;

public class OrderByClause implements OptionalClause {
    private List<MQLElement> orderByElements;
    private boolean asc;

    public OrderByClause(){}

    public OrderByClause(List<MQLElement> orderByElements) {
        this.orderByElements = orderByElements;
    }

    public void setOrderByElements(List<MQLElement> orderByElements) {
        this.orderByElements = orderByElements;
    }

    public void setAsc(boolean asc) {
        this.asc = asc;
    }

    @Override
    public MQLDataStorage executeClause(MQLDataStorage mqlDataStorage) {
        // 1. element is COLUMN

        // 2. element is SINGLE_ROW_FUNCTION

        // 3. element is VALUE
        return expression.operatingWith(mqlDataStorage);
    }


}
