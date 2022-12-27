package com.disney.teams.utils.type;

import com.disney.teams.utils.AssertUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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
public abstract class NumberUtils {

    private static final BigInteger LONG_MIN = BigInteger.valueOf(-9223372036854775808L);
    private static final BigInteger LONG_MAX = BigInteger.valueOf(9223372036854775807L);
    public static final Set<Class<?>> STANDARD_NUMBER_TYPES;

    private NumberUtils() {
    }

    public static <T extends Number> T convertNumberToTargetClass(Number number, Class<T> targetClass) throws IllegalArgumentException {
        AssertUtils.notNull(number, "Number must not be null");
        AssertUtils.notNull(targetClass, "Target class must not be null");
        if (targetClass.isInstance(number)) {
            return (T) number;
        } else {
            long bigInt1;
            if (Byte.class == targetClass) {
                bigInt1 = number.longValue();
                if (bigInt1 < -128L || bigInt1 > 127L) {
                    raiseOverflowException(number, targetClass);
                }
                return (T) Byte.valueOf(number.byteValue());
            } else if (Short.class == targetClass) {
                bigInt1 = number.longValue();
                if (bigInt1 < -32768L || bigInt1 > 32767L) {
                    raiseOverflowException(number, targetClass);
                }
                return (T) Short.valueOf(number.shortValue());
            } else if (Integer.class == targetClass) {
                bigInt1 = number.longValue();
                if (bigInt1 < -2147483648L || bigInt1 > 2147483647L) {
                    raiseOverflowException(number, targetClass);
                }
                return (T) Integer.valueOf(number.intValue());
            } else if (Long.class == targetClass) {
                BigInteger bigInt = null;
                if (number instanceof BigInteger) {
                    bigInt = (BigInteger) number;
                } else if (number instanceof BigDecimal) {
                    bigInt = ((BigDecimal) number).toBigInteger();
                }
                if (bigInt != null && (bigInt.compareTo(LONG_MIN) < 0 || bigInt.compareTo(LONG_MAX) > 0)) {
                    raiseOverflowException(number, targetClass);
                }
                return (T) Long.valueOf(number.longValue());
            } else if (BigInteger.class == targetClass) {
                return number instanceof BigDecimal ? (T) (((BigDecimal) number).toBigInteger()) : (T) (BigInteger.valueOf(number.longValue()));
            } else if (Float.class == targetClass) {
                return (T) Float.valueOf(number.floatValue());
            } else if (Double.class == targetClass) {
                return (T) Double.valueOf(number.doubleValue());
            } else if (BigDecimal.class == targetClass) {
                return (T) (new BigDecimal(number.toString()));
            } else {
                throw new IllegalArgumentException("Could not convert number [" + number + "] of type [" + number.getClass().getName() + "] to unsupported target class [" + targetClass.getName() + "]");
            }
        }
    }

    private static void raiseOverflowException(Number number, Class<?> targetClass) {
        throw new IllegalArgumentException("Could not convert number [" + number + "] of type [" + number.getClass().getName() + "] to target class [" + targetClass.getName() + "]: overflow");
    }

    public static <T extends Number> T parseNumber(String text, Class<T> targetClass) {
        AssertUtils.notNull(text, "Text must not be null");
        AssertUtils.notNull(targetClass, "Target class must not be null");
        String trimmed = StringUtils.trimAllWhitespace(text);
        if (Byte.class == targetClass) {
            return isHexNumber(trimmed) ? (T) Byte.decode(trimmed) : (T) Byte.valueOf(trimmed);
        } else if (Short.class == targetClass) {
            return isHexNumber(trimmed) ? (T) Short.decode(trimmed) : (T) Short.valueOf(trimmed);
        } else if (Integer.class == targetClass) {
            return isHexNumber(trimmed) ? (T) Integer.decode(trimmed) : (T) Integer.valueOf(trimmed);
        } else if (Long.class == targetClass) {
            return isHexNumber(trimmed) ? (T) Long.decode(trimmed) : (T) Long.valueOf(trimmed);
        } else if (BigInteger.class == targetClass) {
            return isHexNumber(trimmed) ? (T) decodeBigInteger(trimmed) : (T) new BigInteger(trimmed);
        } else if (Float.class == targetClass) {
            return (T) Float.valueOf(trimmed);
        } else if (Double.class == targetClass) {
            return (T) Double.valueOf(trimmed);
        } else if (BigDecimal.class != targetClass && Number.class != targetClass) {
            throw new IllegalArgumentException("Cannot convert String [" + text + "] to target class [" + targetClass.getName() + "]");
        } else {
            return (T) new BigDecimal(trimmed);
        }
    }

    public static <T extends Number> T parseNumber(String text, Class<T> targetClass, NumberFormat numberFormat) {
        if (numberFormat == null) {
            return parseNumber(text, targetClass);
        } else {
            AssertUtils.notNull(text, "Text must not be null");
            AssertUtils.notNull(targetClass, "Target class must not be null");
            DecimalFormat decimalFormat = null;
            boolean resetBigDecimal = false;
            if (numberFormat instanceof DecimalFormat) {
                decimalFormat = (DecimalFormat) numberFormat;
                if (BigDecimal.class == targetClass && !decimalFormat.isParseBigDecimal()) {
                    decimalFormat.setParseBigDecimal(true);
                    resetBigDecimal = true;
                }
            }

            Number var6;
            try {
                Number ex = numberFormat.parse(StringUtils.trimAllWhitespace(text));
                var6 = convertNumberToTargetClass(ex, targetClass);
            } catch (ParseException var10) {
                throw new IllegalArgumentException("Could not parse number: " + var10.getMessage());
            } finally {
                if (resetBigDecimal) {
                    decimalFormat.setParseBigDecimal(false);
                }

            }

            return (T) var6;
        }
    }

    private static boolean isHexNumber(String value) {
        int index = value.startsWith("-") ? 1 : 0;
        return value.startsWith("0x", index) || value.startsWith("0X", index) || value.startsWith("#", index);
    }

    private static BigInteger decodeBigInteger(String value) {
        byte radix = 10;
        int index = 0;
        boolean negative = false;
        if (value.startsWith("-")) {
            negative = true;
            ++index;
        }

        if (!value.startsWith("0x", index) && !value.startsWith("0X", index)) {
            if (value.startsWith("#", index)) {
                ++index;
                radix = 16;
            } else if (value.startsWith("0", index) && value.length() > 1 + index) {
                ++index;
                radix = 8;
            }
        } else {
            index += 2;
            radix = 16;
        }

        BigInteger result = new BigInteger(value.substring(index), radix);
        return negative ? result.negate() : result;
    }

    public static String toStringNull(Number number) {
        return number == null ? "null" : number.toString();
    }

    public static String toStringZero(Number number) {
        return number == null ? "0" : number.toString();
    }

    static {
        HashSet numberTypes = new HashSet(8);
        numberTypes.add(Byte.class);
        numberTypes.add(Short.class);
        numberTypes.add(Integer.class);
        numberTypes.add(Long.class);
        numberTypes.add(BigInteger.class);
        numberTypes.add(Float.class);
        numberTypes.add(Double.class);
        numberTypes.add(BigDecimal.class);
        STANDARD_NUMBER_TYPES = Collections.unmodifiableSet(numberTypes);
    }
}

