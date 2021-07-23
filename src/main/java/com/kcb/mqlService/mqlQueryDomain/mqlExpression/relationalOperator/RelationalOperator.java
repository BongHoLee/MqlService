package com.kcb.mqlService.mqlQueryDomain.mqlExpression.relationalOperator;

import com.kcb.mqlService.mqlFactory.exception.MQLQueryExecuteException;
import com.kcb.mqlService.mqlQueryDomain.mqlExpression.element.singleRowFunction.LENGTH;
import com.kcb.mqlService.utils.DateFormatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class RelationalOperator {
    private static final Logger logger = LoggerFactory.getLogger(RelationalOperator.class);

    // EqualTo (==)
    public static boolean equalTo(Object t1, Object t2) {


        if (t1 instanceof String && t2 instanceof String) {
            return equalTo((String)t1, (String)t2);
        } else if (t1 instanceof Number && t2 instanceof Number) {
            return equalTo((Number)t1, (Number) t2);
        } else {
            logger.error("Can't Operate.. Not Matched Type : {} == {}", t1 == null ? "null" : t1.toString(), t2 == null ? "null" : t2.toString());
            throw new MQLQueryExecuteException("Not Matched Type!!!");
        }

    }

    private static boolean equalTo(String t1, String t2) {
        return t1.equals(t2);
    }

    private static boolean equalTo(Number t1, Number t2) {
        BigDecimal b1 = new BigDecimal(String.valueOf(t1));
        BigDecimal b2 = new BigDecimal(String.valueOf(t2));

        return b1.compareTo(b2) == 0;
    }

    // NotEqualTo  (!=)

    public static boolean notEqualTo(Object t1, Object t2) {

        if (t1 instanceof String && t2 instanceof String) {
            return notEqualTo((String)t1, (String)t2);
        } else if (t1 instanceof Number && t2 instanceof Number) {
            return notEqualTo((Number)t1, (Number) t2);
        } else {
            logger.error("Can't Operate.. Not Matched Type : {} != {}", t1 == null ? "null" : t1.toString(), t2 == null ? "null" : t2.toString());
            throw new MQLQueryExecuteException("Not Matched Type!!!");
        }

    }

    private static boolean notEqualTo(String t1, String t2) {
        return !(t1.equals(t2));
    }
    private static boolean notEqualTo(Number t1, Number t2) {
        BigDecimal b1 = new BigDecimal(String.valueOf(t1));
        BigDecimal b2 = new BigDecimal(String.valueOf(t2));

        return b1.compareTo(b2) != 0;
    }

    // LargerThan (>=)
    public static boolean largerThan(Object t1, Object t2) {

        if (t1 instanceof String && t2 instanceof String) {
            return largerThan((String)t1, (String)t2);
        } else if (t1 instanceof Number && t2 instanceof Number) {
            return largerThan((Number)t1, (Number) t2);
        } else {
            logger.error("Can't Operate.. Not Matched Type : {} > {}", t1 == null ? "null" : t1.toString(), t2 == null ? "null" : t2.toString());
            throw new MQLQueryExecuteException("Not Matched Type!!!");
        }
    }

    private static boolean largerThan(String t1, String t2) {
        if (DateFormatUtil.isDateFormat(t1) && DateFormatUtil.isDateFormat(t2)) {
            return DateFormatUtil.largerThan(t1, t2);
        } else {
            return t1.compareTo(t2) > 0;
        }
    }

    private static boolean largerThan(Number t1, Number t2) {
        BigDecimal b1 = new BigDecimal(String.valueOf(t1));
        BigDecimal b2 = new BigDecimal(String.valueOf(t2));

        return b1.compareTo(b2) > 0;
    }

    // LargerThanEqualTo (>=)
    public static boolean largerThanEqualTo(Object t1, Object t2) {

        if (t1 instanceof String && t2 instanceof String) {
            return largerThanEqualTo((String)t1, (String)t2);
        } else if (t1 instanceof Number && t2 instanceof Number) {
            return largerThanEqualTo((Number)t1, (Number) t2);
        } else {
            logger.error("Can't Operate.. Not Matched Type : {} >= {}", t1 == null ? "null" : t1.toString(), t2 == null ? "null" : t2.toString());
            throw new MQLQueryExecuteException("Not Matched Type!!!");
        }
    }

    private static boolean largerThanEqualTo(String t1, String t2) {
        if (DateFormatUtil.isDateFormat(t1) && DateFormatUtil.isDateFormat(t2)) {
            return DateFormatUtil.largerThanOrEqual(t1, t2);
        } else {
            return t1.compareTo(t2) >= 0;
        }
    }

    private static boolean largerThanEqualTo(Number t1, Number t2) {
        BigDecimal b1 = new BigDecimal(String.valueOf(t1));
        BigDecimal b2 = new BigDecimal(String.valueOf(t2));

        return b1.compareTo(b2) >= 0;
    }

    // LessThan (<)
    public static boolean lessThan(Object t1, Object t2) {

        if (t1 instanceof String && t2 instanceof String) {
            return lessThan((String)t1, (String)t2);
        } else if (t1 instanceof Number && t2 instanceof Number) {
            return lessThan((Number)t1, (Number) t2);
        } else {
            logger.error("Can't Operate.. Not Matched Type : {} < {}", t1 == null ? "null" : t1.toString(), t2 == null ? "null" : t2.toString());
            throw new MQLQueryExecuteException("Not Matched Type!!!");
        }
    }

    private static boolean lessThan(String t1, String t2) {
        if (DateFormatUtil.isDateFormat(t1) && DateFormatUtil.isDateFormat(t2)) {
            return DateFormatUtil.lessThan(t1, t2);
        } else {
            return t1.compareTo(t2) < 0;
        }
    }

    private static boolean lessThan(Number t1, Number t2) {
        BigDecimal b1 = new BigDecimal(String.valueOf(t1));
        BigDecimal b2 = new BigDecimal(String.valueOf(t2));

        return b1.compareTo(b2) < 0;
    }

    // LessThanEqualTo (<=)
    public static boolean lessThanEqualTo(Object t1, Object t2) {

        if (t1 instanceof String && t2 instanceof String) {
            return lessThanEqualTo((String)t1, (String)t2);
        } else if (t1 instanceof Number && t2 instanceof Number) {
            return lessThanEqualTo((Number)t1, (Number) t2);
        } else {
            logger.error("Can't Operate.. Not Matched Type : {} <= {}", t1 == null ? "null" : t1.toString(), t2 == null ? "null" : t2.toString());
            throw new MQLQueryExecuteException("Not Matched Type!!!");
        }
    }

    private static boolean lessThanEqualTo(String t1, String t2) {
        if (DateFormatUtil.isDateFormat(t1) && DateFormatUtil.isDateFormat(t2)) {
            return DateFormatUtil.lessThanOrEqual(t1, t2);
        } else {
            return t1.compareTo(t2) <= 0;
        }
    }

    private static boolean lessThanEqualTo(Number t1, Number t2) {
        BigDecimal b1 = new BigDecimal(String.valueOf(t1));
        BigDecimal b2 = new BigDecimal(String.valueOf(t2));

        return b1.compareTo(b2) <= 0;
    }

    private void avoidNull(Object t1, Object t2) {
        if (t1 == null || t2 == null) {
            logger.error("Can't Operate.. Not Matched Type : {} <= {}", t1 == null ? "null" : t1.toString(), t2 == null ? "null" : t2.toString());
            throw new MQLQueryExecuteException("Not Matched Type!!!");
        }
    }



}
