package com.kcb.mqlService.mqlQueryDomain.mqlExpression.logicalOperator;


import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.MQLOperatingExpression;

import java.util.*;

public class OROperator  implements MQLOperatingExpression{

    private MQLOperatingExpression leftExpression;
    private MQLOperatingExpression rightExpression;

    public OROperator(MQLOperatingExpression leftExpression, MQLOperatingExpression rightExpression) {
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

        List<Map<String, Object>> mergedTable = mergeRow(leftTable, rightTable, mqlDataSource);
        mergedTable.addAll(mergeRow(rightTable, leftTable, mqlDataSource));

        Set<String> mergedJoinSet = new HashSet<>(leftTable.getJoinSet());
        mergedJoinSet.addAll(rightTable.getJoinSet());


        return new MQLDataStorage(mqlDataSource, new MQLTable(mergedJoinSet, new ArrayList<>(new HashSet<>(mergedTable))));
    }


    private List<Map<String, Object>> mergeRow(
            MQLTable basedTable,
            MQLTable targetTable,
            MQLDataSource mqlDataSource) {

        List<Map<String, Object>> baseMergedTable = new ArrayList<>(basedTable.getTableData());
        List<String> remaining = new ArrayList<>(new HashSet<>(targetTable.getJoinSet()));
        remaining.removeAll(basedTable.getJoinSet());

        return recursiveMerge(baseMergedTable, mqlDataSource, remaining, 0);
    }

    private List<Map<String, Object>> recursiveMerge(
            List<Map<String, Object>> lastMerged,
            MQLDataSource mqlDataSource,
            List<String> remaining,
            int curIdx) {

        if (remaining.size() > curIdx) {

            List<Map<String, Object>> currentMerged = new ArrayList<>();
            List<Map<String, Object>> eachTable = mqlDataSource.dataSourceOf(remaining.get(curIdx));

            lastMerged.forEach(lastRow -> {
                eachTable.forEach(eachRow -> {
                    Map<String, Object> mergedRow = new HashMap<>(lastRow);
                    mergedRow.putAll(eachRow);
                    currentMerged.add(mergedRow);
                });
            });

            return recursiveMerge(currentMerged, mqlDataSource, remaining, curIdx+1);
        } else {

            return lastMerged;
        }
    }
}
