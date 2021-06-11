package com.kcb.mqlService.mqlFactory;

import org.junit.Test;

public class FactoryExecuteTest {

    @Test
    public void mqlQueryGroupFactoryTest() {
        try {
            MQLQueryGroupFactory.getInstance().create();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
