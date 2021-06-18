package com.kcb.mqlService.mqlFactory.validator;

import com.kcb.mqlService.mqlFactory.SqlContextStorage;
import com.kcb.mqlService.mqlFactory.exception.MQLQueryNotValidException;
import net.sf.jsqlparser.statement.select.PlainSelect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SyntaxValidator implements MQLValidator{
    private static final Logger logger = LogManager.getLogger(SyntaxValidator.class);

    @Override
    public boolean isValid(SqlContextStorage sqlContextStorage) {
        PlainSelect plainSelect = sqlContextStorage.getPlainSelect();

        if (plainSelect.getFromItem() == null) {
            logger.error("MQL Query Must Have 'FROM' clause : {}", sqlContextStorage.getSelect().toString());
            throw  new MQLQueryNotValidException();
        }

        if (plainSelect.getGroupBy() == null && plainSelect.getHaving() != null) {
            logger.error("'GROUP BY' clause must exist to use 'HAVING' clause : {}", sqlContextStorage.getSelect().toString());
            throw  new MQLQueryNotValidException();
        }


        return true;
    }
}
