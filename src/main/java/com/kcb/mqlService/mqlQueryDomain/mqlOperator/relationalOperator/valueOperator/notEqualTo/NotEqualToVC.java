package com.kcb.mqlService.mqlQueryDomain.mqlOperator.relationalOperator.valueOperator.notEqualTo;

import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ColumnOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ValueOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.relationalOperator.valueOperator.ValueColumnOperator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NotEqualToVC extends ValueColumnOperator {
    public NotEqualToVC(ValueOperand leftOperand, ColumnOperand rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected List<Map<String, Object>> operating(
            List<Map<String, Object>> rightDataSource,
            ValueOperand leftOperand,
            ColumnOperand rightOperand) {

        List<Map<String, Object>> result = rightDataSource.stream()
                .filter(
                        eachRow ->
                                (leftOperand.notEqualTo(eachRow.get(rightOperand.getExpressionToString())))

                )
                .collect(Collectors.toList());

        return result;

    }

}
