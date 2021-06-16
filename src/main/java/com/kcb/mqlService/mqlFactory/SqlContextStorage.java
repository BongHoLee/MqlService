package com.kcb.mqlService.mqlFactory;

import com.kcb.mqlService.mqlFactory.exception.MQLQueryNotValidException;
import com.kcb.mqlService.mqlFactory.validator.ClauseValidator;
import com.kcb.mqlService.mqlFactory.validator.FromValidator;
import com.kcb.mqlService.mqlFactory.validator.SelectItemValidator;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.TablesNamesFinder;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SqlContextStorage {
    private Select select;
    private PlainSelect plainSelect;

    private List<ClauseValidator> clauseValidators = Arrays.asList(
            new FromValidator(),
            new SelectItemValidator()
    );

    public SqlContextStorage(String script) {
        try {
            this.select = (Select) CCJSqlParserUtil.parse(new StringReader(script));
            this.plainSelect = (PlainSelect) select.getSelectBody();
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


    public boolean isValid() {
        for (ClauseValidator validator : clauseValidators) {
            if (!validator.isValid(this))
                return false;
        }

        return true;
    }
}
