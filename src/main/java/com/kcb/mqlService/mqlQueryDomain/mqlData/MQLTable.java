package com.kcb.mqlService.mqlQueryDomain.mqlData;

import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.MQLElement;

import java.util.*;

public class MQLTable {
    private Set<String> joinSet;
    private List<Map<String, Object>> tableData;
    private boolean isGrouped = false;
    private List<MQLElement> groupingElements;

    public MQLTable(Set<String> joinSet, List<Map<String, Object>> tableData, boolean isGrouped, List<MQLElement> groupingElements) {
        this.joinSet = new HashSet<>(joinSet);
        this.tableData = new ArrayList<>(tableData);
        this.isGrouped = isGrouped;
        this.groupingElements = new ArrayList<>(groupingElements);
    }

    public MQLTable(MQLTable mqlTable) {
        this.joinSet = new HashSet<>(mqlTable.getJoinSet());
        this.tableData = new ArrayList<>(mqlTable.getTableData());
        this.isGrouped = mqlTable.isGrouped();
        this.groupingElements = new ArrayList<>(mqlTable.getGroupingElements());
    }

    public MQLTable(Set<String> joinSet, List<Map<String, Object>> tableData) {
        this(joinSet, tableData, false, new ArrayList<>());

    }

    public MQLTable(Set<String> joinSet) {
        this(joinSet, new ArrayList<>(), false, new ArrayList<>());
    }

    public MQLTable(List<Map<String, Object>> tableData) {
        this(new HashSet<>(), tableData, false, new ArrayList<>());
    }

    public MQLTable() {
        this(new HashSet<>(), new ArrayList<>(), false, new ArrayList<>());
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

    public boolean isGrouped() {
        return isGrouped;
    }

    public void setGrouped(boolean isGrouped) {
        this.isGrouped = isGrouped;
    }

    public void setGroupingElements(List<MQLElement> groupingElements) {
        this.groupingElements = groupingElements;
    }

    public List<MQLElement> getGroupingElements() {
        return groupingElements;
    }
}
