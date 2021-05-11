package com.kcb.mqlService.mqlQueryDomain.mqlOperator.whereOperator.eqaulTo;

import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ColumnOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ValueOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.whereOperator.ValueColumnOperator;
import com.kcb.mqlService.utils.MQLOperandFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EqualToVC extends ValueColumnOperator {

    public EqualToVC(ValueOperand leftOperand, ColumnOperand rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected boolean operating(ValueOperand standard, ValueOperand compareTarget) {
        return standard.equalTo(compareTarget);
    }
}
