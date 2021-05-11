package com.kcb.mqlService.mqlQueryDomain.mqlOperator.whereOperator.notEqualTo;

import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ColumnOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ValueOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.whereOperator.ValueColumnOperator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NotEqualToVC extends ValueColumnOperator {
    public NotEqualToVC(ValueOperand leftOperand, ColumnOperand rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected boolean operating(ValueOperand standard, ValueOperand compareTarget) {
        return standard.notEqualTo(compareTarget);
    }

}
