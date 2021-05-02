package com.kcb.mqlService.mqlQueryDomain.mqlOperand;

import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class MQLOperandTest {

    @Test
    public void columnOperandInitTest() {
        ColumnOperand operand = new ColumnOperand("A.KEY");

        assertThat("A", equalTo(operand.getDataSourceId()));
    }

    @Test
    public void stringValueOperandTest() {
        String rowValue = "ABC";
        String dateValue = "2020-01-01 23:10:00";

        ValueOperand stringValueOpr = new StringValueOperand(rowValue);
        ValueOperand dateValueOpr = new StringValueOperand(dateValue);

        assertThat(true, equalTo(stringValueOpr.equalTo(rowValue)));
        assertThat(true, equalTo(dateValueOpr.equalTo(dateValue)));
        assertThat(false, equalTo(dateValueOpr.equalTo(rowValue)));

        String rowValue2 = "ABC";
        ValueOperand stringValueOpr2 = new StringValueOperand(rowValue2);

        assertThat(true, equalTo(stringValueOpr.equalTo(stringValueOpr2)));
        assertThat(false, equalTo(stringValueOpr.equalTo(dateValueOpr)));

    }

    @Test
    public void numberValueOperandTest() {
        ValueOperand valueOprString = new NumberValueOperand("1");
        ValueOperand valueOprNumber = new NumberValueOperand(1);

        int intValue = 1;
        long longValue = 1;
        double doubleValue = 1.0;

        assertThat(true, equalTo(valueOprNumber.equalTo(intValue)));
        assertThat(true, equalTo(valueOprNumber.equalTo(longValue)));
        assertThat(true, equalTo(valueOprNumber.equalTo(doubleValue)));

        assertThat(true, equalTo(valueOprString.equalTo(intValue)));
        assertThat(true, equalTo(valueOprString.equalTo(longValue)));
        assertThat(true, equalTo(valueOprString.equalTo(doubleValue)));

        assertThat(true, equalTo(valueOprString.equalTo(new Long(1))));
        assertThat(true, equalTo(valueOprString.equalTo(new Integer(1))));
        assertThat(true, equalTo(valueOprString.equalTo(new Double(1.0))));


        assertThat(true, equalTo(valueOprString.equalTo(valueOprNumber)));



        intValue = 2;
        longValue = 11111111111111111L;
        doubleValue = 2.3;

        assertThat(true, equalTo(valueOprNumber.lessThan(intValue)));
        assertThat(true, equalTo(valueOprNumber.lessThan(longValue)));
        assertThat(true, equalTo(valueOprNumber.lessThan(doubleValue)));
        assertThat(true, equalTo(valueOprNumber.lessThanOrEqualTo(intValue)));
        assertThat(true, equalTo(valueOprNumber.lessThanOrEqualTo(longValue)));
        assertThat(true, equalTo(valueOprNumber.lessThanOrEqualTo(doubleValue)));

    }

}
