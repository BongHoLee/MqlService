package com.kcb.mqlService.mqlQueryDomain.mqlOperator.joinOperator;

import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ColumnOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ValueOperand;
import com.kcb.mqlService.utils.MQLOperandFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LargerThanJoin extends JoinOperator {
    public LargerThanJoin(ColumnOperand leftOperand, ColumnOperand rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected List<Map<String, Object>> join(
            List<Map<String, Object>> leftDataSource,
            List<Map<String, Object>> rightDataSource,
            ColumnOperand leftOperand,
            ColumnOperand rightOperand) {

        List<Map<String, Object>> joinedTable = new ArrayList<>();
        MQLOperandFactory factory = MQLOperandFactory.getInstance();
        leftDataSource.forEach(leftRow -> rightDataSource.forEach(rightRow -> {
            ValueOperand leftValue = factory.create(leftRow.get(leftOperand.getExpressionToString()));
            ValueOperand rightValue = factory.create(rightRow.get(rightOperand.getExpressionToString()));

            if (leftValue.largerThan(rightValue)){
                Map<String, Object> mergedRow = new HashMap<>();
                mergedRow.putAll(rightRow);
                mergedRow.putAll(leftRow);
                joinedTable.add(mergedRow);
            }
        }));

        return joinedTable;
    }

}
