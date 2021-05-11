package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause;

import com.kcb.mqlService.mqlQueryDomain.mqlOperator.joinOperator.MQLJoinOperator;
import com.kcb.mqlService.mqlQueryDomain.mqlOperator.whereOperator.MQLWhereOperator;

public class SelectClause{
    private FromClause fromClause;
    private MQLJoinOperator joinOperator;
    private MQLWhereOperator whereOperator;


    public SelectClause(FromClause fromClause, MQLJoinOperator joinOperator, MQLWhereOperator whereOperator) {
        this.fromClause = fromClause;
        this.joinOperator = joinOperator;
        this.whereOperator = whereOperator;
    }
}
