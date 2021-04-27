package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause;



import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FromClause {
    Set<String> dataSourceIds = new HashSet<>();
    public boolean addDataSourceIds(String dataSourceId) {
        return dataSourceIds.add(dataSourceId);
    }

    public Map<String, List<Map<String, Object>>> makeMqlDataSources(Map<String, List<Map<String, Object>>> rawDataSources) {
        Map<String, List<Map<String, Object>>> mqlDataSources = new HashMap<>();

        for (String dataSourceId : dataSourceIds) {
            List<Map<String, Object>> rawDataSource = rawDataSources.get(dataSourceId);
            List<Map<String, Object>> mqlDataSource = rawDataSource.stream().map(
                    eachMap -> eachMap.entrySet().stream().collect(Collectors.toMap(entry -> dataSourceId + "." + entry.getKey(), Map.Entry::getValue))
            ).collect(Collectors.toList());

            mqlDataSources.put(dataSourceId, mqlDataSource);
        }

        return mqlDataSources;
    }







}
