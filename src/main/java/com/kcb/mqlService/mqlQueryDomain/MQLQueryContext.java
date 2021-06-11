package com.kcb.mqlService.mqlQueryDomain;

import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.FromClause;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.SelectClause;

import java.util.List;
import java.util.Map;

public class MQLQueryContext {
    private SelectClause selectClause;

    public MQLQueryContext(SelectClause selectClause) {
        this.selectClause = selectClause;
    }
    public List<Map<String, Object>> executeQuery(Map<String, List<Map<String, Object>>> rawDataSources) {
        return selectClause.executeQueryWith(rawDataSources);
    }
}
