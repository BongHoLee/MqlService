package com.kcb.mqlService.mqlQueryDomain.mqlExpression;

import com.kcb.mqlService.mqlQueryDomain.mqlExpression.mqlOperation.MQLExpression;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.mqlOperation.MQLOperation;

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
    private MQLOperation operation;

    public OperatingMQLExpression(MQLExpression leftExpression, MQLExpression rightExpression, MQLOperation operation) {
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
        this.operation = operation;
    }

    public List<Map<String, Object>> operateWith(Map<String, List<Map<String, Object>>> mqlDataSource) {

        if (leftExpression != null && rightExpression != null) {

        }

    }
}
