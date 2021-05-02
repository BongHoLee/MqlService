package com.kcb.mqlService.mqlQueryDomain.mqlOperator.relationalOperator.joinOperator;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ColumnOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.MQLOperator;

import java.util.List;
import java.util.Map;

public abstract class JoinOperator implements MQLOperator {
    private ColumnOperand leftOperand;
    private ColumnOperand rightOperand;

    public JoinOperator(ColumnOperand leftOperand, ColumnOperand rightOperand) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    @Override
    public MQLTable operateWith(MQLDataSource mqlDataSource) {
        MQLTable table = new MQLTable();

        String leftDataSourceId = leftOperand.getDataSourceId();
        String rightDataSourceId = rightOperand.getDataSourceId();

        List<Map<String, Object>> leftDataSource = mqlDataSource.dataSourceOf(leftDataSourceId);
        List<Map<String, Object>> rightDataSource = mqlDataSource.dataSourceOf(rightDataSourceId);

        table.addJoinList(leftDataSourceId);
        table.addJoinList(rightDataSourceId);

        table.setTableData(join(
                leftDataSource,
                rightDataSource,
                leftOperand,
                rightOperand
        ));

        return table;
    }


    protected abstract List<Map<String, Object>> join(
            List<Map<String, Object>> leftDataSource,
            List<Map<String, Object>> rightDataSource,
            ColumnOperand leftOperand,
            ColumnOperand rightOperand
    );
}
