package com.kcb.mqlService.mqlFactory;

import com.kcb.mqlService.mqlQueryDomain.MQLQueryContext;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;

public class MQLQueryContextFactory {
    private static MQLQueryContextFactory factory;
    private final CCJSqlParserManager jsqlParserManager = new CCJSqlParserManager();

    private MQLQueryContextFactory(){}

    public synchronized static MQLQueryContextFactory getInstance() {
        if (factory == null) {
            factory = new MQLQueryContextFactory();
        }

        return factory;
    }

    public MQLQueryContext create(String script) throws JSQLParserException {
        SqlContextStorage sqlContextStorage = new SqlContextStorage(script);

        return null;
    }









}
