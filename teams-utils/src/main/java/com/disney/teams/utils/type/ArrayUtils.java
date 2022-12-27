package com.disney.teams.utils.type;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author arron.zhou
 * @version 1.0.0
 * @date 2022/12/16
 * Modification  History:
 * Date         Author        Version        Description
 * ------------------------------------------------------
 * 2022/12/16       arron.zhou      1.0.0          create
 */
public class ArrayUtils {

    public static int length(boolean[] array) {
        return array == null ? 0 : array.length;
    }

    public static int length(byte[] array) {
        return array == null ? 0 : array.length;
    }

    public static int length(short[] array) {
        return array == null ? 0 : array.length;
    }

    public static int length(int[] array) {
        return array == null ? 0 : array.length;
    }

    public static int length(long[] array) {
        return array == null ? 0 : array.length;
    }

    public static int length(float[] array) {
        return array == null ? 0 : array.length;
    }

    public static int length(double[] array) {
        return array == null ? 0 : array.length;
    }

    public static int length(char[] array) {
        return array == null ? 0 : array.length;
    }

    public static int length(Object[] array) {
        return array == null ? 0 : array.length;
    }

    public static boolean isEmpty(boolean[] values) {
        return values == null || values.length < 1;
    }

    public static boolean isNotEmpty(boolean[] values) {
        return !isEmpty(values);
    }

    public static boolean isEmpty(byte[] values) {
        return values == null || values.length < 1;
    }

    public static boolean isNotEmpty(byte[] values) {
        return !isEmpty(values);
    }

    public static boolean isEmpty(short[] values) {
        return values == null || values.length < 1;
    }

    public static boolean isNotEmpty(short[] values) {
        return !isEmpty(values);
    }

    public static boolean isEmpty(int[] values) {
        return values == null || values.length < 1;
    }

    public static boolean isNotEmpty(int[] values) {
        return !isEmpty(values);
    }

    public static boolean isEmpty(long[] values) {
        return values == null || values.length < 1;
    }

    public static boolean isNotEmpty(long[] values) {
        return !isEmpty(values);
    }

    public static boolean isEmpty(char[] values) {
        return values == null || values.length < 1;
    }

    public static boolean isNotEmpty(char[] values) {
        return !isEmpty(values);
    }

    public static boolean isEmpty(float[] values) {
        return values == null || values.length < 1;
    }

    public static boolean isNotEmpty(float[] values) {
        return !isEmpty(values);
    }

    public static boolean isEmpty(double[] values) {
        return values == null || values.length < 1;
    }

    public static boolean isNotEmpty(double[] values) {
        return !isEmpty(values);
    }

    public static <T> boolean isEmpty(T[] values) {
        return values == null || values.length < 1;
    }

    public static <T> boolean isNotEmpty(T[] values) {
        return !isEmpty(values);
    }

    public static <T> T[] clone(T[] source) {
        if (source == null) {
            return null;
        }
        return Arrays.copyOf(source, source.length);
    }

    public static <T> T[] merge(T[] source, T[] target) {
        if (source == null) {
            return target;
        }

        if (target == null) {
            return source;
        }

        int slen = source.length;
        int tlen = target.length;
        T[] newArray = Arrays.copyOf(source, slen + tlen);
        for (int i = slen, j = 0, len = slen + tlen; i < len; ++i, ++j) {
            newArray[i] = target[j];
        }
        return newArray;
    }

    public static <T> T get(T[] array, int i) {
        int len = length(array);
        return i < len ? array[i] : null;
    }

    public static <T> T getLast(T[] array) {
        if (isEmpty(array)) {
            return null;
        }
        return array[array.length - 1];
    }

    public static <T> boolean in(T value, T[] values) {
        if (isEmpty(values)) {
            return false;
        }
        for (T v : values) {
            if (ObjectUtils.eq(value, v)) {
                return true;
            }
        }
        return false;
    }

    public static <T> boolean notIn(T value, T[] values) {
        return !in(value, values);
    }

    public static <T> T[] toArray(Collection<T> collection) {
        if (CollectionUtils.isEmpty(collection)) {
            return (T[]) new Object[0];
        }

        return (T[]) collection.toArray();
    }

    public static <T> T[] toArray(Collection<T> collection, Class<T> clazz) {
        if (CollectionUtils.isEmpty(collection)) {
            return (T[]) Array.newInstance(clazz, 0);
        }
        T[] array = (T[]) Array.newInstance(clazz, collection.size());
        return collection.toArray(array);
    }

    public static <T> List<T> toList(T... values) {
        if (isEmpty(values)) {
            return new ArrayList<>();
        }
        List<T> list = new ArrayList<>(values.length);
        for (T value : values) {
            list.add(value);
        }
        return list;
    }

    public static <T> String toString(T[] values) {
        return toString(values, "");
    }

    public static <T> String toString(T[] values, char gap) {
        if (values == null || values.length == 0) {
            return "";
        }
        if (values.length == 1) {
            return String.valueOf(values[0]);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(values[0]);
        for (int i = 1, len = values.length; i < len; ++i) {
            sb.append(gap).append(values[i]);
        }
        return sb.toString();
    }

    public static <T> String toString(T[] values, String gap) {
        if (values == null || values.length == 0) {
            return "";
        }
        if (values.length == 1) {
            return String.valueOf(values[0]);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(values[0]);
        for (int i = 1, len = values.length; i < len; ++i) {
            sb.append(gap).append(values[i]);
        }
        return sb.toString();
    }

    public static Object newInstance(GenericArrayType targetComponentType, int len) {
        Type ctype = targetComponentType.getGenericComponentType();
        int size = 1;
        while (ctype instanceof GenericArrayType) {
            ctype = ((GenericArrayType) ctype).getGenericComponentType();
            ++size;
        }
        int[] sizes = new int[size];
        sizes[0] = len;
        Object target;
        if (ctype instanceof Class) {
            target = Array.newInstance((Class<?>) ctype, sizes);
        } else if (ctype instanceof ParameterizedType) {
            target = Array.newInstance((Class<?>) ((ParameterizedType) ctype).getRawType(), sizes);
        } else if (ctype instanceof WildcardType) {
            Type[] bounds = ((WildcardType) ctype).getUpperBounds();
            target = Array.newInstance((Class<?>) bounds[0], sizes);
        } else if (ctype instanceof TypeVariable) {
            Type[] bounds = ((TypeVariable<?>) ctype).getBounds();
            target = Array.newInstance((Class<?>) bounds[0], sizes);
        } else {
            throw new UnsupportedOperationException(ctype + " is not support");
        }
        return target;
    }

    public static <T> T convert(Object src, ParameterizedType targetComponetType) {
        Class<?> rawType = (Class<?>) targetComponetType.getRawType();
        int len = Array.getLength(src);
        Object target = Array.newInstance(rawType, len);
        for (int i = 0; i < len; ++i) {
            Array.set(target, i, TypeUtils.convert(Array.get(src, i), targetComponetType));
        }
        return (T) target;
    }

    public static <T> T convert(Object src, GenericArrayType targetComponetType) {
        int len = Array.getLength(src);
        Object target = newInstance(targetComponetType, len);
        Type type = targetComponetType.getGenericComponentType();
        for (int i = 0; i < len; ++i) {
            Array.set(target, i, TypeUtils.convert(Array.get(src, i), type));
        }
        return (T) target;
    }

    public static <T> T convert(Object src, WildcardType targetComponetType) {
        Type[] bounds = targetComponetType.getUpperBounds();
        return convert(src, (Class<?>) bounds[0]);
    }

    public static <T> T convert(Object src, TypeVariable targetComponetType) {
        Type[] bounds = targetComponetType.getBounds();
        return convert(src, (Class<?>) bounds[0]);
    }

    public static <T> T convert(Object src, Type targetComponetType) {
        if (src == null || targetComponetType == null) {
            return (T) src;
        }
        if (targetComponetType instanceof Class) {
            return convert(src, (Class<?>) targetComponetType);
        }

        if (src instanceof Collection) {
            src = CollectionUtils.toArray((Collection) src);
        }

        Object target;
        if (targetComponetType instanceof ParameterizedType) {
            target = convert(src, (ParameterizedType) targetComponetType);
        } else if (targetComponetType instanceof GenericArrayType) {
            target = convert(src, (GenericArrayType) targetComponetType);
        } else if (targetComponetType instanceof WildcardType) {
            target = convert(src, (WildcardType) targetComponetType);
        } else if (targetComponetType instanceof TypeVariable) {
            target = convert(src, (TypeVariable) targetComponetType);
        } else {
            throw new UnsupportedOperationException(targetComponetType + " is not support");
        }
        return (T) target;
    }

    public static <T> T convert(Object src, Class<?> targetComponetType) {
        if (src == null || targetComponetType == null) {
            return (T) src;
        }

        if (src instanceof Collection) {
            src = CollectionUtils.toArray((Collection) src);
        }

        if (!src.getClass().isArray()) {
            src = new Object[]{src};
        }

        if (targetComponetType.isAssignableFrom(src.getClass().getComponentType())) {
            return (T) src;
        }

        int len = Array.getLength(src);
        Object target = Array.newInstance(targetComponetType, len);
        for (int i = 0; i < len; ++i) {
            Object indexValue = Array.get(src, i);
            if (targetComponetType.isPrimitive()) {
                if (targetComponetType == boolean.class) {
                    Array.setBoolean(target, i, BooleanUtils.parse(indexValue));
                } else if (targetComponetType == byte.class) {
                    Array.setByte(target, i, ByteUtils.parse(indexValue));
                } else if (targetComponetType == short.class) {
                    Array.setShort(target, i, ShortUtils.parse(indexValue));
                } else if (targetComponetType == int.class) {
                    Array.setInt(target, i, IntUtils.parse(indexValue));
                } else if (targetComponetType == long.class) {
                    Array.setLong(target, i, LongUtils.parse(indexValue));
                } else if (targetComponetType == float.class) {
                    Array.setFloat(target, i, FloatUtils.parse(indexValue));
                } else if (targetComponetType == double.class) {
                    Array.setDouble(target, i, DoubleUtils.parse(indexValue));
                } else if (targetComponetType == char.class) {
                    Array.setChar(target, i, CharUtils.parse(indexValue));
                }
            } else {
                Array.set(target, i, ClassUtils.convert(indexValue, targetComponetType));
            }
        }
        return (T) target;
    }

    private static <T extends Comparable<T>> T maxByNegtive(T[] array, int negtive) {
        if (isEmpty(array)) {
            return null;
        }

        T max = array[0];
        for (int i = 1, len = array.length; i < len; ++i) {
            T data = array[i];
            if (data == null) {
                continue;
            } else if (max == null || max.compareTo(data) * negtive < 0) {
                max = data;
            }
        }
        return max;
    }

    public static <T extends Comparable<T>> T max(T[] array) {
        return maxByNegtive(array, 1);
    }

    public static <T extends Comparable<T>> T min(T[] array) {
        return maxByNegtive(array, -1);
    }

    public static <T> boolean eq(T[] src, T[] dest) {
        if (src == null) {
            return dest == null;
        }

        if (dest == null) {
            return false;
        }

        if (src.length != dest.length) {
            return false;
        }

        if (src.length == 0) {
            return true;
        }

        for (int i = 0; i < src.length; i++) {
            if (!ObjectUtils.eq(src[i], dest[i])) {
                return false;
            }
        }
        return true;
    }

}
