package com.kcb.mqlService.mqlQueryDomain.mqlOperator.relationalOperator.valueOperator.largerThan;

import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ColumnOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ValueOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.relationalOperator.valueOperator.ValueColumnOperator;
import com.kcb.mqlService.utils.MQLOperandFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LargerThanVC extends ValueColumnOperator {
    public LargerThanVC(ValueOperand leftOperand, ColumnOperand rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected List<Map<String, Object>> operating(
            List<Map<String, Object>> rightDataSource,
            ValueOperand leftOperand,
            ColumnOperand rightOperand) {

        MQLOperandFactory factory = MQLOperandFactory.getInstance();
        List<Map<String, Object>> result = rightDataSource.stream()
                .filter(
                        eachRow -> {
                            ValueOperand compareTarget = factory.create(eachRow.get(rightOperand.getExpressionToString()));
                            return leftOperand.largerThan(compareTarget);
                        }
                )
                .collect(Collectors.toList());


        return result;
    }


}
