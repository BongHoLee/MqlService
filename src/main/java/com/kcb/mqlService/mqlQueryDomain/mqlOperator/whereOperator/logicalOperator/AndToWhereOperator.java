package com.kcb.mqlService.mqlQueryDomain.mqlOperator.whereOperator.logicalOperator;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.whereOperator.MQLWhereOperator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AndToWhereOperator implements MQLWhereOperator {
    private MQLWhereOperator leftOperator;
    private MQLWhereOperator rightOperator;

    public AndToWhereOperator(MQLWhereOperator leftOperator, MQLWhereOperator rightOperator) {
        this.leftOperator = leftOperator;
        this.rightOperator = rightOperator;
    }

    @Override
    public MQLTable operateWith(MQLTable table) {
        MQLTable leftTable = leftOperator.operateWith(table);
        MQLTable rightTable = rightOperator.operateWith(table);


        return new MQLTable(table.getJoinSet(), compare(leftTable.getTableData(), rightTable.getTableData()));
    }

    private List<Map<String, Object>> compare(List<Map<String, Object>> leftTableData, List<Map<String, Object>> rightTableData) {
        List<Map<String, Object>> result = leftTableData.stream().filter(rightTableData::contains).collect(Collectors.toList());
        return result;
    }
}
