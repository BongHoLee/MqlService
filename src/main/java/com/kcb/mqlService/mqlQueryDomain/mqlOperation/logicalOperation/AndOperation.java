package com.kcb.mqlService.mqlQueryDomain.mqlOperation.logicalOperation;

import com.kcb.mqlService.mqlQueryDomain.mqlOperation.MQLOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AndOperation implements MQLOperation {

    private MQLOperation leftOperation;
    private MQLOperation rightOperation;

    public AndOperation(MQLOperation leftOperation, MQLOperation rightOperation) {
        this.leftOperation = leftOperation;
        this.rightOperation = rightOperation;
    }


    @Override
    public List<Map<String, Object>> operateWith(Map<String, List<Map<String, Object>>> mqlDataSource) {
        List<Map<String, Object>> mergedTable = new ArrayList<>();

        List<Map<String, Object>> leftTable = leftOperation.operateWith(mqlDataSource);
        List<Map<String, Object>> rightTable = rightOperation.operateWith(mqlDataSource);

        leftTable.forEach(leftRow -> {
            rightTable.forEach(rightRow -> {
                Map<String, Object> mergedRow = new HashMap<>();
                mergedRow.putAll(leftRow);
                mergedRow.putAll(rightRow);
                mergedTable.add(mergedRow);
            });
        });

        return mergedTable;
    }
}
