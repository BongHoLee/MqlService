package com.kcb.mqlService.mqlFactory.validator;

import com.kcb.mqlService.mqlFactory.SqlContextStorage;

public interface ClauseValidator {
    boolean isValid(SqlContextStorage sqlContextStorage);
}
