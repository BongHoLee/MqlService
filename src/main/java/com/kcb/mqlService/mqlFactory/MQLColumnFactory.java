package com.kcb.mqlService.mqlFactory;

import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.MQLElement;
import net.sf.jsqlparser.schema.Column;

public class MQLColumnFactory {
    private static MQLColumnFactory factory;

    private MQLColumnFactory(){}

    public synchronized static MQLColumnFactory getInstance() {
        if (factory == null) {
            factory = new MQLColumnFactory();
        }

        return factory;
    }

    public MQLElement create(Column column, String alias) {

    }
}
