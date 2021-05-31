package com.kcb.mqlService.mqlQueryDomain.mqlExpression.element;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;

public abstract class GroupFunctionElement implements MQLElement{
    private MQLElement parameter;

    public GroupFunctionElement(MQLElement parameter) {
        this.parameter = parameter;
    }

    public MQLDataStorage executeWith(MQLDataStorage mqlDataStorage) {

    }



}
