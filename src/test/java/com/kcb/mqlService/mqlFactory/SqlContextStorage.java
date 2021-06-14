package com.kcb.mqlService.mqlFactory;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.TablesNamesFinder;

import java.io.StringReader;
import java.util.List;

public class SqlContextStorage {
    private Select select;
    private PlainSelect plainSelect;
    private TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();

    public SqlContextStorage(String script) throws JSQLParserException {
        this.select = (Select) CCJSqlParserUtil.parse(new StringReader(script));
        this.plainSelect = (PlainSelect) select.getSelectBody();
    }

    public PlainSelect getPlainSelect() {
        return plainSelect;
    }
    public List<String> tableNames() {
        return tablesNamesFinder.getTableList(select);
    }

    public boolean isValid() {
        return false;
    }
}
