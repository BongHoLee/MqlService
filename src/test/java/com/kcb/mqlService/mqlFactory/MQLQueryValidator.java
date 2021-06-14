package com.kcb.mqlService.mqlFactory;

import net.sf.jsqlparser.schema.Table;

import java.util.HashMap;
import java.util.Map;

public class MQLQueryValidator {
    private static MQLQueryValidator validator;

    private MQLQueryValidator(){}

    public synchronized static MQLQueryValidator getInstance() {
        if (validator == null) {
            validator = new MQLQueryValidator();
        }

        return validator;
    }

    public boolean isValid(SqlContextStorage sqlContextStorage) {
        // only inner join

        // if tables or from has more than one datasource, context must have join clause
        return false;

    }

    private boolean dataSourceCountCondition(SqlContextStorage sqlContextStorage) {

        return false;
    }




}
