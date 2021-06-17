package com.kcb.mqlService.mqlFactory.validator;

import com.kcb.mqlService.mqlFactory.SqlContextStorage;

public interface MQLValidator {
    boolean isValid(SqlContextStorage sqlContextStorage);
}
