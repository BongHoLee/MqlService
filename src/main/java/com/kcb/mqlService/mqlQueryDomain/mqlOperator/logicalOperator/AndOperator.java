package com.kcb.mqlService.mqlQueryDomain.mqlOperator.logicalOperator;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.MQLOperator;

import java.util.*;

public class AndOperator implements MQLOperator {
    private MQLOperator leftOperator;
    private MQLOperator rightOperator;

    public AndOperator(MQLOperator leftOperator, MQLOperator rightOperator) {
        this.leftOperator = leftOperator;
        this.rightOperator = rightOperator;
    }

    @Override
    public MQLTable operateWith(MQLDataSource mqlDataSource) {
        MQLTable leftTable = leftOperator.operateWith(mqlDataSource);
        MQLTable rightTable = rightOperator.operateWith(mqlDataSource);


        List<Map<String, Object>> mergedTableData = new ArrayList<>();
        List<Map<String, Object>> leftTableData = leftTable.getTableData();
        List<Map<String, Object>> rightTableData = rightTable.getTableData();

        Set<String> matched = leftTable.matchedColumnSet(rightTable);

        if(matched.size() > 0) {
            leftTableData.forEach(leftRow -> {
                rightTableData.forEach(rightRow -> {
                    if (isValueMatched(leftRow, rightRow, matched)) {
                        Map<String, Object> mergedRow = new HashMap<>();
                        mergedRow.putAll(leftRow);
                        mergedRow.putAll(rightRow);
                        mergedTableData.add(mergedRow);
                    }

                });
            });
        } else {
            leftTableData.forEach(leftRow ->  {
                rightTableData.forEach(rightRow -> {
                    Map<String, Object> mergedRow = new HashMap<>();
                    mergedRow.putAll(leftRow);
                    mergedRow.putAll(rightRow);
                    mergedTableData.add(mergedRow);
                });
            });
        }

        Set<String> mergedJoinSet = new HashSet<>();
        mergedJoinSet.addAll(leftTable.getJoinSet());
        mergedJoinSet.addAll(rightTable.getJoinSet());



        return  new MQLTable(mergedJoinSet, mergedTableData);
    }

    private boolean isValueMatched(Map<String, Object> leftRow, Map<String, Object> rightRow, Set<String> matchedColumnSet) {
        for (String eachColumn : matchedColumnSet) {
            if (!(leftRow.get(eachColumn).equals(rightRow.get(eachColumn))))
                return false;
        }
        return true;
    }
}
