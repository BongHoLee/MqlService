package com.kcb.mqlService.mqlFactory;

import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.ColumnElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.MQLElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.ValueElement;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.FromClause;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.OptionalClause;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.SelectClause;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.AllTableColumns;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItemVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class SelectClauseFactory {
    private static SelectClauseFactory factory;

    private SelectClauseFactory() {}

    public static synchronized SelectClauseFactory getInstance() {
        if (factory == null) {
            factory = new SelectClauseFactory();
        }

        return factory;
    }

    public SelectClause create(SqlContextStorage sqlContextStorage) {
        return new SelectClause(
                elements(sqlContextStorage),
                from(),
                optionalClauses(sqlContextStorage)
        );
    }

    private List<MQLElement> elements(SqlContextStorage sqlContextStorage) {
        List<MQLElement> elements = new ArrayList<>();

        sqlContextStorage.getPlainSelect().getSelectItems().forEach(item -> {
            item.accept(new SelectItemVisitorAdapter() {
                @Override
                public void visit(SelectExpressionItem item) {
                    selectItemVisit(item, elements);
                }

                @Override
                public void visit(AllColumns columns) {
                    SelectExpressionItem allColumns = new SelectExpressionItem(new Column(columns.toString()));
                    selectItemVisit(allColumns, elements);
                }

                @Override
                public void visit(AllTableColumns columns) {
                    SelectExpressionItem allTableColumns = new SelectExpressionItem(new Column(columns.toString()));
                    selectItemVisit(allTableColumns, elements);
                }

            });
        });

        return elements;
    }

    private FromClause from() {
        return new FromClause();
    }

    private List<OptionalClause> optionalClauses(SqlContextStorage sqlContextStorage) {
        return OptionalClauseFactory.getInstance().create(sqlContextStorage);
    }

    private void selectItemVisit(SelectExpressionItem item, List<MQLElement> elements) {

        String alias = item.getAlias() == null ? "" : item.getAlias().getName();
        item.getExpression().accept(expressionVisitor(elements,alias) );
    }


    private ExpressionVisitor expressionVisitor(List<MQLElement> elements, String alias) {
        ExpressionVisitor visitor = new ExpressionVisitorAdapter() {

            @Override
            public void visit(Function function) {
                elements.add(functionElement(function, alias));
            }

            @Override
            public void visit(Column column) {
                elements.add(new ColumnElement(alias, column.getName(true)));
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

    private MQLElement functionElement(Function function, String alias) {
        return MQLFunctionFactory.getInstance().create(function, alias);
    }


}
