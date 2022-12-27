package com.disney.teams.utils.type;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

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
public abstract class FieldUtils {

    private static final Map<Class<?>, List<Field>> fieldMap = new HashMap<>();

    private static List<Field> getAllFields(Class<?> clz) {
        if (clz == null) {
            return null;
        }
        List<Field> list = fieldMap.get(clz);
        if (list != null) {
            return list;
        }
        synchronized (clz) {
            if (fieldMap.containsKey(clz)) {
                return null;
            }
            list = new ArrayList<>();
            fieldMap.put(clz, list);
        }
        while (ClassUtils.isNotRootClass(clz)) {
            Field[] fields = clz.getDeclaredFields();
            list.addAll(Arrays.asList(fields));
            clz = clz.getSuperclass();
        }
        return list;
    }

    public static List<Field> getFields(Class<?> clz) {
        return getFields(clz, null);
    }

    public static List<Field> getFields(Class<?> clz, Boolean isStatic) {
        List<Field> fields = getAllFields(clz);
        fields = MemberUtils.filter(fields, isStatic);
        return fields;
    }

    private static Field getField(Class<?> clz, String fieldName, Boolean isStatic) {
        List<Field> fields = getFields(clz, isStatic);
        if (CollectionUtils.isEmpty(fields)) {
            return null;
        }
        for (Field f : fields) {
            if (f.getName().equals(fieldName)) {
                return f;
            }
        }
        return null;
    }

    /**
     * 将类属性转换为Map<br>
     * <strong>注意：如果父类有同名字段，将被覆盖</strong>
     */
    public static Map<String, Field> getFieldMap(Class<?> clz) {
        return getFieldMap(clz, false);
    }

    /**
     * 将类属性转换为Map<br>
     * <strong>注意：如果父类有同名字段，将被覆盖</strong>
     */
    public static Map<String, Field> getFieldMap(Class<?> clz, boolean accessible) {
        List<Field> fieldList = getFields(clz, Boolean.FALSE);
        if (CollectionUtils.isEmpty(fieldList)) {
            return Collections.emptyMap();
        }
        Map<String, Field> fieldMap = new HashMap<>(fieldList.size());
        for (Field field : fieldList) {
            if (accessible) {
                MemberUtils.setAccessible(field);
            }
            fieldMap.put(field.getName(), field);
        }
        return fieldMap;
    }

    public static Type getActualType(Class<?> child, String fieldName) {
        Field field = getField(child, fieldName);
        return getActualType(child, field);
    }

    public static Type getActualType(Class<?> child, Field field) {
        if (field == null) {
            return null;
        }
        return ClassUtils.getActualType(child, field.getDeclaringClass(), field.getGenericType());
    }

    public static Type getActualType(ParameterizedType instanceType, String fieldName) {
        if (ObjectUtils.hasNull(instanceType, fieldName)) {
            return null;
        }
        Class<?> clz = (Class<?>) instanceType.getRawType();
        Field field = getField(clz, fieldName);
        if (field == null) {
            return null;
        }
        return getActualType(instanceType, field);
    }

    public static Type getActualType(ParameterizedType instanceType, Field field) {
        if (field == null) {
            return null;
        }
        if (instanceType == null) {
            return field.getGenericType();
        }
        Class<?> clz = (Class<?>) instanceType.getRawType();
        Type[] types = instanceType.getActualTypeArguments();
        Type type = ClassUtils.getRealType(clz, types, field.getDeclaringClass(), field.getGenericType());
        return type;
    }

    public static Type getActualType(Type instanceType, String fieldName) {
        if (ObjectUtils.hasNull(instanceType, fieldName)) {
            return null;
        }
        if (instanceType instanceof Class) {
            return getActualType((Class<?>) instanceType, fieldName);
        } else if (instanceType instanceof ParameterizedType) {
            return getActualType((ParameterizedType) instanceType, fieldName);
        } else {
            throw new UnsupportedOperationException(instanceType + " " + fieldName + " is not support!");
        }
    }

    public static Type getActualType(Type instanceType, Field field) {
        if (field == null) {
            return null;
        }
        Type type = field.getGenericType();
        if (type instanceof Class) {
            return type;
        }

        if (instanceType instanceof Class) {
            return getActualType((Class<?>) instanceType, field);
        } else if (instanceType instanceof ParameterizedType) {
            return getActualType((ParameterizedType) instanceType, field);
        } else {
            throw new UnsupportedOperationException(instanceType + " " + field + " is not support!");
        }
    }

    public static void setValue(Object target, Field field, Object value, boolean autoConvertValue) {
        if (ObjectUtils.hasNull(target, field)) {
            return;
        }
        MemberUtils.setAccessible(field);
        Type type = getActualType(target.getClass(), field);
        try {
            if (type == boolean.class) {
                field.setBoolean(target, BooleanUtils.parse(value));
            } else if (type == byte.class) {
                field.setByte(target, ByteUtils.parse(value));
            } else if (type == short.class) {
                field.setShort(target, ShortUtils.parse(value));
            } else if (type == int.class) {
                field.setInt(target, IntUtils.parse(value));
            } else if (type == long.class) {
                field.setLong(target, LongUtils.parse(value));
            } else if (type == float.class) {
                field.setFloat(target, FloatUtils.parse(value));
            } else if (type == double.class) {
                field.setDouble(target, DoubleUtils.parse(value));
            } else if (type == char.class) {
                field.setChar(target, CharUtils.parse(value));
            } else {
                field.set(target, autoConvertValue ? TypeUtils.convert(value, type) : value);
            }
        } catch (Exception e) {
            throw ExceptionUtils.uncheck(e);
        }
    }

    /**
     * 通过反射给指定类实例字段注入数据<br>
     * 数据类型将会自动实现转换<br>
     * 如果字段类型为日期类型，会自动将"yyyy-MM-dd HH:mm:ss"格式的字符串转换为日期类型
     * @param target 类实例
     * @param field  对应字段
     * @param value  字段数值
     */
    public static void setValue(Object target, Field field, Object value) {
        setValue(target, field, value, true);
    }

    public static Field getAccessibleField(Object obj, String fieldName) {
        return obj == null ? null : getAccessibleField(obj.getClass(), fieldName, null);
    }

    public static Field getAccessibleField(Object obj, String fieldName, Boolean isStatic) {
        return obj == null ? null : getAccessibleField(obj.getClass(), fieldName, isStatic);
    }

    public static Field getAccessibleField(Class<?> clz, String fieldName) {
        return getAccessibleField(clz, fieldName, null);
    }

    public static Field getAccessibleField(Class<?> clz, String fieldName, Boolean isStatic) {
        Field field = getField(clz, fieldName, isStatic);
        if (field == null) {
            return field;
        }
        MemberUtils.setAccessible(field);
        return field;
    }

    public static Field getField(Object obj, String fieldName) {
        if (obj == null) {
            return null;
        }
        return getField(obj.getClass(), fieldName);
    }

    public static Field getField(Class<?> clz, String fieldName) {
        return getField(clz, fieldName, null);
    }

    /**
     * 给指定实例注入字段数据,类型自动转换
     * @param obj       类实例
     * @param fieldName 字段名称
     * @param value     字段数值
     */
    public static void setValue(Object obj, String fieldName, Object value) {
        Field f = getAccessibleField(obj, fieldName);
        if (f == null) {
            return;
        }
        setValue(obj, f, value);
    }

    /**
     * 设置字段数值
     */
    public static void setValue(Object bean, String fieldName, Object value, boolean ignoreNull) {
        if (ignoreNull && value == null) {
            return;
        }
        setValue(bean, fieldName, value);
    }

    public static <T> T getValue(Object bean, String fieldName) {
        return getValue(bean, fieldName, null);
    }

    public static <T> T getValue(Object bean, String... fieldNames) {
        if (ArrayUtils.isEmpty(fieldNames)) {
            return null;
        }
        for (String fieldName : fieldNames) {
            if (StringUtils.isBlank(fieldName)) {
                continue;
            }
            bean = getValue(bean, fieldName);
        }
        return (T) bean;
    }

    public static <T> T getValue(Object bean, String fieldName, Class<T> clz) {
        return getValue(bean, fieldName, clz, null);
    }

    public static <T> T getValue(Object bean, String fieldName, Class<T> clz, Boolean isStatic) {
        try {
            Field f = getAccessibleField(bean, fieldName, isStatic);
            if (f == null) {
                return null;
            }
            Object value = f.get(bean);
            return clz == null ? (T) value : ClassUtils.convert(f.get(bean), clz);
        } catch (Exception e) {
            throw ExceptionUtils.uncheck(e);
        }
    }

    public static <T> T getValue(Object bean, Field f) {
        return getValue(bean, f, null);
    }

    public static <T> T getValue(Object bean, Field f, Class<T> clz) {
        try {
            if (f == null) {
                return null;
            }
            MemberUtils.setAccessible(f);
            Object value = f.get(bean);
            return clz == null ? (T) value : ClassUtils.convert(value, clz);
        } catch (Exception e) {
            throw ExceptionUtils.uncheck(e);
        }
    }

    public static <T> T getStaticValue(Class<?> clz, String fieldName) {
        return getStaticValue(clz, fieldName, null);
    }

    public static <T> T getStaticValue(Class<?> clz, String fieldName, Class<T> target) {
        try {
            Field f = getAccessibleField(clz, fieldName, Boolean.TRUE);
            return f == null ? null : ClassUtils.convert(f.get(null), target);
        } catch (Exception e) {
            throw ExceptionUtils.uncheck(e);
        }
    }
}
