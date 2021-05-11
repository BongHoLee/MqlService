package com.kcb.mqlService.mqlQueryDomain.mqlOperator.whereOperator.notEqualTo;

import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ColumnOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ValueOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.whereOperator.ColumnValueOperator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NotEqualToCV extends ColumnValueOperator {
    public NotEqualToCV(ColumnOperand leftOperand, ValueOperand rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected boolean operating(ValueOperand standard, ValueOperand compareTarget) {
        return standard.notEqualTo(compareTarget);
    }

}
