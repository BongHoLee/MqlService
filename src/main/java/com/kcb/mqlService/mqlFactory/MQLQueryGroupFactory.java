package com.kcb.mqlService.mqlFactory;

import com.kcb.mqlService.mqlQueryDomain.MQLQueryContext;
import com.kcb.mqlService.mqlQueryDomain.MQLQueryGroup;

import java.util.HashMap;
import java.util.Map;

public class MQLQueryGroupFactory {

    private static  MQLQueryGroupFactory instance;

    private MQLQueryGroupFactory(){}

    public synchronized static MQLQueryGroupFactory getInstance() {
        if (instance == null) {
            instance = new MQLQueryGroupFactory();
        }
        return instance;
    }

    public MQLQueryGroup create() {
        MQLQueryGroup queryGroup = new MQLQueryGroup();

        try {
            Map<String, String> scriptDocumentMap = MQLScriptDocumentFactory.getInstance().create();

            for (String queryId : scriptDocumentMap.keySet()) {
                MQLQueryContext context = MQLQueryContextFactory.getInstance().create(scriptDocumentMap.get(queryId));
                queryGroup.put(queryId, context);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return queryGroup;
    }


}