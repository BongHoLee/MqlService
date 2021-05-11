package com.kcb.mqlService.mqlQueryDomain.mqlOperator.joinOperator;

import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ColumnOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ValueOperand;
import com.kcb.mqlService.utils.MQLOperandFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LargerThanEqualToJoin extends JoinOperator {
    public LargerThanEqualToJoin(ColumnOperand leftOperand, ColumnOperand rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected boolean operating(ValueOperand leftValue, ValueOperand rightValue) {
        return leftValue.largerThanOrEqualTo(rightValue);
    }


}
