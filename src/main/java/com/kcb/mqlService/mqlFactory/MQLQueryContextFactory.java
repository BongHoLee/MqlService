package com.kcb.mqlService.mqlFactory;

import com.kcb.mqlService.mqlFactory.exception.MQLQueryNotValidException;
import com.kcb.mqlService.mqlFactory.validator.SyntaxValidator;
import com.kcb.mqlService.mqlQueryDomain.MQLQueryContext;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.SelectClause;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MQLQueryContextFactory {
    private static MQLQueryContextFactory factory;
    private static final Logger logger = LoggerFactory.getLogger(MQLQueryContextFactory.class);

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
            return new MQLQueryContext(SelectClauseFactory.getInstance().create(sqlContextStorage), sqlContextStorage.getUsedTableAliasWithName());
        } else {
            logger.error("Query Id : {}, MQL Validation Failed : {}", queryId, script);
            throw new MQLQueryNotValidException(queryId + "is not valid query");
        }

    }

}
