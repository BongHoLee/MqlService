package com.kcb.mqlService.mqlQueryDomain.mqlOperator.whereOperator.eqaulTo;

import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ColumnOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ValueOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.whereOperator.ColumnValueOperator;
import com.kcb.mqlService.utils.MQLOperandFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EqualToCV extends ColumnValueOperator {

    public EqualToCV(ColumnOperand leftOperand, ValueOperand rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected boolean operating(ValueOperand standard, ValueOperand compareTarget) {
        return standard.equalTo(compareTarget);
    }
}
