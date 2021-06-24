package com.kcb.mqlService.mqlFactory.validator;

import com.kcb.mqlService.mqlFactory.SqlContextStorage;
import com.kcb.mqlService.mqlFactory.exception.MQLQueryNotValidException;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectVisitorAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SyntaxValidator implements MQLValidator{
    private static final Logger logger = LogManager.getLogger(SyntaxValidator.class);

    @Override
    public boolean isValid(SqlContextStorage sqlContextStorage) {
        PlainSelect plainSelect = sqlContextStorage.getPlainSelect();

        if (plainSelect.getFromItem() == null) {
            logger.error("Query ID : {}, MQL Query Must Have 'FROM' clause : {}", sqlContextStorage.getQueryId(), sqlContextStorage.getSelect().toString());
            throw new MQLQueryNotValidException(sqlContextStorage.getQueryId() + "is not valid query");
        }

        if (plainSelect.getGroupBy() == null && plainSelect.getHaving() != null) {
            logger.error("Query ID : {}, 'GROUP BY' clause must exist to use 'HAVING' clause : {}", sqlContextStorage.getQueryId(), sqlContextStorage.getSelect().toString());
            throw new MQLQueryNotValidException(sqlContextStorage.getQueryId() + "is not valid query");
        }



        return true;
    }
}
