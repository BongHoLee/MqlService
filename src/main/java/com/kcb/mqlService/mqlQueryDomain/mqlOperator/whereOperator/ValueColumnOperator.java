package com.kcb.mqlService.mqlQueryDomain.mqlOperator.whereOperator;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ColumnOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperand.ValueOperand;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.joinOperator.MQLJoinOperator;
import com.kcb.mqlService.utils.MQLOperandFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class ValueColumnOperator implements MQLWhereOperator {

    private ValueOperand leftOperand;
    private ColumnOperand rightOperand;


    public ValueColumnOperator(ValueOperand leftOperand, ColumnOperand rightOperand) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    @Override
    public MQLTable operateWith(MQLTable table) {
        List<Map<String, Object>> tableData = table.getTableData();
        return new MQLTable(table.getJoinSet(), operating(tableData, leftOperand, rightOperand));
    }

    private  List<Map<String, Object>> operating(
            List<Map<String, Object>> tableData,
            ValueOperand leftOperand,
            ColumnOperand rightOperand
    ) {

        MQLOperandFactory factory = MQLOperandFactory.getInstance();
        List<Map<String, Object>> result = tableData.stream()
                .filter(
                        eachRow -> {
                            ValueOperand compareTarget = factory.create(eachRow.get(rightOperand.getExpressionToString()));
                            return operating(leftOperand, compareTarget);
                        }
                )
                .collect(Collectors.toList());


        return result;
    }

    protected abstract boolean operating(ValueOperand standard, ValueOperand compareTarget);
}
