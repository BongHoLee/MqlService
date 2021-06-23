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

    public MQLQueryContext create(String script) {
        SqlContextStorage sqlContextStorage = new SqlContextStorage(script);
        if (sqlContextStorage.isValid()) {
            return new MQLQueryContext(SelectClauseFactory.getInstance().create(sqlContextStorage));
        } else {
            logger.error("MQL Validation Failed : {}", script);
            throw new MQLQueryNotValidException();
        }

    }

}
