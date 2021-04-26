package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause;



import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FromClause {
    Set<String> dataSourceIds = new HashSet<>();
    public boolean addDataSourceIds(String dataSourceId) {
        return dataSourceIds.add(dataSourceId);
    }

    public List<Map<String, Object>> makeMqlDataSources(Map<String, List<Map<String, Object>>> rawDataSources) {
        List<List<Map<String, Object>>> mqlDataSources = new ArrayList<>();

        for (String dataSourceId : dataSourceIds) {
            List<Map<String, Object>> rawDataSource = rawDataSources.get(dataSourceId);
            List<Map<String, Object>> mqlDataSource = rawDataSource.stream().map(
                    eachMap -> eachMap.entrySet().stream().collect(Collectors.toMap(entry -> dataSourceId + "." + entry.getKey(), Map.Entry::getValue))
            ).collect(Collectors.toList());

            mqlDataSources.add(mqlDataSource);
        }

        return makeDataDictionary(mqlDataSources);
    }

    private List<Map<String,Object>> makeDataDictionary(List<List<Map<String, Object>>> mqlDataSources) {
        List<Map<String, Object>> dataDictionary = new ArrayList<>();
        crossProduct(mqlDataSources.size()-1, 0, new HashMap<>(), mqlDataSources, dataDictionary);

        return dataDictionary;
    }

    private void crossProduct(
            int end,
            int pos,
            Map<String, Object> curMap,
            List<List<Map<String, Object>>> mqlDataSources,
            List<Map<String, Object>> dataDictionary
    ) {
        if (end >= pos) {
            List<Map<String, Object>> eachDataSource = mqlDataSources.get(pos);

            // NOTE: curMap을 계속 copy 함으로써 생기는 공간 문제도 계산해봐야함..
            for (Map<String, Object> eachRowMap : eachDataSource) {
                Map<String, Object> copiedMap = new HashMap<>(curMap);
                copiedMap.putAll(eachRowMap);
                crossProduct(end, pos + 1, copiedMap, mqlDataSources, dataDictionary);
            }
        } else {
            dataDictionary.add(curMap);
        }
    }





}
