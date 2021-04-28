package com.kcb.mqlService.mqlQueryDomain.mqlOperand;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class MQLOperandTest {

    @Test
    public void columnOperandInitTest() {
        ColumnOperand operand = new ColumnOperand("A.KEY");

        assertThat("A", equalTo(operand.getDataSourceId()));

    }
}
