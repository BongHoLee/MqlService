package com.kcb.mqlService.utils;

import com.kcb.mqlService.mqlFactory.exception.MQLQueryExecuteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ExceptionThrowerUtil {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionThrowerUtil.class);

    public static void isValidRow(String queryID, Map<String, Object> row, String key) {
        if (!row.containsKey(key)) {
            logger.error("Query ID: {}, Column(Key) `{}` is not exists in data source", queryID, key);
            throw new MQLQueryExecuteException("Query Execution Exception. Query ID : " + queryID);
        }



    }

    public static void isValidRow(Map<String, Object> row, String key) {
        if (!row.containsKey(key)) {
            logger.error("Column(Key) `{}` is not exists in data source",  key);
            throw new MQLQueryExecuteException("Query Execution Exception. ");
        }


    }


}
