package com.kcb.mqlService.mqlQueryDomain.mqlOperator.relationalOperator.joinOperator;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ColumnOperand;

import java.util.List;
import java.util.Map;

public class LargerThanJoin extends JoinOperator{
    public LargerThanJoin(ColumnOperand leftOperand, ColumnOperand rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected List<Map<String, Object>> join(List<Map<String, Object>> leftDataSource, List<Map<String, Object>> rightDataSource, ColumnOperand leftOperand, ColumnOperand rightOperand) {
        return null;
    }

}
