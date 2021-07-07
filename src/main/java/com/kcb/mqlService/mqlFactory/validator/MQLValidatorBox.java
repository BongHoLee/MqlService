package com.kcb.mqlService.mqlFactory.validator;

import com.kcb.mqlService.mqlFactory.SqlContextStorage;

import java.util.Arrays;
import java.util.List;

public class MQLValidatorBox implements MQLValidator{

    private List<MQLValidator> validators = Arrays.asList(
            new SyntaxValidator(),
            new FromMatchJoinValidator(),
            new ItemsOfGeneralClauseValidator(),
            new ItemsOfRelatedGroupByClauseValidator()
    );

    @Override
    public boolean isValid(SqlContextStorage sqlContextStorage) {
        for (MQLValidator validator: validators) {
            if (!validator.isValid(sqlContextStorage))
                return false;
        }

        return true;
    }
}
