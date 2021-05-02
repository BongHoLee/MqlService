package com.kcb.mqlService.mqlQueryDomain.mqlOperator.logicalOperator;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.MQLOperator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AndOperator implements MQLOperator {
    private MQLOperator leftOperator;
    private MQLOperator rightOperator;

    public AndOperator(MQLOperator leftOperator, MQLOperator rightOperator) {
        this.leftOperator = leftOperator;
        this.rightOperator = rightOperator;
    }

    @Override
    public MQLTable operateWith(MQLDataSource mqlDataSource) {
        MQLTable leftTable = leftOperator.operateWith(mqlDataSource);
        MQLTable rightTable = rightOperator.operateWith(mqlDataSource);

        List<String> matchedDataSourceId = leftTable.matchedDataSourceId(rightTable);
        List<Map<String, Object>> joinedTable = new ArrayList<>();
        if (matchedDataSourceId.size() > 0) {
            List<Map<String, Object>> leftTableData = leftTable.getTableData();
            

        } else {

        }

        return null;
    }
}
