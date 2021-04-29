
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class OtherTest {

    @Test
    public void numberTypeTest() {
        Map<String, Object> map = new HashMap<>();


        map.put("int", 1);
        map.put("Integer", new Integer(1));
        map.put("double", 1.0);
        map.put("Double", new Double(1.0));
        map.put("long", 1);
        map.put("Long", new Long(1));
        map.put("LargeNumber", 1111111111111111111L);

        Long test = ((Number) map.get("int")).longValue();

        assertThat(true, equalTo(map.get("int") instanceof Number));
        assertThat(true, equalTo(map.get("Integer") instanceof Number));
        assertThat(true, equalTo(map.get("double") instanceof Number));
        assertThat(true, equalTo(map.get("Double") instanceof Number));
        assertThat(true, equalTo(map.get("long") instanceof Number));
        assertThat(true, equalTo(map.get("Long") instanceof Number));
        assertThat(true, equalTo(map.get("LargeNumber") instanceof Number));

    }

    @Test
    public void bigDecimalCompareTest() {
        BigDecimal i = new BigDecimal("1.1");
        BigDecimal ii = new BigDecimal("2");

        System.out.println(i.add(ii));
    }
}
