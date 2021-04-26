import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.FromClause;

import java.util.*;

public class Main {
    public static void main(String[] args) {
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
        System.out.println(fromClause.makeMqlDataSources(rawDataSources).size());


    }
    public static  List<Map<String, Object>> makeRawDataSource(String key) {
        List<Map<String, Object>> rawDataSource = new ArrayList<>();

        for (int i=0; i<300; i++) {
            Map<String, Object> eachRow = new HashMap<>();
            for (int j = 0; j < 3; j++) {
                eachRow.put(key + i, UUID.randomUUID().toString().substring(0, 3));
            }
            rawDataSource.add(eachRow);
        }

        return rawDataSource;
    }
}
