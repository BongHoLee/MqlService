package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause;

import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class FromClauseTest {

    @Test
    public void makeMqlDataSourcesTest() {


        FromClause fromClause = new FromClause();
        fromClause.addDataSourceIds("dataSourceId1");
        fromClause.addDataSourceIds("dataSourceId2");
        fromClause.addDataSourceIds("dataSourceId3");

        Map<String, List<Map<String, Object>>> rawDataSources = new HashMap<>();
        rawDataSources.put("dataSourceId1", makeRawDataSource("first"));
        rawDataSources.put("dataSourceId2", makeRawDataSource("second"));
        rawDataSources.put("dataSourceId3", makeRawDataSource("third"));

        int crossProductSize = 1;
        for (String key : rawDataSources.keySet()) {
            crossProductSize *= rawDataSources.get(key).size();
        }

        System.out.println(crossProductSize);
        assertThat(crossProductSize, equalTo((fromClause.makeMqlDataSources(rawDataSources)).size()));


    }

    public boolean isConvertedWithoutLeak(Map<String, List<Map<String, Object>>> rawDataSources, Map<String, List<Map<String, Object>>> mqlDataSources ) {
        Set<String> dataSourceIds = rawDataSources.keySet();
        if (dataSourceIds.equals(mqlDataSources.keySet())) {

            for (String dataSourceId : dataSourceIds) {
                List<Map<String, Object>> rawDataSource = rawDataSources.get(dataSourceId);
                List<Map<String, Object>> mqlDataSource = mqlDataSources.get(dataSourceId);

                for (int i = 0; i < rawDataSource.size(); i++) {
                    Map<String, Object> eachRawDataSourceRow = rawDataSource.get(i);
                    Map<String, Object> eachMqlDataSourceRow = mqlDataSource.get(i);

                    if (eachRawDataSourceRow.size() != eachMqlDataSourceRow.size())
                        return false;
                    else {
                        for (Map.Entry<String, Object> entry : eachRawDataSourceRow.entrySet()) {
                            if (!(eachMqlDataSourceRow.containsKey(dataSourceId + "." + entry.getKey()) &&
                                    entry.getValue().equals(eachMqlDataSourceRow.get(dataSourceId + "." + entry.getKey())))) {
                                return false;
                            }
                        }
                    }
                }
            }

            return true;
        }
        return false;
    }


    public List<Map<String, Object>> makeRawDataSource(String key) {
        List<Map<String, Object>> rawDataSource = new ArrayList<>();

        for (int i=0; i<1000; i++) {
            Map<String, Object> eachRow = new HashMap<>();
            for (int j = 0; j < 3; j++) {
                eachRow.put(key + i, UUID.randomUUID().toString());
            }
            rawDataSource.add(eachRow);
        }

        return rawDataSource;
    }
}
