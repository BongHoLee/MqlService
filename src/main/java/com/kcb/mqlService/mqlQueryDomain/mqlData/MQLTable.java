package com.kcb.mqlService.mqlQueryDomain.mqlData;

import java.util.*;

public class MQLTable {
    private Set<String> joinSet;
    private List<Map<String, Object>> tableData;
    private boolean isGrouped = false;
    private List<String> groupingElements;
    private List<Integer> groupingIdx;

    public MQLTable(Set<String> joinSet, List<Map<String, Object>> tableData, boolean isGrouped, List<String> groupingElements, List<Integer> groupingIdx) {
        this.joinSet = new HashSet<>(joinSet);
        this.tableData = new ArrayList<>(tableData);
        this.isGrouped = isGrouped;
        this.groupingElements = new ArrayList<>(groupingElements);
        this.groupingIdx = new ArrayList<>(groupingIdx);
    }

    public MQLTable(MQLTable mqlTable) {
        this.joinSet = new HashSet<>(mqlTable.getJoinSet());
        this.tableData = new ArrayList<>(mqlTable.getTableData());
        this.isGrouped = mqlTable.isGrouped();
        this.groupingElements = new ArrayList<>(mqlTable.getGroupingElements());
        this.groupingIdx = new ArrayList<>(mqlTable.getGroupingIdxs());
    }

    public MQLTable(Set<String> joinSet, List<Map<String, Object>> tableData) {
        this(joinSet, tableData, false, new ArrayList<>(), new ArrayList<>());

    }

    public MQLTable(Set<String> joinSet) {
        this(joinSet, new ArrayList<>(), false, new ArrayList<>(), new ArrayList<>());
    }

    public MQLTable(List<Map<String, Object>> tableData) {
        this(new HashSet<>(), tableData, false, new ArrayList<>(), new ArrayList<>());
    }

    public MQLTable() {
        this(new HashSet<>(), new ArrayList<>(), false, new ArrayList<>(), new ArrayList<>());
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

    public void setGroupingElements(List<String> groupingElements) {
        this.groupingElements = groupingElements;
        setGroupingIdx();
    }

    // set grouping idx
    private void setGroupingIdx() {

        List<Integer> groupingIndex = new ArrayList<>();
        Map<String, Object> last = new HashMap<>();

        for (int i=0; i<tableData.size(); i++) {
            if (i == 0) {
                last = tableData.get(i);
                continue;
            }

            Map<String, Object> cur = tableData.get(i);

            for (String groupElement : groupingElements) {
                if (!(cur.get(groupElement).equals(last.get(groupElement)))) {
                    groupingIndex.add(i-1);
                    break;
                }
            }

            if (i == tableData.size() - 1) {
                groupingIndex.add(i);
            }
            last = cur;
        }

        this.groupingIdx = groupingIndex;

    }


    public List<String> getGroupingElements() {
        return groupingElements;
    }

    public void setGroupingIdx(List<Integer> groupingIdx) {
        this.groupingIdx = groupingIdx;
    }

    public List<Integer> getGroupingIdxs() {
        return groupingIdx;
    }
}
