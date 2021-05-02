package com.kcb.mqlService.mqlQueryDomain.mqlOperation.relationalOpration;

import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ColumnOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.MQLOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ValueOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperation.MQLOperation;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public abstract class RelationalOperation implements MQLOperation {

    private MQLOperand leftOperand;
    private MQLOperand rightOperand;

    public RelationalOperation(MQLOperand leftOperand, MQLOperand rightOperand) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }


    @Override
    public List<Map<String, Object>> operateWith(Map<String, List<Map<String, Object>>> mqlDataSource) {
        if (leftOperand instanceof ColumnOperand && rightOperand instanceof ColumnOperand) {
            return operate((ColumnOperand) leftOperand, (ColumnOperand) rightOperand, mqlDataSource);
        }
        else if (leftOperand instanceof ColumnOperand && rightOperand instanceof ValueOperand) {
            return operate((ColumnOperand) leftOperand, (ValueOperand) rightOperand, mqlDataSource);
        }
        else if (leftOperand instanceof ValueOperand && rightOperand instanceof ColumnOperand) {
            return operate( (ValueOperand) leftOperand, (ColumnOperand) rightOperand, mqlDataSource);
        }
        else {
            throw new RuntimeException("NOT VALID OPERAND TYPE");
        }
    }

    // A.key = B.key
    protected abstract List<Map<String, Object>> operate(ColumnOperand leftOperand, ColumnOperand rightOperand, Map<String, List<Map<String, Object>>> mqlDataSource);

    // A.column = 'city'
    protected abstract List<Map<String, Object>> operate(ColumnOperand leftOperand, ValueOperand rightOperand, Map<String, List<Map<String, Object>>> mqlDataSource);

    // 'city' = A.column
    protected abstract List<Map<String, Object>> operate(ValueOperand rightOperand, ColumnOperand leftOperand, Map<String, List<Map<String, Object>>> mqlDataSource);

}

