package com.kcb.mqlService.mqlQueryDomain;



import java.util.*;

public class MQLQueryGroup  {
    private Map<String, MQLQueryContext> queryGroup = new HashMap<>();

    public MQLQueryContext put(String queryId, MQLQueryContext dataSource) {
        return queryGroup.put(queryId, dataSource);
    }

    public MQLQueryContext get(String queryId) {
        return queryGroup.get(queryId);
    }

    public MQLQueryContext remove(String queryId) {
        return queryGroup.remove(queryId);
    }

    public List<Map<String, Object>> executeQuery(String queryId, Map<String, List<Map<String, Object>>> rawDataSources) {
        if (queryGroup.containsKey(queryId) || queryGroup.get(queryId) != null) {
            return queryGroup.get(queryId).executeQuery(rawDataSources);
        } else {
            throw new RuntimeException("Query ID is not exist");
        }

    }


}
