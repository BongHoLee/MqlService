package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.optionalClause;

import com.kcb.mqlService.mqlFactory.exception.MQLQueryNotValidException;
import com.kcb.mqlService.mqlQueryDomain.MQLQueryContext;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.OptionalClause;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.FromItemVisitorAdapter;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;

import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

public class NoneClause implements OptionalClause {
    @Override
    public MQLDataStorage executeClause(MQLDataStorage mqlDataStorage) {

        try {

            MQLDataSource dataSource = mqlDataStorage.getMqlDataSource();
            String queryScript = mqlDataStorage.getQueryScript();
            Select select = (Select) CCJSqlParserUtil.parse(new StringReader(queryScript));
            PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
            plainSelect.getFromItem().accept(new FromItemVisitorAdapter() {
                @Override
                public void visit(Table table) {
                    String dataSourceId = table.getAlias().getName();
                    mqlDataStorage.setMqlTable(new MQLTable(new HashSet<>(Collections.singletonList(dataSourceId)), dataSource.dataSourceOf(dataSourceId)));

                }
            });

        } catch (RuntimeException | JSQLParserException e) {
            e.printStackTrace();
        }

        return mqlDataStorage;
    }
}
