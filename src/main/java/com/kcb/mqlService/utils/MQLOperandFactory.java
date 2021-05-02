package com.kcb.mqlService.utils;

import com.kcb.mqlService.mqlQueryDomain.mqlOperand.*;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.parser.ASTNodeAccessImpl;
import net.sf.jsqlparser.schema.Column;

public class MQLOperandFactory {
    private static MQLOperandFactory instance;

    private MQLOperandFactory(){}

    public static synchronized MQLOperandFactory getInstance() {
        if (instance == null) {
            instance = new MQLOperandFactory();
        }
        return instance;
    }

    public MQLOperand create(ASTNodeAccessImpl astNodeAccess, String expression) {
        if (astNodeAccess instanceof Column)
            return new ColumnOperand(expression);
        else if (astNodeAccess instanceof DoubleValue || astNodeAccess instanceof LongValue)
            return new NumberValueOperand(expression);
        else if (astNodeAccess instanceof StringValue)
            return new StringValueOperand(expression);
        else
            throw new RuntimeException("NOT VALID OPERAND TYPE");
    }

    public ValueOperand create(Object target) {
        if (target instanceof Number)
            return new NumberValueOperand(target.toString());
        else if (target instanceof String)
            return new StringValueOperand(target.toString());
        else
            throw new RuntimeException("NOT VALID OPERAND TYPE");
    }
}
