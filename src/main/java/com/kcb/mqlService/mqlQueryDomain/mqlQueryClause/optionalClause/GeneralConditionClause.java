package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.optionalClause;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.MQLOperatingExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.logicalOperator.ANDOperator;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.OptionalClause;

import java.util.Arrays;
import java.util.List;

public class GeneralConditionClause implements OptionalClause {
    private List<GeneralCondition> generalConditions;

    public GeneralConditionClause(List<GeneralCondition> generalConditions) {
        this.generalConditions = generalConditions;
    }

    public GeneralConditionClause(GeneralCondition ... generalConditions) {
        this.generalConditions = Arrays.asList(generalConditions);
    }

    @Override
    public MQLDataStorage executeClause(MQLDataStorage mqlDataStorage) {
        MQLOperatingExpression left = generalConditions.get(0).getOperatingExpression();

        for(int i=1; i < generalConditions.size(); i++) {
            MQLOperatingExpression right = generalConditions.get(i).getOperatingExpression();
            left = new ANDOperator(left, right);
        }

        return left.operatingWith(mqlDataStorage);
    }
}
