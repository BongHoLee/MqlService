package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.optionalClause;

import com.kcb.mqlService.mqlFactory.exception.MQLQueryExecuteException;
import com.kcb.mqlService.mqlFactory.exception.MQLQueryNotValidException;
import com.kcb.mqlService.mqlFactory.validator.FromMatchJoinValidator;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

public class NoneClause implements OptionalClause {
    private static final Logger logger = LoggerFactory.getLogger(NoneClause.class);

    @Override
    public MQLDataStorage executeClause(MQLDataStorage mqlDataStorage) {

        try {
            boolean flag = false;
            MQLDataSource dataSource = mqlDataStorage.getMqlDataSource();
            String queryScript = mqlDataStorage.getQueryScript();
            Select select = (Select) CCJSqlParserUtil.parse(new StringReader(queryScript));
            PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
            if (plainSelect.getFromItem() != null) {
                flag = true;
                plainSelect.getFromItem().accept(new FromItemVisitorAdapter() {
                    @Override
                    public void visit(Table table) {
                        String dataSourceId = table.getAlias().getName();
                        mqlDataStorage.setMqlTable(new MQLTable(new HashSet<>(Collections.singletonList(dataSourceId)), dataSource.dataSourceOf(dataSourceId)));
                    }
                });
            }

            if (!flag) {
                if (dataSource.getMqlDataSources().keySet().size() > 1) {
                    logger.error("Query ID: {}, has too many data source check out query and data source is matched", mqlDataStorage.getQueryID());
                    throw new MQLQueryExecuteException("MQL Execution Exception. query ID : " + mqlDataStorage.getQueryID());
                }

                for (String key : dataSource.getMqlDataSources().keySet()) {
                    mqlDataStorage.setMqlTable(new MQLTable(new HashSet<>(Collections.singletonList(key)), dataSource.dataSourceOf(key)));
                }
            }
        } catch (JSQLParserException e) {
            e.printStackTrace();
        }

        return mqlDataStorage;
    }
}
