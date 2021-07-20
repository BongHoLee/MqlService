package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.testData.TestDataFactory;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class FromClauseTest {

    @Test
    public void makeMqlDataSourcesTest() {

        Map<String, List<Map<String, Object>>> rawDataSource = new HashMap<>();
        rawDataSource.put("A", TestDataFactory.tableOf("categories"));
        rawDataSource.put("B", TestDataFactory.tableOf("employees"));
        rawDataSource.put("C", TestDataFactory.tableOf("shippers"));

        FromClause fromClause = new FromClause();

        MQLDataSource mqlDataSource = fromClause.makeMqlDataSources("testQuery", "testQueryScript", rawDataSource).getMqlDataSource();


        assertThat(true, equalTo(isConvertedWithoutLeak(rawDataSource, mqlDataSource.getMqlDataSources())));
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
    
}
