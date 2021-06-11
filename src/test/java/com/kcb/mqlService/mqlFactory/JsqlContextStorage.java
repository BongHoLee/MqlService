package com.kcb.mqlService.mqlFactory;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.TablesNamesFinder;

import java.io.StringReader;
import java.util.List;

public class JsqlContextStorage {
    private Select select;
    private PlainSelect plainSelect;
    private TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
    private CCJSqlParserManager jSqlParserManager = new CCJSqlParserManager();
    private String script;


    public JsqlContextStorage(String script) throws JSQLParserException {
        this.script = script;
        this.select = (Select) jSqlParserManager.parse(new StringReader(script));
        this.plainSelect = (PlainSelect) select.getSelectBody();
    }

    public PlainSelect getPlainSelect() {
        return plainSelect;
    }

    public Select getSelect() {
        return select;
    }

    public List<String> tableNames() {
        return tablesNamesFinder.getTableList(select);
    }
}
