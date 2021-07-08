package com.kcb.mqlService.mqlFactory.validator;

import com.kcb.mqlService.mqlFactory.SqlContextStorage;
import com.kcb.mqlService.mqlFactory.exception.MQLQueryNotValidException;
import com.kcb.mqlService.utils.DefinedFunction;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.*;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class SyntaxValidator implements MQLValidator{
    private static final Logger logger = LoggerFactory.getLogger(SyntaxValidator.class);


    @Override
    public boolean isValid(SqlContextStorage sqlContextStorage) {
        PlainSelect plainSelect = sqlContextStorage.getPlainSelect();

        if (plainSelect.getFromItem() == null) {
            logger.error("Query ID : {}, MQL Query Must Have 'FROM' clause : {}", sqlContextStorage.getQueryId(), sqlContextStorage.getSelect().toString());
            throw new MQLQueryNotValidException(sqlContextStorage.getQueryId() + "is not valid query");
        }

        // group by 없을 때
        if (plainSelect.getGroupBy() == null) {
            // having 존재 시
            if ( plainSelect.getHaving() != null) {
                logger.error("Query ID : {}, 'GROUP BY' clause must exist to use 'HAVING' clause : {}", sqlContextStorage.getQueryId(), sqlContextStorage.getSelect().toString());
                throw new MQLQueryNotValidException(sqlContextStorage.getQueryId() + "is not valid query");
            }


            boolean[] boolTable = new boolean[2];

            // Select Item에 그룹함수, 컬럼 함께 존재 시
            for(SelectItem selectItem : plainSelect.getSelectItems()) {

                selectItem.accept(new SelectItemVisitorAdapter(){

                    @Override
                    public void visit(SelectExpressionItem item) {
                        if (item.getExpression() instanceof Function) {
                            if (DefinedFunction.GROUP_FUNCTION.getDefinedFunctionList().contains(((Function) item.getExpression()).getName())) {
                                if (boolTable[0]) {
                                    logger.error("Query ID : {}, this Query must have 'GROUP BY' clause : {}", sqlContextStorage.getQueryId(), sqlContextStorage.getSelect().toString());
                                    throw new MQLQueryNotValidException(sqlContextStorage.getQueryId() + "is not valid query");
                                }
                                boolTable[1] = true;
                            }
                        }else if (item.getExpression() instanceof Column) {
                            if (boolTable[1]) {
                                logger.error("Query ID : {}, this Query must have 'GROUP BY' clause : {}", sqlContextStorage.getQueryId(), sqlContextStorage.getSelect().toString());
                                throw new MQLQueryNotValidException(sqlContextStorage.getQueryId() + "is not valid query");
                            }
                            boolTable[0] = true;
                        }
                    }

                    @Override
                    public void visit(AllColumns columns) {
                        if (boolTable[1]) {
                            logger.error("Query ID : {}, this Query must have 'GROUP BY' clause : {}", sqlContextStorage.getQueryId(), sqlContextStorage.getSelect().toString());
                            throw new MQLQueryNotValidException(sqlContextStorage.getQueryId() + "is not valid query");
                        }
                        boolTable[0] = true;
                    }

                    @Override
                    public void visit(AllTableColumns columns) {
                        if (boolTable[1]) {
                            logger.error("Query ID : {}, this Query must have 'GROUP BY' clause : {}", sqlContextStorage.getQueryId(), sqlContextStorage.getSelect().toString());
                            throw new MQLQueryNotValidException(sqlContextStorage.getQueryId() + "is not valid query");
                        }
                        boolTable[0] = true;
                    }

                });
            }
        }

        return true;
    }
}
