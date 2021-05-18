package com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.singleRowFunction;

import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.ColumnElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.MQLElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.SingleRowFunctionElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.ValueElement;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SUBSTR extends SingleRowFunctionElement {

    public SUBSTR(MQLElement ... parameters) {
        super(parameters);
    }

    @Override
    public String getElementExpression() {
        return "SUBSTR";
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
            return ((String) p1).substring(st, to);
        }  else if (p1 instanceof ValueElement && ((ValueElement) p1).getValue() instanceof String) {
            return ((String) ((ValueElement) p1).getValue()).substring(st, to);
        } else if (p1 instanceof ColumnElement && singleRow.get(((ColumnElement) p1).getColumnName()) instanceof String) {
            return ((String) singleRow.get(((ColumnElement) p1).getColumnName())).substring(st, to);
        } else if (p1 instanceof SingleRowFunctionElement){
            return execute(((SingleRowFunctionElement) p1).executeAbout(singleRow), start, end, singleRow);
        } else {
            throw new RuntimeException("UPPER Parameter must String");
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
            throw new RuntimeException("UPPER Parameter must String");
        }
    }
}
