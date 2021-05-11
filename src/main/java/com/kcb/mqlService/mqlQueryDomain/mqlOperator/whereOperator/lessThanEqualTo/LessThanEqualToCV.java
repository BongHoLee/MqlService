package com.kcb.mqlService.mqlQueryDomain.mqlOperator.whereOperator.lessThanEqualTo;

import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ColumnOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ValueOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.whereOperator.ColumnValueOperator;
import com.kcb.mqlService.utils.MQLOperandFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LessThanEqualToCV extends ColumnValueOperator {
    public LessThanEqualToCV(ColumnOperand leftOperand, ValueOperand rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected boolean operating(ValueOperand standard, ValueOperand compareTarget) {
        return standard.lessThanOrEqualTo(compareTarget);
    }

}
