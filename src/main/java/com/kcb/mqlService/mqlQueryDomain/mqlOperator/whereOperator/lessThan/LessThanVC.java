package com.kcb.mqlService.mqlQueryDomain.mqlOperator.whereOperator.lessThan;

import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ColumnOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ValueOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.whereOperator.ValueColumnOperator;
import com.kcb.mqlService.utils.MQLOperandFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LessThanVC extends ValueColumnOperator {
    public LessThanVC(ValueOperand leftOperand, ColumnOperand rightOperand) {
        super(leftOperand, rightOperand);
    }



    @Override
    protected boolean operating(ValueOperand standard, ValueOperand compareTarget) {
        return standard.lessThan(compareTarget);
    }


}
