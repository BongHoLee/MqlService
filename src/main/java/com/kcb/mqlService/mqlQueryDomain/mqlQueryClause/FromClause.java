package com.kcb.mqlService.mqlQueryDomain.mqlQueryClause;



import com.kcb.mqlService.mqlFactory.MQLQueryContextFactory;
import com.kcb.mqlService.mqlFactory.exception.MQLQueryExecuteException;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataSource;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLDataStorage;
import com.kcb.mqlService.mqlQueryDomain.mqlData.MQLTable;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItemVisitorAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FromClause {
    private static final Logger logger = LoggerFactory.getLogger(FromClause.class);
    private MQLDataSource mqlDataSource = new MQLDataSource();

    public MQLDataStorage makeMqlDataSources(String queryID, String queryScript, Map<String, List<Map<String, Object>>> rawDataSources) {
        mqlDataSource.makeFromRawDataSources(rawDataSources);
        selectItemsValidationWithRawDataSources(mqlDataSource.getMqlDataSources(), queryID, queryScript);

        MQLDataStorage mqlDataStorage = new MQLDataStorage(
                queryID,
                queryScript,
                mqlDataSource,
                new MQLTable()
        );

        return mqlDataStorage;
    }

    public MQLDataStorage makeMqlDataSources(Map<String, List<Map<String, Object>>> rawDataSources) {
        return makeMqlDataSources("", "", rawDataSources);
    }


    private void selectItemsValidationWithRawDataSources(Map<String, List<Map<String, Object>>> mqlDataSources, String queryID, String queryScript) {

        Set<String> declaredSelectColumns = getDeclaredColumns(queryScript);
        Set<String> rawDataSourcesAllColumns = mqlDataSources.values().stream().flatMap(eachList -> eachList.get(0).keySet().stream()).collect(Collectors.toSet());
        if (!rawDataSourcesAllColumns.containsAll(declaredSelectColumns)) {
            logger.error("Query ID: {} | The SELECT ITEMS defined in the MQL script is not included in the Data Source | {}", queryID, queryScript);
            throw new MQLQueryExecuteException("Query ID : " + queryID + " | The SELECT ITEMS ID defined in the MQL script is not included in the Data Source. | " + queryScript);
        }
    }

    private Set<String> getDeclaredColumns(String queryScript) {
        Set<String> declaredSelectColumn = new HashSet<>();

        try {
            Select select = (Select) CCJSqlParserUtil.parse(new StringReader(queryScript));
            PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
            plainSelect.getSelectItems().forEach(eachItem ->{
                eachItem.accept(new SelectItemVisitorAdapter(){

                    @Override
                    public void visit(SelectExpressionItem item) {
                        item.getExpression().accept(new ExpressionVisitorAdapter(){

                            @Override
                            public void visit(Column column) {
                                declaredSelectColumn.add(column.getName(true));
                            }
                        });
                    }
                });
            });
        } catch (JSQLParserException e) {
            e.printStackTrace();
        }

        return declaredSelectColumn;
    }







}
