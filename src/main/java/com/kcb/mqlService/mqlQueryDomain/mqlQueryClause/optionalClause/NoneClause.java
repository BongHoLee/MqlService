package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.optionalClause;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.OptionalClause;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

public class NoneClause implements OptionalClause {
    @Override
    public MQLDataStorage executeClause(MQLDataStorage mqlDataStorage) {
        MQLDataSource dataSource = mqlDataStorage.getMqlDataSource();

        if (dataSource.getMqlDataSources().keySet().size() > 1) {
            throw new RuntimeException("None Clause Must has only one data table!");
        }
        for(String key : dataSource.getMqlDataSources().keySet()) {
            mqlDataStorage.setMqlTable(new MQLTable(new HashSet<>(Collections.singletonList(key)), dataSource.dataSourceOf(key)));
        }

        return mqlDataStorage;
    }
}
