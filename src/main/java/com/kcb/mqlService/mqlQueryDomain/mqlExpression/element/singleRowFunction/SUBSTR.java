package com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.singleRowFunction;

import com.kcb.mqlService.mqlFactory.exception.MQLQueryExecuteException;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.*;
import com.kcb.mqlService.utils.ExceptionThrowerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SUBSTR extends SingleRowFunctionElement {
    private static final Logger logger = LoggerFactory.getLogger(SUBSTR.class);

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


        if (!(p2 instanceof ValueElement)) {
            logger.error("MQL Execute Exception. SUBSTR's Second Parameter Type Must `Value`");
            throw new MQLQueryExecuteException("SUBSTR second Parameter type is not valid");
        }

        // SUBSTR('abc', 1, 5)
        if (parameters.size() > 2) {
            MQLElement p3 = parameters.get(2);
            if (!(p3 instanceof ValueElement)) {
                logger.error("MQL Execute Exception. SUBSTR's Third Parameter Type Must `Value`");
                throw new MQLQueryExecuteException("SUBSTR third Parameter type is not valid");
            }
            try {
                return execute(p1, Integer.parseInt(p2.getElementExpression()), Integer.parseInt(p3.getElementExpression()), singleRow);
            } catch (RuntimeException e) {
                e.printStackTrace();
                logger.error("MQL Execute Exception. SUBSTR's Second, Third Parameter Type Must `Value`");
                throw new MQLQueryExecuteException("SUBSTR second, third Parameter type is not valid");
            }

        // SUBSTR('abc', 2)
        } else {
            try {
                return execute(p1, Integer.parseInt(p2.getElementExpression()), singleRow);
            } catch (RuntimeException e) {
                e.printStackTrace();
                logger.error("MQL Execute Exception. SUBSTR's Second Parameter Type Must `Value`");
                throw new MQLQueryExecuteException("SUBSTR second Parameter type is not valid");
            }
        }


    }

    private Object execute(Object p1, int start, int end, Map<String, Object> singleRow) {
        start = start == 0 ? 1 : start;

        if (end < 1) {
            logger.error("MQL Execute Exception. SUBSTR(param, start, end) `end` must larger than 1");
            throw new MQLQueryExecuteException("MQL Execute Exception. SUBSTR(param, start, end) `end` must larger than 1");
        }

        if (start > 0) {
            return positiveExecute(p1, start, end, singleRow);
        } else {
            return negativeExecute(p1, start, end, singleRow);
        }




    }

    private Object positiveExecute(Object p1, int start, int end, Map<String, Object> singleRow) {
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
            if (p1 instanceof ColumnElement)
                ExceptionThrowerUtil.isValidRow(singleRow, ((ColumnElement) p1).getColumnName());

            logger.error("Not Valid SUBSTR parameter type!");
            throw new MQLQueryExecuteException("Not Valid SUBSTR parameter type!");
        }
    }

    private Object negativeExecute(Object p1, int start, int end, Map<String, Object> singleRow) {
        if (p1 instanceof String) {
            int st = ((String)p1).length() + start;
            int to = end + st;
            to = Math.min(((String) p1).length(), to);
            return ((String) p1).substring(st, to);
        }  else if (p1 instanceof ValueElement && ((ValueElement) p1).getValue() instanceof String) {
            int st = ((String) ((ValueElement) p1).getValue()).length() + start;
            int to = end + st;
            to = Math.min(((String) ((ValueElement) p1).getValue()).length(), to);
            return ((String) ((ValueElement) p1).getValue()).substring(st, to);
        } else if (p1 instanceof ColumnElement && singleRow.get(((ColumnElement) p1).getColumnName()) instanceof String) {
            int st = ((String) singleRow.get(((ColumnElement) p1).getColumnName())).length() + start;
            int to = end + st;
            to = Math.min(((String) singleRow.get(((ColumnElement) p1).getColumnName())).length(), to);
            return ((String) singleRow.get(((ColumnElement) p1).getColumnName())).substring(st, to);
        } else if (p1 instanceof SingleRowFunctionElement){
            return execute(((SingleRowFunctionElement) p1).executeAbout(singleRow), start, end, singleRow);
        } else {
            if (p1 instanceof ColumnElement)
                ExceptionThrowerUtil.isValidRow(singleRow, ((ColumnElement) p1).getColumnName());

            logger.error("Not Valid SUBSTR parameter type!");
            throw new MQLQueryExecuteException("Not Valid SUBSTR parameter type!");
        }
    }

    private Object execute(Object p1, int start, Map<String, Object> singleRow){
        start = start == 0 ? 1 : start;
        if (start > 0) {
            return positiveExecute(p1, start, singleRow);
        } else {
            return negativeExecute(p1, start, singleRow);
        }

    }

    private Object positiveExecute(Object p1, int start, Map<String, Object> singleRow) {
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
            logger.error("Not Valid SUBSTR parameter type!");
            throw new MQLQueryExecuteException("Not Valid SUBSTR parameter type!");
        }
    }

    private Object negativeExecute(Object p1, int start, Map<String, Object> singleRow) {
        if (p1 instanceof String) {
            int st = ((String)p1).length() + start;
            return ((String) p1).substring(st);
        }  else if (p1 instanceof ValueElement && ((ValueElement) p1).getValue() instanceof String) {
            int st = ((String) ((ValueElement) p1).getValue()).length() + start;
            return ((String) ((ValueElement) p1).getValue()).substring(st);
        } else if (p1 instanceof ColumnElement && singleRow.get(((ColumnElement) p1).getColumnName()) instanceof String) {
            int st = ((String) singleRow.get(((ColumnElement) p1).getColumnName())).length() + start;
            return ((String) singleRow.get(((ColumnElement) p1).getColumnName())).substring(st);
        } else if (p1 instanceof SingleRowFunctionElement){
            return execute(((SingleRowFunctionElement) p1).executeAbout(singleRow), start, singleRow);
        } else {
            logger.error("Not Valid SUBSTR parameter type!");
            throw new MQLQueryExecuteException("Not Valid SUBSTR parameter type!");
        }
    }
}
