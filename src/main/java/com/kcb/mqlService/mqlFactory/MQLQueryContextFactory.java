package com.kcb.mqlService.mqlFactory;

import com.kcb.mqlService.mqlFactory.exception.MQLQueryNotValidException;
import com.kcb.mqlService.mqlFactory.validator.SyntaxValidator;
import com.kcb.mqlService.mqlQueryDomain.MQLQueryContext;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.SelectClause;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MQLQueryContextFactory {
    private static MQLQueryContextFactory factory;
    private static final Logger logger = LogManager.getLogger(MQLQueryContextFactory.class);

    private MQLQueryContextFactory(){}

    public synchronized static MQLQueryContextFactory getInstance() {
        if (factory == null) {
            factory = new MQLQueryContextFactory();
        }

        return factory;
    }

    public MQLQueryContext create(String queryId, String script) {
        SqlContextStorage sqlContextStorage = new SqlContextStorage(queryId, script);
        if (sqlContextStorage.isValid()) {
            return new MQLQueryContext(SelectClauseFactory.getInstance().create(sqlContextStorage));
        } else {
            logger.error("Query Id : {}, MQL Validation Failed : {}", queryId, script);
            throw new MQLQueryNotValidException(queryId + "is not valid query");
        }

    }

}
