import com.kcb.mqlService.utils.DateFormatUtil;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;


public class JustTest {

    @Test
    public void dateTest() {
        String target = "2020-01-02";

        assertThat(true, equalTo(DateFormatUtil.isDateFormat(target)));

    }
}
