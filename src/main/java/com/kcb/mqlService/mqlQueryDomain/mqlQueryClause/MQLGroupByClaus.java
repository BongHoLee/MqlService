package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * Deal With Aggregate Function
 */
public class MQLGroupByClaus {
    private List<String> groupingElements;

    public MQLGroupByClaus(String ... elements) {
        groupingElements = new ArrayList<>(Arrays.asList(elements));
    }

    public MQLTable groupingWith()
}
