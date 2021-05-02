package com.kcb.mqlService.mqlQueryDomain.mqlOperator.relationalOperator.valueOperator.largerThan;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ColumnOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ValueOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.relationalOperator.valueOperator.ValueColumnOperator;

import java.util.List;
import java.util.Map;

public class LargerThanVC extends ValueColumnOperator {
    public LargerThanVC(ValueOperand leftOperand, ColumnOperand rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected List<Map<String, Object>> operating(List<Map<String, Object>> rightDataSource, ValueOperand leftOperand, ColumnOperand rightOperand) {
        return null;
    }


}
