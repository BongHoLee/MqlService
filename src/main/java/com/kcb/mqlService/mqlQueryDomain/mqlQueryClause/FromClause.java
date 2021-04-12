package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause;

import com.kcb.mqlService.mqlDataSource.MQLDataSource;

import java.util.*;
import java.util.stream.Collectors;

public class FromClause {
    List<String> dataSourceIds = new ArrayList<>();

    public boolean addDataSourceIds(String dataSourceId) {
        return dataSourceIds.add(dataSourceId);
    }

    // stream으로 어케 변환 안될까?
    public Map<String, List<Map<String, Object>>> makeMqlDataSources(List<Map<String, Object>> ... rawDataSources) {
        Map<String, List<Map<String, Object>>> mqlDataSources = new HashMap<>();

        for (int i=0; i<dataSourceIds.size(); i++) {
            String dataSourceId = dataSourceIds.get(i);
            List<Map<String, Object>> rawDataSource = rawDataSources[i];

            List<Map<String, Object>> mqlDataSource = rawDataSource.stream().map(
                    eachMap -> eachMap.entrySet().stream().collect(Collectors.toMap(entry -> dataSourceId + "." + entry.getKey(), Map.Entry::getValue))
            ).collect(Collectors.toList());

            mqlDataSources.put(dataSourceId, mqlDataSource);
        }

        return mqlDataSources;
    }

}
