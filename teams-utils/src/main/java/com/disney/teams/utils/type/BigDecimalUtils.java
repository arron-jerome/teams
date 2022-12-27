package com.disney.teams.utils.type;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author arron.zhou
 * @version 1.0.0
 * @date 2022/12/16
 * Modification  History:
 * Date         Author        Version        Description
 * ------------------------------------------------------
 * 2022/12/16       arron.zhou      1.0.0          create
 */
public class BigDecimalUtils {
    public static BigDecimal valueOf(Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        } else if (value instanceof Number) {
            return new BigDecimal(value.toString());
        } else {
            if (value instanceof Boolean) {
                return Boolean.TRUE.equals(value) ? BigDecimal.ONE : BigDecimal.ZERO;
            } else if (value instanceof String) {
                if (StringUtils.isBlank((String) value)) {
                    return null;
                }
            }
            return new BigDecimal(value.toString());
        }
    }

    public static final BigDecimal ZERO = BigDecimal.ZERO;
    public static final BigDecimal ONE = BigDecimal.ONE;

    public static BigDecimal[] toArray(String data, String split) {
        if (data == null || data.length() == 0) {
            return null;
        }
        String[] arr = data.split(split);
        int i = 0, len = arr.length;
        BigDecimal[] dataArr = new BigDecimal[len];
        for (String str : arr) {
            if (StringUtils.isBlank(str)) {
                continue;
            }
            dataArr[i++] = new BigDecimal(str);
        }
        return Arrays.copyOf(dataArr, i);
    }

    public static boolean eq(BigDecimal source, BigDecimal target) {
        if (source == target) {
            return true;
        }
        if (source == null || target == null) {
            return false;
        }
        return source.compareTo(target) == 0;
    }

    public static boolean gt(BigDecimal source, BigDecimal target) {
        return ObjectUtils.gt(source, target);
    }

    public static boolean gte(BigDecimal source, BigDecimal target) {
        return ObjectUtils.gte(source, target);
    }

    public static boolean lt(BigDecimal source, BigDecimal target) {
        return ObjectUtils.lt(source, target);
    }

    public static boolean lte(BigDecimal source, BigDecimal target) {
        return ObjectUtils.lte(source, target);
    }

    public static boolean gt0(BigDecimal source) {
        return gt(source, ZERO);
    }

    public static boolean gt1(BigDecimal source) {
        return gt(source, ONE);
    }

    public static boolean gte0(BigDecimal source) {
        return gte(source, ZERO);
    }

    public static boolean gte1(BigDecimal source) {
        return gte(source, ONE);
    }

    public static boolean lt0(BigDecimal source) {
        return lt(source, ZERO);
    }

    public static boolean lt1(BigDecimal source) {
        return lt(source, ONE);
    }

    public static boolean lte0(BigDecimal source) {
        return lte(source, ZERO);
    }

    public static boolean lte1(BigDecimal source) {
        return lte(source, ONE);
    }

    public static BigDecimal sum(BigDecimal... dests) {
        BigDecimal result = BigDecimal.ZERO;
        if (ArrayUtils.isEmpty(dests)) {
            return result;
        }
        for (BigDecimal value : dests) {
            if (value != null) {
                result = result.add(value);
            }
        }
        return result;
    }

    public static BigDecimal sum(Collection<BigDecimal> dests) {
        BigDecimal result = BigDecimal.ZERO;
        if (CollectionUtils.isEmpty(dests)) {
            return result;
        }
        for (BigDecimal value : dests) {
            if (value != null) {
                result = result.add(value);
            }
        }
        return result;
    }

    public static BigDecimal subtract(BigDecimal src, BigDecimal... dests) {
        BigDecimal result = (src == null ? BigDecimal.ZERO : src);
        if (ArrayUtils.isEmpty(dests)) {
            return result;
        }
        for (BigDecimal value : dests) {
            if (value != null) {
                result = result.subtract(value);
            }
        }
        return result;
    }

    private static boolean isZero(BigDecimal value) {
        return value == null || BigDecimal.ZERO.equals(value);
    }

    public static BigDecimal multiply(BigDecimal src, BigDecimal... dests) {
        if (isZero(src)) {
            return BigDecimal.ZERO;
        }
        BigDecimal result = src;
        if (ArrayUtils.isEmpty(dests)) {
            return result;
        }
        for (BigDecimal value : dests) {
            if (isZero(value)) {
                return BigDecimal.ZERO;
            }
            result = result.multiply(value);
        }
        return result;
    }

    public static BigDecimal divide(BigDecimal src, BigDecimal... dests) {
        if (isZero(src)) {
            return BigDecimal.ZERO;
        }
        BigDecimal result = src;
        if (ArrayUtils.isEmpty(dests)) {
            return result;
        }
        for (BigDecimal value : dests) {
            if (isZero(value)) {
                throw new ArithmeticException("divide by zero");
            }
            result = result.divide(value);
        }
        return result;
    }

    public static boolean betweenIgnoreNull(BigDecimal source, BigDecimal min, BigDecimal max) {
        if (source == null) {
            return min == null;
        }

        if (min != null) {
            if (lt(source, min)) { //比最小小
                return false;
            }
        }

        if (max != null) {
            //比最大大
            return !gt(source, max);
        }

        return true;
    }

    public static BigDecimal ifNull(BigDecimal value, BigDecimal defaultValue) {
        return value == null ? defaultValue : value;
    }

}
