package com.kcb.mqlService.mqlQueryDomain.mqlExpression;

import java.util.List;
import java.util.Map;

public interface MQLExpression {
    public List<Map<String, Object>> operateWith(Map<String, List<Map<String, Object>>> mqlDataSource);
}
