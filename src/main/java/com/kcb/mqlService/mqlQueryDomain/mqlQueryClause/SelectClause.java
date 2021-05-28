package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.MQLElement;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.optionalClause.NoneClause;

import java.util.*;

public class SelectClause{
    private List<MQLElement> selectElements = new ArrayList<>();
    private FromClause from;
    private List<OptionalClause> optionalClauses = Collections.singletonList(new NoneClause());

    public SelectClause(List<MQLElement> selectElements, FromClause from, List<OptionalClause> optionalClauses) {
        this.selectElements = selectElements;
        this.from = from;
        this.optionalClauses = optionalClauses;
    }

    public SelectClause(List<MQLElement> selectElements, FromClause from) {
        this(selectElements, from, Collections.singletonList(new NoneClause()));
    }

    public SelectClause(List<MQLElement> selectElements, FromClause from, OptionalClause ... optionalClauses) {
        this(selectElements, from, Arrays.asList(optionalClauses));
    }


    public List<Map<String, Object>> executeQueryWith(Map<String, List<Map<String, Object>>> rawDataSource) {
        MQLDataStorage dataStorage = from.makeMqlDataSources(rawDataSource);

        for (OptionalClause eachClause : optionalClauses) {
            dataStorage = eachClause.executeClause(dataStorage);
        }

        return null;
    }


}
