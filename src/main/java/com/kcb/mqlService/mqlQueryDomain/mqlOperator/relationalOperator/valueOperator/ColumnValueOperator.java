package com.kcb.mqlService.mqlQueryDomain.mqlOperator.relationalOperator.valueOperator;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ColumnOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ValueOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.MQLOperator;

import java.util.List;
import java.util.Map;

public abstract class ColumnValueOperator implements MQLOperator {
    private ColumnOperand leftOperand;
    private ValueOperand rightOperand;

    public ColumnValueOperator(ColumnOperand leftOperand, ValueOperand rightOperand) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    @Override
    public MQLTable operateWith(MQLDataSource mqlDataSource) {
        MQLTable table = new MQLTable();

        String leftDataSourceId = leftOperand.getDataSourceId();
        List<Map<String, Object>> leftDataSource = mqlDataSource.dataSourceOf(leftDataSourceId);

        table.addJoinList(leftDataSourceId);
        table.setTableData(operating(leftDataSource, leftOperand, rightOperand));

        return table;
    }

    protected abstract List<Map<String, Object>> operating(
            List<Map<String, Object>> leftDataSource,
            ColumnOperand leftOperand,
            ValueOperand rightOperand
            );




}
