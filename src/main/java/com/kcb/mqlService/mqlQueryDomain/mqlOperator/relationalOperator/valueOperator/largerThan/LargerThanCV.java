package com.kcb.mqlService.mqlQueryDomain.mqlOperator.relationalOperator.valueOperator.largerThan;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ColumnOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ValueOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.relationalOperator.valueOperator.ColumnValueOperator;

import java.util.List;
import java.util.Map;

public class LargerThanCV extends ColumnValueOperator {

    public LargerThanCV(ColumnOperand leftOperand, ValueOperand rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected List<Map<String, Object>> operating(List<Map<String, Object>> leftDataSource, ColumnOperand leftOperand, ValueOperand rightOperand) {
        return null;
    }

}
