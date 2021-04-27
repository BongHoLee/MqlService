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
        List<Map<String, Object>> dataDictionary = new ArrayList<>(27000010);
        crossProduct(mqlDataSources.size()-1, 0, mqlDataSources, dataDictionary, new ArrayList<>());

        return dataDictionary;
    }

    private void crossProduct(
            int end,
            int pos,
            List<List<Map<String, Object>>> mqlDataSources,
            List<Map<String, Object>> dataDictionary,
            List<Map<String, Object>> forwardedMaps
    ) {
        if (end >= pos) {
            List<Map<String, Object>> eachDataSource = mqlDataSources.get(pos);

            for (Map<String, Object> eachRowMap : eachDataSource) {
            //Map<String, Object> copiedMap = new HashMap<>(curMap);
                //copiedMap.putAll(eachRowMap);
                //crossProduct(end, pos + 1, copiedMap, mqlDataSources, dataDictionary);

                forwardedMaps.add(eachRowMap);
                crossProduct(end, pos + 1, mqlDataSources, dataDictionary, forwardedMaps);
                forwardedMaps.remove(forwardedMaps.size()-1);
            }
        } else {
            //dataDictionary.add(curMap);
            Map<String, Object> copiedMap = new HashMap<>();
            for (Map<String, Object> forwardedMap : forwardedMaps)
                copiedMap.putAll(forwardedMap);

            dataDictionary.add(copiedMap);

        }
    }





}
