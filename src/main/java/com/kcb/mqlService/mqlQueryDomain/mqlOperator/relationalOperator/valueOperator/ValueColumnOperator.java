package com.kcb.mqlService.mqlQueryDomain.mqlOperator.relationalOperator.valueOperator;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ColumnOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ValueOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.MQLOperator;

import java.util.List;
import java.util.Map;

public abstract class ValueColumnOperator implements MQLOperator {

    private ValueOperand leftOperand;
    private ColumnOperand rightOperand;


    public ValueColumnOperator(ValueOperand leftOperand, ColumnOperand rightOperand) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    @Override
    public MQLTable operateWith(MQLDataSource mqlDataSource) {
        MQLTable table = new MQLTable();

        String rightDataSourceId = rightOperand.getDataSourceId();
        List<Map<String, Object>> rightDataSource = mqlDataSource.dataSourceOf(rightDataSourceId);

        table.addJoinList(rightDataSourceId);
        table.setTableData(operating(rightDataSource, leftOperand, rightOperand));

        return table;

    }

    protected abstract List<Map<String, Object>> operating(
            List<Map<String, Object>> rightDataSource,
            ValueOperand leftOperand,
            ColumnOperand rightOperand

    );
}
