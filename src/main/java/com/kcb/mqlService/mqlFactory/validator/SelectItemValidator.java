package com.kcb.mqlService.mqlFactory.validator;

import com.kcb.mqlService.mqlFactory.SqlContextStorage;

public class SelectItemValidator implements ClauseValidator{
    @Override
    public boolean isValid(SqlContextStorage sqlContextStorage) {
        return false;
    }
}
