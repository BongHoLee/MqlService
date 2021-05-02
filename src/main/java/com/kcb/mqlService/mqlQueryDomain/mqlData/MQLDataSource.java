package com.kcb.mqlService.mqlQueryDomain.mqlData;

import java.util.*;
import java.util.stream.Collectors;

public class MQLDataSource {
    private Set<String> dataSourceIds = new HashSet<>();
    private Map<String, List<Map<String, Object>>> mqlDataSources = new HashMap<>();

    public boolean addDataSourceId(String dataSourceId) {
        return dataSourceIds.add(dataSourceId);
    }

    public void makeFromRawDataSources(Map<String, List<Map<String, Object>>> rawDataSources) {
        if (rawDataSources.keySet().equals(dataSourceIds)) {

            for (String dataSourceId : dataSourceIds) {
                List<Map<String, Object>> rawDataSource = rawDataSources.get(dataSourceId);
                List<Map<String, Object>> mqlDataSource = rawDataSource.stream().map(
                        eachMap -> eachMap.entrySet().stream().collect(Collectors.toMap(entry -> dataSourceId + "." + entry.getKey(), Map.Entry::getValue))
                ).collect(Collectors.toList());

                mqlDataSources.put(dataSourceId, mqlDataSource);
            }
        } else {
            throw new RuntimeException("Not Valid Raw Data Source, Check data source ids");
        }
    }

    public boolean hasDataSource(String dataSourceId) {
        return dataSourceIds.contains(dataSourceId);
    }

    public boolean hasDataSource(Collection<String> dataSources) {
        return dataSourceIds.containsAll(dataSources);
    }

    public List<Map<String, Object>> dataSourceOf(String dataSourceId) {
        return mqlDataSources.get(dataSourceId);
    }


    public Set<String> getDataSourcesId() {
        return dataSourceIds;
    }

    public Map<String, List<Map<String, Object>>> getMqlDataSources() {
        return mqlDataSources;
    }
}
