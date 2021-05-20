package com.kcb.mqlService.mqlQueryDomain.mqlExpression.logicalOperator;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.MQLOperatingExpression;

import java.util.*;
import java.util.stream.Collectors;

public  class ANDOperator implements MQLOperatingExpression {

    private MQLOperatingExpression leftExpression;
    private MQLOperatingExpression rightExpression;

    public ANDOperator(MQLOperatingExpression leftExpression, MQLOperatingExpression rightExpression) {
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
    }

    @Override
    public MQLDataStorage operatingWith(MQLDataStorage mqlDataStorage) {

        MQLDataStorage leftDataStorage = leftExpression.operatingWith(mqlDataStorage);
        MQLDataStorage rightDataStorage = rightExpression.operatingWith(mqlDataStorage);
        MQLDataSource mqlDataSource = leftDataStorage.getMqlDataSource();

        MQLTable leftTable = leftDataStorage.getMqlTable();
        MQLTable rightTable = rightDataStorage.getMqlTable();

        List<Map<String, Object>> mergedTableData = new ArrayList<>();
        Set<String> matched = leftTable.matchedColumnSet(rightTable);

        if (leftTable.getJoinSet().equals(rightTable.getJoinSet())) {
            mergedTableData = operatingByAllEqualityColumnSet(leftTable.getTableData(), rightTable.getTableData());
        } else {
            if (matched.size() > 0) {
                mergedTableData = operatingBySomeEqualityColumnSet(leftTable.getTableData(), rightTable.getTableData(), matched);
            } else {
                mergedTableData = operatingByNoneEqualityColumnSet(leftTable.getTableData(), rightTable.getTableData());
            }
        }

        Set<String> mergedJoinSet = new HashSet<>();
        mergedJoinSet.addAll(leftTable.getJoinSet());
        mergedJoinSet.addAll(rightTable.getJoinSet());

        return  new MQLDataStorage(mqlDataSource, new MQLTable(mergedJoinSet, mergedTableData));
    }


    private List<Map<String, Object>>
    operatingByAllEqualityColumnSet(
            List<Map<String, Object>> leftTableData,
            List<Map<String, Object>> rightTableData) {

        List<Map<String, Object>> result = leftTableData.stream().filter(rightTableData::contains).collect(Collectors.toList());
        return result;

    }

    private List<Map<String, Object>>
    operatingBySomeEqualityColumnSet(
            List<Map<String, Object>> leftTableData,
            List<Map<String, Object>> rightTableData,
            Set<String> matchedColumns) {

        List<Map<String, Object>> mergedTableData = new ArrayList<>();

        leftTableData.forEach(leftRow -> {
            rightTableData.forEach(rightRow -> {
                if (isValueMatched(leftRow, rightRow, matchedColumns)) {
                    Map<String, Object> mergedRow = new HashMap<>();
                    mergedRow.putAll(leftRow);
                    mergedRow.putAll(rightRow);
                    mergedTableData.add(mergedRow);
                }
            });
        });

        return mergedTableData;
    }

   private List<Map<String, Object>>
    operatingByNoneEqualityColumnSet(
            List<Map<String, Object>> leftTableData,
            List<Map<String, Object>> rightTableData) {
       List<Map<String, Object>> mergedTableData = new ArrayList<>();

       leftTableData.forEach(leftRow ->  {
           rightTableData.forEach(rightRow -> {
               Map<String, Object> mergedRow = new HashMap<>();
               mergedRow.putAll(leftRow);
               mergedRow.putAll(rightRow);
               mergedTableData.add(mergedRow);
           });
       });

       return mergedTableData;
   }

    private boolean isValueMatched(Map<String, Object> leftRow, Map<String, Object> rightRow, Set<String> matchedColumnSet) {
        for (String eachColumn : matchedColumnSet) {
            if (!(leftRow.get(eachColumn).equals(rightRow.get(eachColumn))))
                return false;
        }
        return true;
    }
}
