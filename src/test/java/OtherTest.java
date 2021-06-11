
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.MQLElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.SingleRowFunctionElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.ValueElement;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.singleRowFunction.LENGTH;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.relationalOperator.RelationalOperation;
import com.kcb.mqlService.testData.TestDataFactory;
import org.junit.Test;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;

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

    @Test
    public void matchedColumnTest() {
        Map<String, Object> map1 = new HashMap<>();
        map1.put("A", "A");
        map1.put("B", "C");

        Map<String, Object> map2 = new HashMap<>();
        map2.put("A", "A");
        map2.put("C", "C");

        Set<String> matchedColumnSet = new HashSet<>(map1.keySet());
        matchedColumnSet.retainAll(map2.keySet());

        assertThat(matchedColumnSet.size(), is(1));
        assertThat(matchedColumnSet, equalTo(new HashSet<>(Arrays.asList("A"))));

    }

    @Test
    public void lengthTest() {
        SingleRowFunctionElement length = new LENGTH(
                new ValueElement(1000.0)
        );
        System.out.println(length.executeAbout(new HashMap<>()));
    }

    @Test
    public void substrTest() {
        int start = 2;
        int end = 4;
        String str = "Ana Trujillo Emparedados y helados";


        int st =  start - 1;
        int to = end + st;
        System.out.println(str.substring(st, to));
    }

    @Test
    public void bigdecimalTest() {
        Map<String, Object> map = new HashMap<>();
        BigDecimal inte = new BigDecimal(11);

        map.put("int", 1L);
        map.put("double", 1.0);
        map.put("long", inte.longValue());

        System.out.println(map.get("long") instanceof Integer);
        System.out.println(map.get("long") instanceof Long);
        System.out.println(map.get("int") instanceof Integer);
        System.out.println(map.get("int") instanceof Double);
        System.out.println(map.get("double") instanceof Integer);
        System.out.println(map.get("double") instanceof Double);
    }

    @Test
    public void subListCopyTest() {
        List<Integer> origin = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> tmp = new ArrayList<>(origin.subList(0, 3));
        System.out.println(tmp);
    }

    @Test
    public void getResourcePath() {

    }

}
