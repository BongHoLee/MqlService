package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause;

import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.joinOperator.MQLJoinOperator;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.joinOperator.logicalOperator.AndToJoinOperator;

import java.util.Arrays;
import java.util.List;

public class JoinClaus {
    private List<MQLJoinOperator> joinOperators;

    public JoinClaus(MQLJoinOperator ... joinOperators) {
        this.joinOperators = Arrays.asList(joinOperators);
    }

    public MQLTable join(MQLDataSource mqlDataSource) {
        MQLJoinOperator allJoined = joinLink(joinOperators.get(0), 0);
        return allJoined.operateWith(mqlDataSource);
    }

    // if it is a Multiple join, 'AND' Operation is operated on each join result
    // recursive
    private MQLJoinOperator joinLink(MQLJoinOperator currentOperator, int idx) {
        if (joinOperators.size() > idx+1) {
            MQLJoinOperator nextOperator = new AndToJoinOperator(currentOperator, joinOperators.get(idx+1));
            return joinLink(nextOperator, idx+1);
        } else {
            return currentOperator;
        }
    }
}
