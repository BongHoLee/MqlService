package com.kcb.mqlService.mqlQueryDomain;

import java.util.List;
import java.util.Map;

public class MQLQueryContext {

    private String queryId;

    public MQLQueryContext(String queryId) {
        this.queryId = queryId;
    }

    public List<Map<String, Object>> executeQuery(Map<String, List<Map<String, Object>>> rawDataSources) {
        return  null;
    }

}
