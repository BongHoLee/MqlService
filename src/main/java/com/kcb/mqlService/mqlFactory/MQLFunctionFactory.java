package com.kcb.mqlService.mqlFactory;

import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.*;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.groupFunction.*;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.singleRowFunction.LENGTH;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.singleRowFunction.LOWER;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.singleRowFunction.SUBSTR;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.singleRowFunction.UPPER;
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
            return groupFunction(function, alias);
        } else {
            return singleRowFunction(function, alias);
        }

    }

    private MQLElement groupFunction(Function function, String alias) {
        GroupFunctionElement groupFunctionElement = null;

        switch (function.getName()) {
            case "SUM" :
                groupFunctionElement = new SUM(alias);
                break;
            case "MIN" :
                groupFunctionElement = new MIN(alias);
                break;
            case "MAX" :
                groupFunctionElement = new MAX(alias);
                break;
            case "COUNT" :
                groupFunctionElement = new COUNT(alias);
                break;
            case "AVG" :
                groupFunctionElement = new AVG(alias);
                break;
        }

        setGroupFunctionParameter(function, groupFunctionElement);
        return groupFunctionElement;
    }

    private void setGroupFunctionParameter(Function function, GroupFunctionElement groupFunctionElement) {

        if (function.isAllColumns()) {
            groupFunctionElement.addParameter(new ColumnElement("*"));
        } else {
            function.getParameters().getExpressions().get(0).accept(new ExpressionVisitorAdapter() {
                @Override
                public void visit(Column column) {
                    groupFunctionElement.addParameter(new ColumnElement(column.getName(true)));
                }

                @Override
                public void visit(Function function) {
                    groupFunctionElement.addParameter(MQLFunctionFactory.getInstance().create(function, ""));
                }

                @Override
                public void visit(DoubleValue value) {
                    groupFunctionElement.addParameter(new ValueElement(value.getValue()));
                }

                @Override
                public void visit(LongValue value) {
                    groupFunctionElement.addParameter(new ValueElement(value.getValue()));
                }

                @Override
                public void visit(DateValue value) {
                    groupFunctionElement.addParameter(new ValueElement(value.getValue().toString()));
                }

                @Override
                public void visit(StringValue value) {
                    groupFunctionElement.addParameter(new ValueElement(value.getValue()));
                }
            });
        }

    }

    private MQLElement singleRowFunction(Function function, String alias) {
        SingleRowFunctionElement singleRowFunctionElement = null;

        switch (function.getName()) {
            case "LENGTH":
                singleRowFunctionElement = new LENGTH(alias);
                break;
            case "LOWER":
                singleRowFunctionElement = new LOWER(alias);
                break;
            case "SUBSTR":
                singleRowFunctionElement = new SUBSTR(alias);
                break;
            case "UPPER":
                singleRowFunctionElement = new UPPER(alias);
                break;
        }

        List<MQLElement> parameters = new ArrayList<>();
        setSingleRowFunctionParameters(function, parameters);
        singleRowFunctionElement.setParameters(parameters);
        return singleRowFunctionElement;
    }

    private void setSingleRowFunctionParameters(Function function, List<MQLElement> parameters) {
        function.getParameters().getExpressions().forEach(expression ->  {
            expression.accept(new ExpressionVisitorAdapter(){

                @Override
                public void visit(Function function) {
                    parameters.add(MQLFunctionFactory.getInstance().create(function, ""));
                }

                @Override
                public void visit(Column column) {
                    parameters.add(new ColumnElement(column.getName(true)));
                }

                @Override
                public void visit(DoubleValue value) {
                    parameters.add(new ValueElement(value.getValue()));
                }

                @Override
                public void visit(LongValue value) {
                    parameters.add(new ValueElement(value.getValue()));
                }

                @Override
                public void visit(DateValue value) {
                    parameters.add(new ValueElement(value.getValue().toString()));
                }

                @Override
                public void visit(StringValue value) {
                    parameters.add(new ValueElement(value.getValue()));
                }
            });
        });
    }


}
