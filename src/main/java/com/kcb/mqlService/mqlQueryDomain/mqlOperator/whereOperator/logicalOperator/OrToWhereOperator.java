package com.kcb.mqlService.mqlQueryDomain.mqlOperator.whereOperator.logicalOperator;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.whereOperator.MQLWhereOperator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class OrToWhereOperator implements MQLWhereOperator {

    private MQLWhereOperator leftOperator;
    private MQLWhereOperator rightOperator;

    public OrToWhereOperator(MQLWhereOperator leftOperator, MQLWhereOperator rightOperator) {
        this.leftOperator = leftOperator;
        this.rightOperator = rightOperator;
    }

    @Override
    public MQLTable operateWith(MQLTable table) {

        MQLTable leftTable = leftOperator.operateWith(table);
        MQLTable rightTable = rightOperator.operateWith(table);

        return new MQLTable(leftTable.getJoinSet(), merge(leftTable.getTableData(), rightTable.getTableData()));
    }

    private List<Map<String, Object>> merge(List<Map<String, Object>> leftTableData, List<Map<String, Object>> rightTableData) {
        List<Map<String, Object>> merged = new ArrayList<>();
        merged.addAll(leftTableData);
        merged.addAll(rightTableData);
        return new ArrayList<>(new HashSet<>(merged));
    }
}
