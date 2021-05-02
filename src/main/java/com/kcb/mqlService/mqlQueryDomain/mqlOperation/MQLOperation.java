package com.kcb.mqlService.mqlQueryDomain.mqlOperation;

import java.util.List;
import java.util.Map;

public interface MQLOperation {
    List<Map<String, Object>> operateWith(Map<String, List<Map<String, Object>>> mqlDataSource);
}
