package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
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

        MQLDataSource mqlDataSource = fromClause.makeMqlDataSources(rawDataSources);

        System.out.println(mqlDataSource.getDataSourcesId());
        System.out.println(mqlDataSource.getMqlDataSources());


        assertThat(true, equalTo(isConvertedWithoutLeak(rawDataSources, mqlDataSource.getMqlDataSources())));
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


        for (int i=0; i<10; i++) {
            Map<String, Object> eachRow = new HashMap<>();
            for (int j = 0; j < 3; j++) {
                eachRow.put(key + i, UUID.randomUUID().toString().substring(0, 3));
            }
            rawDataSource.add(eachRow);
        }

        return rawDataSource;
    }

    @Test
    public void mapCopyAndRemoveTest() {
        List<Map<String, Object>> storeList = new ArrayList<>();

        Map<String, Object> map1 = new HashMap<>();
        map1.put("a", 1);
        map1.put("b", 1);
        map1.put("c", 1);
        map1.put("d", 1);
        map1.put("e", 1);

        Map<String, Object> map2 = new HashMap<>();
        map2.put("AA", 1);
        map2.put("BB", 1);

        map1.putAll(map2);

        Map<String, Object> map3 = new HashMap<>(map1);
        storeList.add(map3);

        map1.keySet().removeAll(map2.keySet());

        System.out.println(map1);
        System.out.println(map2);
        System.out.println(map3);
        System.out.println(storeList.get(0));

        assertThat(map1, equalTo(storeList.get(0)));

    }
}
