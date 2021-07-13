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
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringReader;
import java.util.*;

public class SqlContextStorage {
    private static final Logger logger = LoggerFactory.getLogger(SqlContextStorage.class);

    private Select select;
    private PlainSelect plainSelect;
    private Map<String, String> usedTableAliasWithName = new HashMap<>();
    private List<String> groupByElementsNames = new ArrayList<>();
    private String queryId;


    private MQLValidator mqlValidator = new MQLValidatorBox();

    public SqlContextStorage(String queryId, String script) {
        try {
            this.queryId = queryId;
            this.select = (Select) CCJSqlParserUtil.parse(new StringReader(script));
            this.plainSelect = (PlainSelect) select.getSelectBody();
            setOtherDatas();
        } catch (JSQLParserException e) {
            logger.error(e.getMessage());
            throw new MQLQueryNotValidException(queryId + "is not valid query");
        }
    }

    public PlainSelect getPlainSelect() {
        return plainSelect;
    }
    public Select getSelect() {
        return select;
    }
    public String getQueryId() {
        return queryId;
    }

    public Map<String, String> getUsedTableAliasWithName() {
        return usedTableAliasWithName;
    }

    public boolean isValid() {
        return mqlValidator.isValid(this);
    }

    private void setOtherDatas() {
        select.getSelectBody().accept(new SelectVisitorAdapter() {

            @Override
            public void visit(PlainSelect plainSelect) {
                if (plainSelect.getFromItem() != null) {
                    plainSelect.getFromItem().accept(new FromItemVisitorAdapter() {

                        @Override
                        public void visit(Table table) {
                            if (table.getAlias() == null) {
                                logger.error("Query Id : {},  MQL Query Must Have Data Source ID in 'FROM' clause : {}", queryId, select.toString());
                                throw new MQLQueryNotValidException(queryId + "is not valid query");
                            }
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
                                    if (table.getAlias() == null) {
                                        logger.error("Query Id : {}, MQL Query Must Have 'FROM' clause : {}", queryId, select.toString());
                                        throw new MQLQueryNotValidException(queryId + "is not valid query");
                                    }
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
