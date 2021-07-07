package com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.singleRowFunction;

import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SUBSTR extends SingleRowFunctionElement {
    private String expression = "";
    private String alias = "";
    private boolean hasAlias;

    public SUBSTR(String alias) {
        super();
        this.alias = alias;
        setHasAlias();
    }

    public SUBSTR(String alias, MQLElement ... parameters) {
        super(parameters);
        this.alias = alias;
        makeExpression(Arrays.asList(parameters));
        setHasAlias();
    }

    public SUBSTR(MQLElement ... parameters) {
        this("",parameters);
    }

    public SUBSTR(String alias, List<MQLElement> parameters) {
        super(parameters);
        this.alias = alias;
        makeExpression(parameters);
        setHasAlias();
    }

    public void setParameters(List<MQLElement> parameters) {
        super.setParameters(parameters);
        makeExpression(parameters);
    }

    private void makeExpression(List<MQLElement> parameters) {
        expression = "SUBSTR(";

        for (int i=0; i<parameters.size(); i++) {
            MQLElement each = parameters.get(i);

            if (each instanceof ValueElement && ((ValueElement) each).getValueType() == ValueType.STRING) {
                expression += "'" + each.getElementExpression() + "'";
            } else {
                expression += each.getElementExpression();
            }

            if (i < parameters.size()-1) {
                expression += ",";
            }
        }

        expression += ")";
    }

    private void setHasAlias() {
        hasAlias = !alias.isEmpty();
    }

    @Override
    public String getElementExpression() {
        return expression;
    }

    @Override
    public boolean hasAlias() {
        return hasAlias;
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public Object executeWith(List<MQLElement> parameters, Map<String, Object> singleRow) {
        MQLElement p1 = parameters.get(0);
        MQLElement p2 = parameters.get(1);


        if (!(p2 instanceof ValueElement))
            throw new RuntimeException("SUBSTR second Parameter type is not valid");

        // SUBSTR('abc', 1, 5)
        if (parameters.size() > 2) {
            MQLElement p3 = parameters.get(2);
            if (!(p3 instanceof ValueElement))
                throw new RuntimeException("SUBSTR third Parameter type is not valid");
            try {
                return execute(p1, Integer.parseInt(p2.getElementExpression()), Integer.parseInt(p3.getElementExpression()), singleRow);
            } catch (RuntimeException e) {
                throw new RuntimeException("SUBSTR second, third Parameter type is not valid");
            }

        // SUBSTR('abc', 2)
        } else {
            try {
                return execute(p1, Integer.parseInt(p2.getElementExpression()), singleRow);
            } catch (RuntimeException e) {
                throw new RuntimeException("SUBSTR second Parameter type is not valid");
            }
        }


    }

    private Object execute(Object p1, int start, int end, Map<String, Object> singleRow) {
        int st = start - 1;
        int to = end + st;

        if (p1 instanceof String) {
            to = Math.min(((String) p1).length(), to);
            return ((String) p1).substring(st, to);
        }  else if (p1 instanceof ValueElement && ((ValueElement) p1).getValue() instanceof String) {
            to = Math.min(((String) ((ValueElement) p1).getValue()).length(), to);
            return ((String) ((ValueElement) p1).getValue()).substring(st, to);
        } else if (p1 instanceof ColumnElement && singleRow.get(((ColumnElement) p1).getColumnName()) instanceof String) {
            to = Math.min(((String) singleRow.get(((ColumnElement) p1).getColumnName())).length(), to);
            return ((String) singleRow.get(((ColumnElement) p1).getColumnName())).substring(st, to);
        } else if (p1 instanceof SingleRowFunctionElement){
            return execute(((SingleRowFunctionElement) p1).executeAbout(singleRow), start, end, singleRow);
        } else {
            throw new RuntimeException("Not Valid SUBSTR parameter type!");
        }
    }

    private Object execute(Object p1, int start, Map<String, Object> singleRow){
        int st = start - 1;
        if (p1 instanceof String) {
            return ((String) p1).substring(st);
        }  else if (p1 instanceof ValueElement && ((ValueElement) p1).getValue() instanceof String) {
            return ((String) ((ValueElement) p1).getValue()).substring(st);
        } else if (p1 instanceof ColumnElement && singleRow.get(((ColumnElement) p1).getColumnName()) instanceof String) {
            return ((String) singleRow.get(((ColumnElement) p1).getColumnName())).substring(st);
        } else if (p1 instanceof SingleRowFunctionElement){
            return execute(((SingleRowFunctionElement) p1).executeAbout(singleRow), start, singleRow);
        } else {
            throw new RuntimeException("Not Valid SUBSTR parameter type!");
        }
    }
}
