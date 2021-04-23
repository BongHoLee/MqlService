package com.kcb.mqlService.mqlQueryDomain.mqlExpression.mqlOperation;

import java.util.List;
import java.util.Map;

public interface MQLOperation {
    List<Map<String, Object>> operate(
            List<Map<String, Object>> left,
            List<Map<String, Object>> right,
            Map<String, List<Map<String, Object>>> mqlDataSource
    );
}
