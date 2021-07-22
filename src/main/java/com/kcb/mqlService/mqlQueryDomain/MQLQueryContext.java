package com.kcb.mqlService.mqlQueryDomain;

import com.kcb.mqlService.mqlFactory.MQLScriptDocumentFactory;
import com.kcb.mqlService.mqlFactory.SqlContextStorage;
import com.kcb.mqlService.mqlFactory.exception.MQLQueryExecuteException;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.FromClause;
import com.kcb.mqlService.mqlQueryDomain.mqlQueryClause.SelectClause;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MQLQueryContext {
    private static final Logger logger = LoggerFactory.getLogger(MQLQueryContext.class);

    private SelectClause selectClause;
    private Map<String, String> usedTableAliasWithName;

    public MQLQueryContext(SelectClause selectClause, Map<String, String> usedTableAliasWithName) {
        this.selectClause = selectClause;
        this.usedTableAliasWithName = usedTableAliasWithName;
    }
    public List<Map<String, Object>> executeQuery(Map<String, List<Map<String, Object>>> rawDataSources) {
        sqlValidationWithRawDataSources(rawDataSources);
        return selectClause.executeQueryWith(rawDataSources);
    }

    private void sqlValidationWithRawDataSources(Map<String, List<Map<String, Object>>> rawDataSources) {
        if (!rawDataSources.keySet().containsAll(usedTableAliasWithName.keySet())) {
            logger.error("Query ID: {} | The Data Source ID defined in the MQL script is not included in the Data Source | {}", selectClause.getQueryID(), selectClause.getQueryScript());
            throw new MQLQueryExecuteException("Query ID : " + selectClause.getQueryID() + " | The Data Source ID defined in the MQL script is not included in the Data Source. | " + selectClause.getQueryScript());
        }
    }



}
