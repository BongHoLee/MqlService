package com.kcb.mqlService.mqlQueryDomain.mqlData;

import java.util.*;

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

    public void addJoinList(String ... dataSourceIds) {
        joinSet.addAll(Arrays.asList(dataSourceIds));
    }

    public Set<String> matchedColumnSet(MQLTable compareTable) {
        Set<String> matched = new HashSet<>(tableData.get(0).keySet());
        Set<String> compare = compareTable.getTableData().get(0).keySet();
        matched.retainAll(compare);

        return matched;
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
