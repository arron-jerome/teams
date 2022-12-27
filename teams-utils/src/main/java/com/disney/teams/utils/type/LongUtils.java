package com.disney.teams.utils.type;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author arron.zhou
 * @version 1.0.0
 * @date 2022/12/16
 * Description:
 * Modification  History:
 * Date         Author        Version        Description
 * ------------------------------------------------------
 * 2022/12/16       arron.zhou      1.0.0          create
 */
public abstract class LongUtils {

    public static final long ZERO = 0L;
    public static final long ONE = 1L;

    private LongUtils() {
    }

    public static long parse(Object value) {
        if (value == null) {
            throw new NullPointerException();
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        } else if (value instanceof Date) {
            return ((Date) value).getTime();
        } else {
            if (value instanceof Boolean) {
                return ((Boolean) value) ? ONE : ZERO;
            } else {
                String str = value.toString();
                if (StringUtils.isBlank(str)) {
                    return ZERO;
                }
                return Long.parseLong(str);
            }
        }
    }

    public static long parse(Object value, long defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        return parse(value);
    }

    public static long ifNull(Long value, long defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static Long valueOf(Object value) {
        if (value == null || "null".equals(value)) {
            return null;
        } else if (value instanceof Long) {
            return (Long) value;
        } else if (value instanceof String) {
            if (StringUtils.isBlank((String) value)) {
                return null;
            }
        }
        return parse(value);
    }

    public static Long[] toArray(String data, String split) {
        List<Long> list = toList(data, split);
        return list == null ? null : list.toArray(new Long[0]);
    }

    public static List<Long> toList(String data, String split) {
        if (data == null || data.length() == 0) {
            return null;
        }
        String[] arr = data.split(split);
        int len = arr.length;
        List<Long> list = new ArrayList<>(len);
        for (String str : arr) {
            if (StringUtils.isBlank(str)) {
                continue;
            }
            list.add(Long.valueOf(str));
        }
        return list;
    }

    public static boolean gt(Long source, long target) {
        return source != null && source > target;
    }

    public static boolean gt(long source, Long target) {
        return target == null || source > target;
    }

    public static boolean gte(Long source, long target) {
        return source == null || source >= target;
    }

    public static boolean gte(long source, Long target) {
        return target != null && source >= target;
    }

    public static boolean lt(Long source, long target) {
        return source == null || source < target;
    }

    public static boolean lt(long source, Long target) {
        return target != null && source < target;
    }

    public static boolean lte(Long source, long target) {
        return source == null || source <= target;
    }

    public static boolean lte(long source, Long target) {
        return target != null && source <= target;
    }

    public static boolean gt0(Long source) {
        return gt(source, ZERO);
    }

    public static boolean gt1(Long source) {
        return gt(source, ONE);
    }

    public static boolean gte0(Long source) {
        return gte(source, ZERO);
    }

    public static boolean gte1(Long source) {
        return gte(source, ONE);
    }

    public static boolean lt0(Long source) {
        return lt(source, ZERO);
    }

    public static boolean lt1(Long source) {
        return lt(source, ONE);
    }

    public static boolean lte0(Long source) {
        return lte(source, ZERO);
    }

    public static boolean lte1(Long source) {
        return lte(source, ONE);
    }

    public static boolean between(long source, long min, long max) {
        return min <= source && source <= max;
    }

    public static boolean between(Long source, long min, long max) {
        return lte(min, source) && lte(source, max);
    }

    public static boolean between(Long source, Long min, Long max) {
        return ObjectUtils.between(source, min, max);
    }

    public static Long ltToNull(Long source, long value) {
        if (source == null) {
            return null;
        }
        return source < value ? null : source;
    }
}
