package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.*;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.optionalClause.NoneClause;

import java.util.*;

public class SelectClause{
    private List<MQLElement> selectElements = new ArrayList<>();
    private FromClause from;
    private List<OptionalClause> optionalClauses = Collections.singletonList(new NoneClause());

    private List<ValueElement> valueElements = new ArrayList<>();
    private List<ColumnElement> columnElements = new ArrayList<>();
    private List<SingleRowFunctionElement> singleRowFunctionElements = new ArrayList<>();
    private List<GroupFunctionElement> groupFunctionElements = new ArrayList<>();

    public SelectClause(List<MQLElement> selectElements, FromClause from, List<OptionalClause> optionalClauses) {
        this.selectElements = selectElements;
        this.from = from;
        this.optionalClauses = optionalClauses;
        distinguish();
    }
    public SelectClause(List<MQLElement> selectElements, FromClause from) {
        this(selectElements, from, Collections.singletonList(new NoneClause()));
    }

    public SelectClause(List<MQLElement> selectElements, FromClause from, OptionalClause ... optionalClauses) {
        this(selectElements, from, Arrays.asList(optionalClauses));
    }

    private void distinguish() {
        for (MQLElement eachElement : selectElements) {
            if (eachElement instanceof ValueElement) {
                valueElements.add((ValueElement) eachElement);
            } else if (eachElement instanceof ColumnElement) {
                columnElements.add((ColumnElement) eachElement);
            } else if (eachElement instanceof SingleRowFunctionElement) {
                singleRowFunctionElements.add((SingleRowFunctionElement) eachElement);
            } else if (eachElement instanceof GroupFunctionElement) {
                groupFunctionElements.add((GroupFunctionElement) eachElement);
            }

        }
    }


    public List<Map<String, Object>> executeQueryWith(Map<String, List<Map<String, Object>>> rawDataSource) {
        MQLDataStorage dataStorage = from.makeMqlDataSources(rawDataSource);

        for (OptionalClause eachClause : optionalClauses) {
            dataStorage = eachClause.executeClause(dataStorage);
        }

        return extractFrom(dataStorage);
    }

    private List<Map<String, Object>> extractFrom(MQLDataStorage dataStorage) {


        if (dataStorage.getMqlTable().isGrouped()) {
            return extractFromGroup(dataStorage);
        } else {
            return extractFromPlain(dataStorage);
        }
    }

    private List<Map<String, Object>> extractFromGroup(MQLDataStorage dataStorage) {

        List<Map<String, Object>> extractedResult = new ArrayList<>();
        List<Integer> groupingIdx = dataStorage.getMqlTable().getGroupingIdxs();

        int start = 0;
        for (int end : groupingIdx) {
            Map<String, Object> extractedMap = new HashMap<>();
            extractColumn(end, dataStorage, extractedMap);
            extractValue(extractedMap);
            extractSingleRowFunction(end, dataStorage, extractedMap);
            extractGroupFunction(start, end, dataStorage, extractedMap);
            extractedResult.add(extractedMap);

            start = end + 1;
        }

        return extractedResult;
    }

    private List<Map<String, Object>> extractFromPlain(MQLDataStorage dataStorage) {
        List<Map<String, Object>> extractedResult = new ArrayList<>();

        // only group element has (ex : COUNT(*))
        if (valueElements.isEmpty() && columnElements.isEmpty() && singleRowFunctionElements.isEmpty()) {
            Map<String, Object> extractedMap = new HashMap<>();
            extractGroupFunction(0, dataStorage.getMqlTable().getTableData().size()-1, dataStorage, extractedMap);
            extractedResult.add(extractedMap);

            return extractedResult;
        } else {
            for (int i=0; i<dataStorage.getMqlTable().getTableData().size(); i++) {
                Map<String, Object> extractedMap = new HashMap<>();
                extractColumn(i, dataStorage, extractedMap);
                extractValue(extractedMap);
                extractSingleRowFunction(i, dataStorage, extractedMap);
                extractGroupFunction(0, dataStorage.getMqlTable().getTableData().size()-1, dataStorage, extractedMap);
                extractedResult.add(extractedMap);
            }

            return extractedResult;
        }

    }

    private void extractColumn(int idx, MQLDataStorage dataStorage, Map<String, Object> map) {
        for (ColumnElement element : columnElements) {
            map.put(element.getColumnName(), dataStorage.getMqlTable().getTableData().get(idx).get(element.getColumnName()));
        }

    }

    private void extractValue(Map<String, Object> map) {
        for (ValueElement element : valueElements) {
            map.put(element.getElementExpression(), element.getValue());
        }
    }

    private void extractSingleRowFunction(int idx, MQLDataStorage dataStorage, Map<String, Object> map) {
        for (SingleRowFunctionElement element : singleRowFunctionElements) {
            Map<String, Object> row = dataStorage.getMqlTable().getTableData().get(idx);
            if (row.containsKey(element.getElementExpression())) {
                map.put(element.getElementExpression(), row.get(element.getElementExpression()));
            } else {
                map.put(element.getElementExpression(), element.executeAbout(row));
            }
        }
    }

    private void extractGroupFunction(int start, int end, MQLDataStorage dataStorage, Map<String, Object> map) {
        for (GroupFunctionElement element : groupFunctionElements) {
            map.put(element.getElementExpression(), element.executeAbout(start, end, dataStorage));
        }
    }




}
