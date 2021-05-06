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
    protected List<Map<String, Object>> operating(
            List<Map<String, Object>> tableData,
            ColumnOperand leftOperand,
            ValueOperand rightOperand) {


        MQLOperandFactory factory = MQLOperandFactory.getInstance();
        List<Map<String, Object>> result = tableData.stream()
                .filter(
                        eachRow ->  {
                            ValueOperand compareTarget = factory.create(eachRow.get(leftOperand.getExpressionToString()));
                            return rightOperand.equalTo(compareTarget);
                        }
                )
                .collect(Collectors.toList());


        return result;
    }
}
