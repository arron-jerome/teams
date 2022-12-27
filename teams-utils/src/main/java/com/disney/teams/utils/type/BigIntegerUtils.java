package com.disney.teams.utils.type;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * @author arron.zhou
 * @version 1.0.0
 * @date 2022/12/16
 * Modification  History:
 * Date         Author        Version        Description
 * ------------------------------------------------------
 * 2022/12/16       arron.zhou      1.0.0          create
 */
public class BigIntegerUtils {

    public static BigInteger valueOf(Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof Number) {
            return BigInteger.valueOf(((Number) value).longValue());
        } else {
            if (value instanceof Boolean) {
                return Boolean.TRUE.equals(value) ? BigInteger.ONE : BigInteger.ZERO;
            } else if (value instanceof String) {
                if (StringUtils.isBlank((String) value)) {
                    return null;
                }
            }
            return new BigInteger(value.toString());
        }
    }

    public static final BigInteger ZERO = BigInteger.ZERO;
    public static final BigInteger ONE = BigInteger.ONE;

    public static BigInteger[] toArray(String data, String split) {
        if (data == null || data.length() == 0) {
            return null;
        }
        String[] arr = data.split(split);
        int i = 0, len = arr.length;
        BigInteger[] dataArr = new BigInteger[len];
        for (String str : arr) {
            if (StringUtils.isBlank(str)) {
                continue;
            }
            dataArr[i++] = new BigInteger(str);
        }
        return Arrays.copyOf(dataArr, i);
    }

    public static boolean gt(BigInteger source, BigInteger target) {
        return ObjectUtils.gt(source, target);
    }

    public static boolean gte(BigInteger source, BigInteger target) {
        return ObjectUtils.gte(source, target);
    }

    public static boolean lt(BigInteger source, BigInteger target) {
        return ObjectUtils.lt(source, target);
    }

    public static boolean lte(BigInteger source, BigInteger target) {
        return ObjectUtils.lte(source, target);
    }

    public static boolean gt0(BigInteger source) {
        return gt(source, ZERO);
    }

    public static boolean gt1(BigInteger source) {
        return gt(source, ONE);
    }

    public static boolean gte0(BigInteger source) {
        return gte(source, ZERO);
    }

    public static boolean gte1(BigInteger source) {
        return gte(source, ONE);
    }

    public static boolean lt0(BigInteger source) {
        return lt(source, ZERO);
    }

    public static boolean lt1(BigInteger source) {
        return lt(source, ONE);
    }

    public static boolean lte0(BigInteger source) {
        return lte(source, ZERO);
    }

    public static boolean lte1(BigInteger source) {
        return lte(source, ONE);
    }

}
