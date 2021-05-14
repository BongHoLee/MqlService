package com.kcb.mqlService.mqlQueryDomain.mqlExpression.relationalOperator;

import java.math.BigDecimal;

public class RelationalOperator {

    // EqualTo (==)
    public static boolean equalTo(Object t1, Object t2) {
        if (t1 instanceof String && t2 instanceof String) {
            return equalTo((String)t1, (String)t2);
        } else if (t1 instanceof Number && t2 instanceof Number) {
            return equalTo((Number)t1, (Number) t2);
        } else {
            throw new RuntimeException("Not Matched Type!!!");
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

    public boolean notEqualTo(Object t1, Object t2) {

        if (t1 instanceof String && t2 instanceof String) {
            return notEqualTo((String)t1, (String)t2);
        } else if (t1 instanceof Number && t2 instanceof Number) {
            return notEqualTo((Number)t1, (Number) t2);
        } else {
            throw new RuntimeException("Not Matched Type!!!");
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

}
