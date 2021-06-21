package com.kcb.mqlService.mqlFactory;

import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.MQLElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.ValueElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.groupFunction.SUM;
import com.kcb.mqlService.utils.DefinedFunction;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.schema.Column;

import java.util.ArrayList;
import java.util.List;


public class MQLFunctionFactory {
    private static MQLFunctionFactory factory;

    private MQLFunctionFactory(){}

    public synchronized static MQLFunctionFactory getInstance() {
        if (factory == null) {
            factory = new MQLFunctionFactory();
        }

        return factory;
    }

    public MQLElement create(Function function, String alias) {
        if (DefinedFunction.GROUP_FUNCTION.getDefinedFunctionList().contains(function.getName())) {
            return groupFunction(function, alias, null);
        } else {
            return singleRowFunction(function, alias);
        }

    }

    private MQLElement groupFunction(Function function, String alias, MQLElement visitElement) {
        MQLElement parameterElement = null;


        switch (function.getName()) {
            case "SUM" :
                visitElement =  new SUM(alias, parameter(function.getParameters().getExpressions().get(0), parameterElement));
        }

        return visitElement;
    }

    private MQLElement singleRowFunction(Function function, String alias) {

    }

    private List<MQLElement> parameters(ExpressionList parameterList) {

    }

    private MQLElement parameter(Expression parameter, MQLElement visitElement) {
        parameter.accept(visitor(visitElement));

        return visitElement;
    }

    private ExpressionVisitor visitor(MQLElement visitElement) {
        List<MQLElement> tmpList = new ArrayList<>();
        ExpressionVisitor visitor = new ExpressionVisitorAdapter() {
            @Override
            public void visit(Function function) {
                tmpList.add()
            }

            @Override
            public void visit(Column column) {

            }


            @Override
            public void visit(LongValue value) {

            }

            @Override
            public void visit(DoubleValue value) {

            }

            @Override
            public void visit(DateValue value) {

            }

            @Override
            public void visit(StringValue value) {

            }
        };

        return visitor;
    }
}
