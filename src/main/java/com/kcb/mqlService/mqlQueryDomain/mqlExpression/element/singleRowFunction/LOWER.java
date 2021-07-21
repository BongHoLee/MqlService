package com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.singleRowFunction;

import com.kcb.mqlService.mqlFactory.exception.MQLQueryExecuteException;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.*;
import com.kcb.mqlService.utils.ExceptionThrowerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class LOWER extends SingleRowFunctionElement {
    private static final Logger logger = LoggerFactory.getLogger(LOWER.class);

    private String expression = "";
    private boolean hasAlias;
    private String alias = "";

    public LOWER(String alias) {
        super();
        this.alias = alias;
        setHasAlias();
    }

    public LOWER(String alias, MQLElement ... parameters) {
        super(parameters);
        this.alias = alias;
        makeExpression(Arrays.asList(parameters));
        setHasAlias();

    }

    public LOWER(MQLElement ... parameters) {
        this("", parameters);
    }

    public LOWER(String alias, List<MQLElement> parameters) {
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
        expression = "LOWER(";

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
        return execute(parameters.get(0), singleRow);
    }

    private Object execute(Object param, Map<String, Object> singleRow) {
        if (param instanceof String) {
            return ((String) param).toLowerCase();
        } else if (param instanceof ValueElement && ((ValueElement) param).getValue() instanceof String) {
            return ((String) ((ValueElement) param).getValue()).toLowerCase();
        } else if (param instanceof ColumnElement && singleRow.get(((ColumnElement) param).getColumnName()) instanceof String) {
            ExceptionThrowerUtil.isValidRow(singleRow,((ColumnElement) param).getColumnName());
            return ((String) singleRow.get(((ColumnElement) param).getColumnName())).toLowerCase();
        } else if (param instanceof SingleRowFunctionElement){
            return execute(((SingleRowFunctionElement) param).executeAbout(singleRow), singleRow);
        } else {
            logger.error("MQL Execution Exception. Lower Parameter Must String!");
            throw new MQLQueryExecuteException("LOWER Parameter must String");
        }
    }
}