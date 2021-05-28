package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause;



import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FromClause {

    private MQLDataSource mqlDataSource = new MQLDataSource();

    public MQLDataStorage makeMqlDataSources(Map<String, List<Map<String, Object>>> rawDataSources) {
        mqlDataSource.makeFromRawDataSources(rawDataSources);
        MQLDataStorage mqlDataStorage = new MQLDataStorage(
                mqlDataSource,
                new MQLTable()
        );

        return mqlDataStorage;
    }







}
