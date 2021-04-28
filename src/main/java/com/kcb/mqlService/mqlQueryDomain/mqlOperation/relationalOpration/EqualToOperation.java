package com.kcb.mqlService.mqlQueryDomain.mqlOperation.relationalOpration;

import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ColumnOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.MQLOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ValueOperand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EqualToOperation extends RelationalOperation {

    public EqualToOperation(MQLOperand leftOperand, MQLOperand rightOperand) {
        super(leftOperand, rightOperand);
    }


    // join
    @Override
    protected List<Map<String, Object>> operate(ColumnOperand leftOperand, ColumnOperand rightOperand, Map<String, List<Map<String, Object>>> mqlDataSource) {
        List<Map<String, Object>> result = new ArrayList<>();

        List<Map<String, Object>> leftDataSource = mqlDataSource.get(leftOperand.getDataSourceId());
        List<Map<String, Object>> rightDataSource = mqlDataSource.get(rightOperand.getDataSourceId());

        leftDataSource.forEach(leftRow -> rightDataSource.forEach(rightRow -> {
            if ((leftRow.get(leftOperand.getExpressionToString())).equals(rightRow.get(rightOperand.getExpressionToString()))){
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
                        eachRow -> (rightOperand.equalTo(eachRow.get(leftOperand.getExpressionToString())))
                )
                .collect(Collectors.toList());

        return result;
    }

}
