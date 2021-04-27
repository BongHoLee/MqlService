package com.kcb.mqlService.mqlQueryDomain;

import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.FromClause;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.SelectClause;

import java.util.List;
import java.util.Map;

public class MQLQueryContext {

    private String queryId;
    private SelectClause selectClause;
    private FromClause fromClause = new FromClause();

    public MQLQueryContext(String queryId, SelectClause selectClause) {
        this.queryId = queryId;
        this.selectClause = selectClause;
    }

    public List<Map<String, Object>> executeQuery(Map<String, List<Map<String, Object>>> rawDataSources) {
        //List<Map<String, Object>> dataDictionary = fromClause.makeMqlDataSources(rawDataSources);


        return  null;
    }

}
