package com.kcb.mqlService.mqlQueryDomain.mqlOperation.relationalOpration;

import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ColumnOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.MQLOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ValueOperand;
import groovyjarjarantlr.collections.impl.LList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EqualToOperation extends RelationalOperation {

    public EqualToOperation(MQLOperand leftOperand, MQLOperand rightOperand) {
        super(leftOperand, rightOperand);
    }


    // join
    @Override
    protected List<Map<String, Object>> operate(ColumnOperand leftOperand, ColumnOperand rightOperand, Map<String, List<Map<String, Object>>> mqlDataSource) {
        List<Map<String, Object>> operationResult = new ArrayList<>();

        // Stream으로 해보자.
        List<Map<String, Object>> leftDataSource = mqlDataSource.get(leftOperand.getDataSourceId());
        List<Map<String, Object>> rightDataSource = mqlDataSource.get(rightOperand.getDataSourceId());

        return null;
    }

    @Override
    protected List<Map<String, Object>> operate(ColumnOperand leftOperand, ValueOperand rightOperand, Map<String, List<Map<String, Object>>> mqlDataSource) {
        List<Map<String, Object>> operationResult = new ArrayList<>();
        return null;
    }

}
