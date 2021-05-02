package com.kcb.mqlService.mqlQueryDomain.mqlOperation.relationalOpration;

import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ColumnOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.MQLOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ValueOperand;
import com.kcb.mqlService.utils.MQLOperandFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NotEqualToOperation extends RelationalOperation {

    public NotEqualToOperation(MQLOperand leftOperand, MQLOperand rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected List<Map<String, Object>> operate(ColumnOperand leftOperand, ColumnOperand rightOperand, Map<String, List<Map<String, Object>>> mqlDataSource) {
        List<Map<String, Object>> result = new ArrayList<>();

        List<Map<String, Object>> leftDataSource = mqlDataSource.get(leftOperand.getDataSourceId());
        List<Map<String, Object>> rightDataSource = mqlDataSource.get(rightOperand.getDataSourceId());

        MQLOperandFactory factory = MQLOperandFactory.getInstance();
        leftDataSource.forEach(leftRow -> rightDataSource.forEach(rightRow -> {

            ValueOperand operand = factory.create(leftRow.get(leftOperand.getExpressionToString()));

            if (operand.notEqualTo(rightRow.get(rightOperand.getExpressionToString()))){
                Map<String, Object> mergedRow = new HashMap<>();
                mergedRow.putAll(rightRow);
                mergedRow.putAll(leftRow);
                result.add(mergedRow);
            }
        }));

        return result;

    }

    @Override
    protected List<Map<String, Object>> operate(ColumnOperand leftOperand, ValueOperand rightOperand, Map<String, List<Map<String, Object>>> mqlDataSource) {
        List<Map<String, Object>> leftDataSource = mqlDataSource.get(leftOperand.getDataSourceId());

        List<Map<String, Object>> result = leftDataSource.stream()
                .filter(
                        eachRow -> (rightOperand.notEqualTo(eachRow.get(leftOperand.getExpressionToString())))
                )
                .collect(Collectors.toList());

        return result;
    }

    @Override
    protected List<Map<String, Object>> operate(ValueOperand rightOperand, ColumnOperand leftOperand, Map<String, List<Map<String, Object>>> mqlDataSource) {
        return operate(leftOperand, rightOperand, mqlDataSource);
    }
}
