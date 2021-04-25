package com.kcb.mqlService.mqlQueryDomain.mqlExpression;

import java.util.List;
import java.util.Map;


/**
 *
 *
 *
 */

public class OperatingMQLExpression implements MQLExpression {

    private MQLExpression leftExpression;
    private MQLExpression rightExpression;


    public OperatingMQLExpression(MQLExpression leftExpression, MQLExpression rightExpression) {
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
    }

    @Override
    public List<Map<String, Object>> operateWith(Map<String, List<Map<String, Object>>> mqlDataSource) {

    }
}
