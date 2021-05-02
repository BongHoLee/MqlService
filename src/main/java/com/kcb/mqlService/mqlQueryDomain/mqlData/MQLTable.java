package com.kcb.mqlService.mqlQueryDomain.mqlData;

import java.util.*;
import java.util.stream.Collectors;

public class MQLTable {
    private Set<String> joinSet;
    private List<Map<String, Object>> tableData;

    public MQLTable(Set<String> joinSet, List<Map<String, Object>> tableData) {
        this.joinSet = joinSet;
        this.tableData = tableData;
    }

    public MQLTable(Set<String> joinSet) {
        this(joinSet, new ArrayList<>());
    }

    public MQLTable(List<Map<String, Object>> tableData) {
        this(new HashSet<>(), tableData);
    }

    public MQLTable() {
        this(new HashSet<>(), new ArrayList<>());
    }

    public boolean addJoinList(String dataSourceId) {
        return joinSet.add(dataSourceId);
    }

    public boolean alreadyJoined(String dataSourceId) {
        return joinSet.contains(dataSourceId);
    }

    public boolean alreadyJoined(Collection<String> dataSources) {
        return joinSet.containsAll(dataSources);
    }

    public List<String> matchedDataSourceId(MQLTable compareTable) {
        return joinSet.stream().filter(eachDataSourceId -> compareTable.getJoinSet().contains(eachDataSourceId)).collect(Collectors.toList());
    }

    public List<Map<String, Object>> getTableData() {
        return tableData;
    }

    public Set<String> getJoinSet() {
        return joinSet;
    }


    public void setTableData(List<Map<String, Object>> tableData) {
        this.tableData = tableData;
    }
}
