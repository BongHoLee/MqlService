package com.kcb.mqlService.mqlQueryDomain.mqlOperator.logicalOperator;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.MQLOperator;
import groovyjarjarantlr.collections.impl.LList;

import java.util.*;

public class OrOperator implements MQLOperator {
    private MQLOperator leftOperator;
    private MQLOperator rightOperator;

    public OrOperator(MQLOperator leftOperator, MQLOperator rightOperator) {
        this.leftOperator = leftOperator;
        this.rightOperator = rightOperator;
    }

    @Override
    public MQLTable operateWith(MQLDataSource mqlDataSource) {
        MQLTable leftTable = leftOperator.operateWith(mqlDataSource);
        MQLTable rightTable = rightOperator.operateWith(mqlDataSource);

        List<Map<String, Object>> mergedTable = mergeRow(leftTable, rightTable, mqlDataSource);
        mergedTable.addAll(mergeRow(rightTable, leftTable, mqlDataSource));

        Set<String> mergedJoinSet = new HashSet<>(leftTable.getJoinSet());
        mergedJoinSet.addAll(rightTable.getJoinSet());


        return new MQLTable(mergedJoinSet, new ArrayList<>(new HashSet<>(mergedTable)));
    }

    private List<Map<String, Object>> mergeRow(
            MQLTable basedTable,
            MQLTable targetTable,
            MQLDataSource mqlDataSource) {


        List<Map<String, Object>> basedMergedTable = new ArrayList<>(basedTable.getTableData());
        List<String> remaining = new ArrayList<>(new HashSet<>(targetTable.getJoinSet()));
        remaining.removeAll(basedTable.getJoinSet());


        return recursiveMerge(basedMergedTable, mqlDataSource, remaining, 0);
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
