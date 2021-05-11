package com.kcb.mqlService.mqlQueryDomain.mqlOperator.whereOperator.lessThanEqualTo;

import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ColumnOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ValueOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.whereOperator.ValueColumnOperator;
import com.kcb.mqlService.utils.MQLOperandFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LessThanEqualToVC extends ValueColumnOperator {
    public LessThanEqualToVC(ValueOperand leftOperand, ColumnOperand rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected boolean operating(ValueOperand standard, ValueOperand compareTarget) {
        return standard.lessThanOrEqualTo(compareTarget);
    }

}
