package com.kcb.mqlService.mqlQueryDomain;



import com.kcb.mqlService.mqlFactory.exception.MQLQueryExecuteException;
import com.kcb.mqlService.mqlFactory.validator.ItemsOfGeneralClauseValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class MQLQueryGroup  {
    private static final Logger logger = LoggerFactory.getLogger(MQLQueryGroup.class);
    private Map<String, MQLQueryContext> queryGroup = new HashMap<>();

    public void put(String queryId, MQLQueryContext mqlQueryContext) {
        queryGroup.put(queryId, mqlQueryContext);
    }

    public MQLQueryContext get(String queryId) {
        return queryGroup.get(queryId);
    }


    public List<Map<String, Object>> executeQuery(String queryId, Map<String, List<Map<String, Object>>> rawDataSources) {
        if (queryGroup.containsKey(queryId) || queryGroup.get(queryId) != null) {
            logger.info("Query ID {} execute start.", queryId);
            return queryGroup.get(queryId).executeQuery(rawDataSources);
        } else {
            logger.error("Query ID {} is not exists in Query Group", queryId);
            throw new MQLQueryExecuteException("Query ID is not exist");
        }

    }


}
