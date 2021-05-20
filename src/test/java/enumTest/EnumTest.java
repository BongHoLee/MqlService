package enumTest;

import org.junit.Test;

public class EnumTest {

    @Test
    public void enumCurrencyTest() {
        Currency currency = Currency.DOLLAR;
        System.out.println(currency.getValue());

    }
}
