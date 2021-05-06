package com.kcb.mqlService.mqlQueryDomain.mqlOperator.whereOperator.notEqualTo;

import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ColumnOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ValueOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.whereOperator.ValueColumnOperator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NotEqualToVC extends ValueColumnOperator {
    public NotEqualToVC(ValueOperand leftOperand, ColumnOperand rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected List<Map<String, Object>> operating(
            List<Map<String, Object>> tableData,
            ValueOperand leftOperand,
            ColumnOperand rightOperand) {

        List<Map<String, Object>> result = tableData.stream()
                .filter(
                        eachRow ->
                                (leftOperand.notEqualTo(eachRow.get(rightOperand.getExpressionToString())))

                )
                .collect(Collectors.toList());

        return result;

    }

}
