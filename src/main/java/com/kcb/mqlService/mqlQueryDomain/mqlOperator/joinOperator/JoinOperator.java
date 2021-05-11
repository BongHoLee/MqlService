package com.kcb.mqlService.mqlQueryDomain.mqlOperator.joinOperator;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ColumnOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ValueOperand;
import com.kcb.mqlService.utils.MQLOperandFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class JoinOperator implements MQLJoinOperator {
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

        table.setTableData(joinTable(
                leftDataSource,
                rightDataSource,
                leftOperand,
                rightOperand
        ));

        return table;
    }

    private List<Map<String, Object>> joinTable(
            List<Map<String, Object>> leftDataSource,
            List<Map<String, Object>> rightDataSource,
            ColumnOperand leftOperand,
            ColumnOperand rightOperand) {

        List<Map<String, Object>> joinedTable = new ArrayList<>();
        MQLOperandFactory factory = MQLOperandFactory.getInstance();
        leftDataSource.forEach(leftRow -> rightDataSource.forEach(rightRow -> {
            ValueOperand leftValue = factory.create(leftRow.get(leftOperand.getExpressionToString()));
            ValueOperand rightValue = factory.create(rightRow.get(rightOperand.getExpressionToString()));

            if (operating(leftValue, rightValue)){
                Map<String, Object> mergedRow = new HashMap<>();
                mergedRow.putAll(rightRow);
                mergedRow.putAll(leftRow);
                joinedTable.add(mergedRow);
            }
        }));

        return joinedTable;

    }

    protected abstract boolean operating(ValueOperand leftValue, ValueOperand rightValue);
}
