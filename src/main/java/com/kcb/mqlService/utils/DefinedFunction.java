package com.kcb.mqlService.utils;

import java.util.Arrays;
import java.util.List;

public enum DefinedFunction {
    SINGLE_ROW_FUNCTION(Arrays.asList("LENGTH", "LOWER", "SUBSTR", "UPPER")),
    GROUP_FUNCTION(Arrays.asList("AVG", "COUNT", "MAX", "MIN", "SUM"));

    private List<String> definedFunctionList;

    DefinedFunction(List<String> definedFunctionList) {
        this.definedFunctionList = definedFunctionList;
    }

    public List<String> getDefinedFunctionList() {
        return definedFunctionList;
    }
}
