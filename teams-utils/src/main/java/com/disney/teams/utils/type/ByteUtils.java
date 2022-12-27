package com.disney.teams.utils.type;

import com.disney.teams.utils.io.CloseUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
public abstract class ByteUtils {

    public static final byte ZERO = 0;
    public static final byte ONE = 1;

    private ByteUtils() {
    }

    public static byte parse(Object value) {
        if (value == null) {
            throw new NullPointerException();
        }
        if (value instanceof Number) {
            return ((Number) value).byteValue();
        } else {
            if (value instanceof Boolean) {
                return (((Boolean) value) ? ONE : ZERO);
            } else {
                String str = value.toString();
                if (StringUtils.isBlank(str)) {
                    return ZERO;
                }
                return Byte.parseByte(str);
            }
        }
    }

    public static byte parse(Object value, byte defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        return parse(value);
    }

    public static Byte valueOf(Object value) {
        if (value == null || "null".equals(value)) {
            return null;
        } else if (value instanceof Byte) {
            return (Byte) value;
        } else if (value instanceof String) {
            if (StringUtils.isBlank((String) value)) {
                return null;
            }
        }
        return parse(value);
    }

    public static Byte[] toArray(String data, String split) {
        if (data == null || data.length() == 0) {
            return null;
        }
        String[] arr = data.split(split);
        int i = 0, len = arr.length;
        Byte[] dataArr = new Byte[len];
        for (String str : arr) {
            if (StringUtils.isBlank(str)) {
                continue;
            }
            dataArr[i++] = Byte.valueOf(str);
        }
        return Arrays.copyOf(dataArr, i);
    }


    public static byte[] inputToByte(InputStream istream) throws IOException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buff = new byte[10240];
            int index = istream.read(buff);
            while (index != -1) {
                outputStream.write(buff, 0, index);
                index = istream.read(buff);
            }
            outputStream.flush();
            return outputStream.toByteArray();
        } finally {
            if (istream != null) {
                CloseUtils.close(istream);
            }
        }

    }

    public static boolean gt(Byte source, byte target) {
        return source == null ? false : source.byteValue() > target;
    }

    public static boolean gt(byte source, Byte target) {
        return target == null ? true : source > target.byteValue();
    }

    public static boolean gte(Byte source, byte target) {
        return source == null ? true : source.byteValue() >= target;
    }

    public static boolean gte(byte source, Byte target) {
        return target == null ? false : source >= target.byteValue();
    }

    public static boolean lt(Byte source, byte target) {
        return source == null ? true : source.byteValue() < target;
    }

    public static boolean lt(byte source, Byte target) {
        return target == null ? false : source < target.byteValue();
    }

    public static boolean lte(Byte source, byte target) {
        return source == null ? true : source.byteValue() <= target;
    }

    public static boolean lte(byte source, Byte target) {
        return target == null ? false : source <= target.byteValue();
    }

    public static boolean gt0(Byte source) {
        return gt(source, ZERO);
    }

    public static boolean gt1(Byte source) {
        return gt(source, ONE);
    }

    public static boolean gte0(Byte source) {
        return gte(source, ZERO);
    }

    public static boolean gte1(Byte source) {
        return gte(source, ONE);
    }

    public static boolean lt0(Byte source) {
        return lt(source, ZERO);
    }

    public static boolean lt1(Byte source) {
        return lt(source, ONE);
    }

    public static boolean lte0(Byte source) {
        return lte(source, ZERO);
    }

    public static boolean lte1(Byte source) {
        return lte(source, ONE);
    }

    public static boolean between(byte source, byte min, byte max) {
        return min <= source && source <= max;
    }

    public static boolean between(Byte source, byte min, byte max) {
        return lte(min, source) && lte(source, max);
    }

    public static boolean between(Byte source, Byte min, Byte max) {
        return ObjectUtils.between(source, min, max);
    }
}
