package com.kcb.mqlService.mqlFactory;

import com.kcb.mqlService.mqlFactory.exception.MQLQueryNotValidException;
import com.kcb.mqlService.mqlFactory.validator.*;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.FromItemVisitorAdapter;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectVisitorAdapter;

import java.io.StringReader;
import java.util.*;

public class SqlContextStorage {
    private Select select;
    private PlainSelect plainSelect;
    private Map<String, String> usedTableAliasWithName = new HashMap<>();
    private List<String> groupByElementsNames = new ArrayList<>();

    private List<MQLValidator> MQLValidators = Arrays.asList(
            new SyntaxValidator(),
            new FromMatchJoinValidator(),
            new ItemsOfGeneralClauseValidator(),
            new ItemsOfRelatedGroupByClauseValidator()
    );

    public SqlContextStorage(String script) {
        try {
            this.select = (Select) CCJSqlParserUtil.parse(new StringReader(script));
            this.plainSelect = (PlainSelect) select.getSelectBody();
            setOtherDatas();
        } catch (JSQLParserException e) {
            e.printStackTrace();
            throw new MQLQueryNotValidException();
        }
    }

    public PlainSelect getPlainSelect() {
        return plainSelect;
    }
    public Select getSelect() {
        return select;
    }

    public Map<String, String> getUsedTableAliasWithName() {
        return usedTableAliasWithName;
    }

    public boolean isValid() {
        for (MQLValidator validator : MQLValidators) {
            if (!validator.isValid(this))
                return false;
        }

        return true;
    }

    private void setOtherDatas() {
        select.getSelectBody().accept(new SelectVisitorAdapter() {

            @Override
            public void visit(PlainSelect plainSelect) {
                if (plainSelect.getFromItem() != null) {
                    plainSelect.getFromItem().accept(new FromItemVisitorAdapter() {
                        @Override
                        public void visit(Table table) {
                            usedTableAliasWithName.put(table.getAlias().getName(), table.getFullyQualifiedName());
                        }
                    });
                }

                if (plainSelect.getJoins() != null) {
                    plainSelect.getJoins().forEach(eachJoin -> {
                        if (eachJoin.getRightItem() != null) {
                            eachJoin.getRightItem().accept(new FromItemVisitorAdapter() {
                                @Override
                                public void visit(Table table) {
                                    usedTableAliasWithName.put(table.getAlias().getName(), table.getFullyQualifiedName());
                                }
                            });
                        }
                    });
                }

                if (plainSelect.getGroupBy() != null) {
                    plainSelect.getGroupBy().getGroupByExpressions().forEach(eachElement -> {
                        groupByElementsNames.add(eachElement.toString());
                    });
                }
            }
        });
    }

    public List<String> getGroupByElementsNames() {
        return groupByElementsNames;
    }
}
