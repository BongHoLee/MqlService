package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause;



import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FromClause {

    private MQLDataSource mqlDataSource = new MQLDataSource();

    public boolean addDataSourceIds(String dataSourceId) {
        return mqlDataSource.addDataSourceId(dataSourceId);
    }

    public MQLDataSource makeMqlDataSources(Map<String, List<Map<String, Object>>> rawDataSources) {
        mqlDataSource.makeFromRawDataSources(rawDataSources);
        return mqlDataSource;
    }







}
