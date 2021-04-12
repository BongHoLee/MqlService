package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class FromClauseTest {

    @Test
    public void makeMqlDataSourcesTest() {
        FromClause fromClause = new FromClause();
        fromClause.addDataSourceIds("testId1");
        fromClause.addDataSourceIds("testId2");
        fromClause.addDataSourceIds("testId3");


    }

    public boolean isConvertedWithoutLeak(List<Map<String, Object>> rawDataSource, List<Map<String, Object>> mqlDataSource, String mqlDataSourceId) {
        for (int i=0; i<rawDataSource.size(); i++) {
            Map<String, Object> eachRawDataSourceRow = rawDataSource.get(i);
            Map<String, Object> eachMqlDataSourceRow = mqlDataSource.get(i);

            if (eachRawDataSourceRow.size() != eachMqlDataSourceRow.size())
                return false;
            else {
                boolean isValid = true;
                for (Map.Entry<String, Object> entry : eachRawDataSourceRow.entrySet()) {
                    isValid = eachMqlDataSourceRow.containsKey(mqlDataSourceId + "." + entry.getKey());
                }
            }

        }
    }


    public List<Map<String, Object>> makeRawDataSource(String key) {
        List<Map<String, Object>> rawDataSource = new ArrayList<>();

        for (int i=0; i<10; i++) {
            Map<String, Object> eachRow = new HashMap<>();
            for (int j = 0; j < 3; j++) {
                eachRow.put(key + i, UUID.randomUUID().toString());
            }
            rawDataSource.add(eachRow);
        }

        return rawDataSource;
    }
}
