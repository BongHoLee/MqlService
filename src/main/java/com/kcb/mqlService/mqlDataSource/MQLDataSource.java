package com.kcb.mqlService.mqlDataSource;

import java.util.List;
import java.util.Map;

public class MQLDataSource {
    private String dataSourceId;
    private List<Map<String, Object>> rawDataSource;

    public MQLDataSource(String dataSourceId, List<Map<String, Object>> rawDataSource) {
        this.dataSourceId = dataSourceId;
        this.rawDataSource = rawDataSource;
    }


}
