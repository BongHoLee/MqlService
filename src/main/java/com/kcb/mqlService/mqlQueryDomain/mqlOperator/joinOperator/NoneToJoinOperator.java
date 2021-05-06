package com.kcb.mqlService.mqlQueryDomain.mqlOperator.joinOperator;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class NoneToJoinOperator implements MQLJoinOperator {

    @Override
    public MQLTable operateWith(MQLDataSource mqlDataSource) {
        Set<String> dataSourceIds = mqlDataSource.getDataSourcesId();
        if (dataSourceIds.size() > 1) {
            throw new RuntimeException("Can't include more than one data source");
        } else if(dataSourceIds.size() == 1){
            Iterator<String> itr = dataSourceIds.iterator();
            String dataSourceId = itr.next();
            return new MQLTable(dataSourceIds, mqlDataSource.dataSourceOf(dataSourceId));
        } else {
            return new MQLTable(new HashSet<>(), new ArrayList<>());
        }
    }
}
