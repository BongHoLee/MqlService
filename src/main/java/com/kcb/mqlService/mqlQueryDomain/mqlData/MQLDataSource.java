package com.kcb.mqlService.mqlQueryDomain.mqlData;

import java.util.*;
import java.util.stream.Collectors;

public class MQLDataSource {

    private Map<String, List<Map<String, Object>>> mqlDataSources = new HashMap<>();
    public void makeFromRawDataSources(Map<String, List<Map<String, Object>>> rawDataSources) {
        Set<String> dataSourceIds = rawDataSources.keySet();
        for (String dataSourceId : dataSourceIds) {
            List<Map<String, Object>> rawDataSource = rawDataSources.get(dataSourceId);
            List<Map<String, Object>> mqlDataSource = rawDataSource.stream().map(
                    eachMap -> {
                        Map<String, Object> copied = new HashMap<>();
                        for (String key : eachMap.keySet()) {
                            String convertKey = dataSourceId + "." + key;
                            copied.put(convertKey, eachMap.get(key));
                        }
                        return copied;
                    }
            ).collect(Collectors.toList());

            mqlDataSources.put(dataSourceId, mqlDataSource);
        }
    }

    public List<Map<String, Object>> dataSourceOf(String dataSourceId) {
        return mqlDataSources.get(dataSourceId);
    }

    public Map<String, List<Map<String, Object>>> getMqlDataSources() {
        return mqlDataSources;
    }
}
