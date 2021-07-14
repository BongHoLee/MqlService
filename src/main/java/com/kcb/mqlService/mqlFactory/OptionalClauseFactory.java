package com.kcb.mqlService.mqlFactory;

import com.kcb.mqlService.mqlQueryDomain.mqlExpression.*;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.*;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.logicalOperator.ANDOperator;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.logicalOperator.OROperator;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.operatingVisitor.WithColumnTargetOperating;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.operatingVisitor.WithGroupFunctionTargetOperating;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.operatingVisitor.WithSingleRowFunctionTargetOperating;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.operatingVisitor.WithValueTargetOperating;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.relationalOperator.RelationalOperation;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.relationalOperator.RelationalOperator;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.OptionalClause;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.optionalClause.*;
import com.kcb.mqlService.utils.DefinedFunction;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.Join;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OptionalClauseFactory {
    private final String EQUAL_TO = "EQUAL_TO";
    private final String NOT_EQUAL_TO = "NOT_EQUAL_TO";
    private final String MINOR_THAN = "MINOR_THAN";
    private final String MINOR_THAN_EQUAL_TO = "MINOR_THAN_EQUAL_TO";
    private final String GREATER_THAN = "GREATER_THAN";
    private final String GREATER_THAN_EQUAL_TO = "GREATER_THAN_EQUAL_TO";

    private final String DOUBLE = "DOUBLE";
    private final String LONG = "LONG";
    private final String DATE = "DATE";
    private final String STRING = "STRING";
    private final String COLUMN = "COLUMN";
    private final String FUNCTION = "FUNCTION";
    private final String AND = "AND";
    private final String OR = "OR";

    private static OptionalClauseFactory factory;

    private OptionalClauseFactory(){}

    public static synchronized OptionalClauseFactory getInstance() {
        if (factory == null) {
            factory = new OptionalClauseFactory();
        }

        return factory;
    }

    public List<OptionalClause> create(SqlContextStorage sqlContextStorage) {
        List<OptionalClause> optionalClauses = new ArrayList<>();
        if (
                sqlContextStorage.getPlainSelect().getJoins() != null ||
                sqlContextStorage.getPlainSelect().getWhere() != null ||
                sqlContextStorage.getPlainSelect().getGroupBy() != null ||
                sqlContextStorage.getPlainSelect().getHaving() != null
        ) {

            // JOIN and WHERE Clause
            if (sqlContextStorage.getPlainSelect().getJoins() != null || sqlContextStorage.getPlainSelect().getWhere() != null) {
                GeneralConditionClause conditionClause = new GeneralConditionClause();

                if (sqlContextStorage.getPlainSelect().getJoins() != null) {
                    sqlContextStorage.getPlainSelect().getJoins().forEach(join -> {
                        if (join.getOnExpression() != null) {
                            JoinClause joinClause = new JoinClause();
                            joinClause.setOperatingExpression(createOperation(join.getOnExpression()));
                            conditionClause.addCondition(joinClause);
                        }
                    });
                }

                if (sqlContextStorage.getPlainSelect().getWhere() != null) {
                    WhereClause whereClause = new WhereClause();
                    whereClause.setOperatingExpression(createOperation(sqlContextStorage.getPlainSelect().getWhere()));
                    conditionClause.addCondition(whereClause);
                }

                optionalClauses.add(conditionClause);
            }

            // GROUP BY Clause
            if (sqlContextStorage.getPlainSelect().getGroupBy() != null) {
                List<MQLElement> groupByElements = new ArrayList<>();

                sqlContextStorage.getPlainSelect().getGroupBy().getGroupByExpressions().forEach(item -> {
                    item.accept(groupByElementVisitor(groupByElements));
                });

                optionalClauses.add(new GroupByClause(groupByElements));
            }

            if (sqlContextStorage.getPlainSelect().getHaving() != null) {

                HavingClause havingClause = new HavingClause();
                havingClause.setOperatingExpression(createOperation(sqlContextStorage.getPlainSelect().getHaving()));

                optionalClauses.add(havingClause);
            }

        } else {
            optionalClauses.add(new NoneClause());
        }

        return optionalClauses;
    }


    private MQLOperatingExpression createOperation(Expression expression) {
        if (expression != null) {
            if (getExpressionType(expression).equals(AND)) {

                ANDOperator andOperator = new ANDOperator();
                MQLOperatingExpression left = createOperation(((AndExpression) expression).getLeftExpression());
                MQLOperatingExpression right = createOperation(((AndExpression) expression).getRightExpression());
               andOperator.setLeftExpression(left);
               andOperator.setRightExpression(right);
               return andOperator;

            } else if (getExpressionType(expression).equals(OR)) {

                OROperator orOperator = new OROperator();
                MQLOperatingExpression left = createOperation(((OrExpression) expression).getLeftExpression());
                MQLOperatingExpression right = createOperation(((OrExpression) expression).getRightExpression());
                orOperator.setLeftExpression(left);
                orOperator.setRightExpression(right);
                return orOperator;

            } else if (getExpressionType(expression).equals(EQUAL_TO)) {

                RelationalOperation operation = RelationalOperator::equalTo;
                MQLElement standard = createElement(((EqualsTo)expression).getLeftExpression());
                MQLElement compare = createElement(((EqualsTo)expression).getRightExpression());
                return createOperand(standard, compare, operation);

            } else if (getExpressionType(expression).equals(NOT_EQUAL_TO)) {

                RelationalOperation operation = RelationalOperator::notEqualTo;
                MQLElement standard = createElement(((NotEqualsTo)expression).getLeftExpression());
                MQLElement compare = createElement(((NotEqualsTo)expression).getRightExpression());
                return createOperand(standard, compare, operation);

            } else if (getExpressionType(expression).equals(MINOR_THAN)) {

                RelationalOperation operation = RelationalOperator::lessThan;
                MQLElement standard = createElement(((MinorThan)expression).getLeftExpression());
                MQLElement compare = createElement(((MinorThan)expression).getRightExpression());
                return createOperand(standard, compare, operation);

            } else if (getExpressionType(expression).equals(MINOR_THAN_EQUAL_TO)) {

                RelationalOperation operation = RelationalOperator::lessThanEqualTo;
                MQLElement standard = createElement(((MinorThanEquals)expression).getLeftExpression());
                MQLElement compare = createElement(((MinorThanEquals)expression).getRightExpression());
                return createOperand(standard, compare, operation);

            } else if (getExpressionType(expression).equals(GREATER_THAN)) {

                RelationalOperation operation = RelationalOperator::largerThan;
                MQLElement standard = createElement(((GreaterThan)expression).getLeftExpression());
                MQLElement compare = createElement(((GreaterThan)expression).getRightExpression());
                return createOperand(standard, compare, operation);

            } else if (getExpressionType(expression).equals(GREATER_THAN_EQUAL_TO)) {

                RelationalOperation operation = RelationalOperator::largerThanEqualTo;
                MQLElement standard = createElement(((GreaterThanEquals)expression).getLeftExpression());
                MQLElement compare = createElement(((GreaterThanEquals)expression).getRightExpression());
                return createOperand(standard, compare, operation);

            }
        }

        return null;
    }

    private MQLElement createElement(Expression expression) {
        MQLElement element = null;

        if (expression != null) {
            if (getExpressionType(expression).equals(COLUMN)) {
                element = new ColumnElement(((Column)expression).getName(true));
            } else if (getExpressionType(expression).equals(FUNCTION)) {
                element = MQLFunctionFactory.getInstance().create((Function) expression, "");
            } else if (getExpressionType(expression).equals(DOUBLE)) {
                element = new ValueElement(((DoubleValue)expression).getValue());
            } else if (getExpressionType(expression).equals(LONG)) {
                element = new ValueElement(((LongValue)expression).getValue());
            } else if (getExpressionType(expression).equals(STRING)) {
                element = new ValueElement(((StringValue)expression).getValue());
            } else if (getExpressionType(expression).equals(DATE)) {
                element = new ValueElement(((DateValue)expression).getValue().toString());
            }
        }

        return element;
    }

    private MQLOperatingExpression createOperand(MQLElement standard, MQLElement compare, RelationalOperation operation) {
        if (standard instanceof ColumnElement) {
            if (compare instanceof ColumnElement) {

                return new ColumnOperandExpression(
                        (ColumnElement) standard,
                        operation,
                        new WithColumnTargetOperating((ColumnElement) compare)
                );

            } else if (compare instanceof SingleRowFunctionElement) {

                return new ColumnOperandExpression(
                        (ColumnElement) standard,
                        operation,
                        new WithSingleRowFunctionTargetOperating((SingleRowFunctionElement) compare)
                );

            } else if (compare instanceof GroupFunctionElement) {

                return new ColumnOperandExpression(
                        (ColumnElement) standard,
                        operation,
                        new WithGroupFunctionTargetOperating((GroupFunctionElement) compare)
                );

            } else {

                return new ColumnOperandExpression(
                        (ColumnElement) standard,
                        operation,
                        new WithValueTargetOperating((ValueElement) compare)
                );
            }
        } else if (standard instanceof SingleRowFunctionElement) {
            if (compare instanceof ColumnElement) {

                return new SingleRowFunctionOperandExpression(
                        (SingleRowFunctionElement) standard,
                        operation,
                        new WithColumnTargetOperating((ColumnElement) compare)
                );

            } else if (compare instanceof SingleRowFunctionElement) {

                return new SingleRowFunctionOperandExpression(
                        (SingleRowFunctionElement) standard,
                        operation,
                        new WithSingleRowFunctionTargetOperating((SingleRowFunctionElement) compare)
                );

            } else if (compare instanceof GroupFunctionElement) {

                return new SingleRowFunctionOperandExpression(
                        (SingleRowFunctionElement) standard,
                        operation,
                        new WithGroupFunctionTargetOperating((GroupFunctionElement) compare)
                );

            } else {

                return new SingleRowFunctionOperandExpression(
                        (SingleRowFunctionElement) standard,
                        operation,
                        new WithValueTargetOperating((ValueElement) compare)
                );

            }

        } else if (standard instanceof GroupFunctionElement) {

            if (compare instanceof ColumnElement) {

                return new GroupFunctionOperandExpression(
                        (GroupFunctionElement) standard,
                        operation,
                        new WithColumnTargetOperating((ColumnElement) compare)
                );

            } else if (compare instanceof SingleRowFunctionElement) {

                return new GroupFunctionOperandExpression(
                        (GroupFunctionElement) standard,
                        operation,
                        new WithSingleRowFunctionTargetOperating((SingleRowFunctionElement) compare)
                );

            } else if (compare instanceof GroupFunctionElement) {

                return new GroupFunctionOperandExpression(
                        (GroupFunctionElement) standard,
                        operation,
                        new WithGroupFunctionTargetOperating((GroupFunctionElement) compare)
                );

            } else {

                return new GroupFunctionOperandExpression(
                        (GroupFunctionElement) standard,
                        operation,
                        new WithValueTargetOperating((ValueElement) compare)
                );

            }

        } else {

            if (compare instanceof ColumnElement) {

                return new ValueOperandExpression(
                        (ValueElement) standard,
                        operation,
                        new WithColumnTargetOperating((ColumnElement) compare)
                );

            } else if (compare instanceof SingleRowFunctionElement) {

                return new ValueOperandExpression(
                        (ValueElement) standard,
                        operation,
                        new WithSingleRowFunctionTargetOperating((SingleRowFunctionElement) compare)
                );

            } else if (compare instanceof GroupFunctionElement) {

                return new ValueOperandExpression(
                        (ValueElement) standard,
                        operation,
                        new WithGroupFunctionTargetOperating((GroupFunctionElement) compare)
                );

            } else {

                return new ValueOperandExpression(
                        (ValueElement) standard,
                        operation,
                        new WithValueTargetOperating((ValueElement) compare)
                );

            }

        }
    }

    private ExpressionVisitor groupByElementVisitor(List<MQLElement> elements) {
        ExpressionVisitor visitor = new ExpressionVisitorAdapter() {
            @Override
            public void visit(Function function) {
                elements.add(MQLFunctionFactory.getInstance().create(function, ""));
            }

            @Override
            public void visit(Column column) {
                elements.add(new ColumnElement(column.getName(true)));
            }


            @Override
            public void visit(LongValue value) {
                elements.add(new ValueElement(value.getValue()));
            }

            @Override
            public void visit(DoubleValue value) {
                elements.add(new ValueElement(value.getValue()));
            }

            @Override
            public void visit(DateValue value) {
                elements.add(new ValueElement(value.getValue().toString()));
            }

            @Override
            public void visit(StringValue value) {
                elements.add(new ValueElement(value.getValue()));
            }
        };

        return visitor;
    }


    private String getExpressionType(Expression expression) {
        if (expression != null) {
            if (expression instanceof AndExpression) {
                return AND;
            } else if (expression instanceof OrExpression) {
                return OR;
            } else if (expression instanceof EqualsTo) {
                return EQUAL_TO;
            } else if (expression instanceof NotEqualsTo) {
                return NOT_EQUAL_TO;
            } else if (expression instanceof MinorThan) {
                return MINOR_THAN;
            } else if (expression instanceof MinorThanEquals) {
                return MINOR_THAN_EQUAL_TO;
            } else if (expression instanceof GreaterThan) {
                return GREATER_THAN;
            } else if (expression instanceof GreaterThanEquals) {
                return GREATER_THAN_EQUAL_TO;
            } else if (expression instanceof Column) {
                return COLUMN;
            } else if (expression instanceof Function) {
                return FUNCTION;
            } else if (expression instanceof DoubleValue) {
                return DOUBLE;
            } else if (expression instanceof LongValue) {
                return LONG;
            } else if (expression instanceof DateValue) {
                return DATE;
            } else if (expression instanceof StringValue) {
                return STRING;
            }
        }

        return null;
    }
}
