package com.kcb.mqlService.mqlQueryDomain.mqlOperator.whereOperator;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ColumnOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ValueOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.joinOperator.MQLJoinOperator;

import java.util.List;
import java.util.Map;

public abstract class ColumnValueOperator implements MQLWhereOperator {
    private ColumnOperand leftOperand;
    private ValueOperand rightOperand;

    public ColumnValueOperator(ColumnOperand leftOperand, ValueOperand rightOperand) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    @Override
    public MQLTable operateWith(MQLTable table) {
        List<Map<String, Object>> tableData = table.getTableData();
        return new MQLTable(table.getJoinSet(), operating(tableData, leftOperand, rightOperand));
    }

    protected abstract List<Map<String, Object>> operating(
            List<Map<String, Object>> tableData,
            ColumnOperand leftOperand,
            ValueOperand rightOperand
            );




}
