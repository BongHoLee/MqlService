package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause;



import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FromClause {

    private MQLDataSource mqlDataSource = new MQLDataSource();

    public void addDataSourceIds(String ... dataSourceIds) {
        for (String eachId : dataSourceIds)
            mqlDataSource.addDataSourceId(eachId);
    }

    public MQLDataSource makeMqlDataSources(Map<String, List<Map<String, Object>>> rawDataSources) {
        mqlDataSource.makeFromRawDataSources(rawDataSources);
        return mqlDataSource;
    }







}
