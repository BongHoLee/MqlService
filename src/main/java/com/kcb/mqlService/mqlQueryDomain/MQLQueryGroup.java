package com.kcb.mqlService.mqlQueryDomain;



import java.util.*;
import java.util.stream.Collectors;

public class MQLQueryGroup  {
    private Map<String, MQLQueryContext> queryGroup = new HashMap<>();

    public void put(String queryId, MQLQueryContext mqlQueryContext) {
        queryGroup.put(queryId, mqlQueryContext);
    }

    public MQLQueryContext get(String queryId) {
        return queryGroup.get(queryId);
    }

    public List<String> getQueryIds() {
        return new ArrayList<>(queryGroup.keySet());
    }


    public List<Map<String, Object>> executeQuery(String queryId, Map<String, List<Map<String, Object>>> rawDataSources) {
        if (queryGroup.containsKey(queryId) || queryGroup.get(queryId) != null) {
            return queryGroup.get(queryId).executeQuery(rawDataSources);
        } else {
            throw new RuntimeException("Query ID is not exist");
        }

    }


}
