package com.disney.teams.utils.type;

import com.disney.teams.model.anno.Modify;
import com.disney.teams.model.pagination.PageList;
import com.disney.teams.model.pagination.IPageList;
import com.disney.teams.utils.io.CloseUtils;

import java.io.*;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author arron.zhou
 * @version 1.0.0
 * @date 2022/12/16
 * Modification  History:
 * Date         Author        Version        Description
 * ------------------------------------------------------
 * 2022/12/16       arron.zhou      1.0.0          create
 */
public abstract class ClassUtils {

    public static interface ClassParser<Q> {

        <T> Q parse(T src, Class<Q> clazz);

        <T> Q parse(T src, Class<Q> clazz, Type[] qtypes);

    }

    public static interface BeanPropertyExtractor {
        boolean isExtractable(Object bean);

        Map<String, Object> getPropertyMap(Object bean);
    }

    private static final Map<Class<?>, Map<Class<?>, Map<Type, Type>>> genericTypeMap = new HashMap<>();

    private static final Map<Class<?>, ClassParser> parserMap = new HashMap<>();

    private static final Set<BeanPropertyExtractor> extractorSet = new HashSet<>();

    public static final BeanPropertyExtractor defaultExtractor = new BeanPropertyExtractor() {
        @Override
        public boolean isExtractable(Object bean) {
            return true;
        }

        @Override
        public Map<String, Object> getPropertyMap(Object bean) {
            return MethodUtils.invokeAllGetterMethod(bean);
        }

    };

    public static final void addExtractor(BeanPropertyExtractor extract) {
        if (extract != null) {
            extractorSet.add(extract);
        }
    }

    public static final <T> void addParser(Class<T> clazz, ClassParser<T> parser) {
        if (clazz == null || parser == null) {
            throw new NullPointerException();
        }
        parserMap.put(clazz, parser);
    }

    static {
        addParser(IPageList.class, new ClassParser<IPageList>() {
            @Override
            public <T> IPageList parse(T src, Class<IPageList> clazz) {
                return parse(src, clazz, null);
            }

            @Override
            public <T> IPageList parse(T src, Class<IPageList> clazz, Type[] qtypes) {
                return ClassUtils.convert(src, PageList.class, qtypes);
            }
        });
    }

    public static boolean isRootClass(Class<?> clazz) {
        return clazz == null || Object.class.equals(clazz);
    }

    public static boolean isNotRootClass(Class<?> clazz) {
        return !isRootClass(clazz);
    }

    private static <T> T getDefaultValue(Class<T> target) {
        if (target == null) {
            return null;
        }
        Object value;
        if (boolean.class == target) {
            value = Boolean.FALSE;
        } else if (byte.class == target) {
            value = (byte) 0;
        } else if (short.class == target) {
            value = (short) 0;
        } else if (int.class == target) {
            value = 0;
        } else if (long.class == target) {
            value = 0L;
        } else if (float.class == target) {
            value = 0f;
        } else if (double.class == target) {
            value = 0d;
        } else if (char.class == target) {
            value = (char) 0;
        } else {
            value = null;
        }
        return (T) value;
    }

    public static Map<String, Object> getPropertyMap(Object bean) {
        for (BeanPropertyExtractor extract : extractorSet) {
            if (extract.isExtractable(bean)) {
                return extract.getPropertyMap(bean);
            }
        }
        return defaultExtractor.getPropertyMap(bean);
    }

    private static <T> T convertBean(Object src, Class<T> targetClass, Type[] targetRealTypes) {
        Map<String, Object> map;
        if (src instanceof Map) {
            map = MapUtils.convert((Map<?, ?>) src, String.class, null);
        } else {
            map = getPropertyMap(src);
        }

        T target = ClassUtils.newInstance(targetClass, map, targetRealTypes);
        return target;
    }

    private static boolean needConvert(Class<?> src, Class<?> target, Type[] targetRealTypes) {
        if (target == null || Object.class == target) {
            return false;
        }
        if (Iterable.class.isAssignableFrom(target) || Map.class.isAssignableFrom(target)) {
            return true;
        }
        if (ArrayUtils.isNotEmpty(targetRealTypes)) {
            return true;
        }
        return !target.isAssignableFrom(src);
    }

    private static <T> T convertEnum(Object srcValue, Class<T> target) {
        if (srcValue instanceof Enum) {
            return (T) Enum.valueOf((Class<Enum>) target, ((Enum) srcValue).name());
        } else if (srcValue instanceof Number) {
            long value = ((Number) srcValue).longValue();
            T[] enums = target.getEnumConstants();
            if (ArrayUtils.length(enums) > value) {
                return enums[(int) value];
            }
        }
        String text = srcValue.toString();
        if (StringUtils.isNumber(text)) {
            long value = Long.parseLong(text);
            T[] enums = target.getEnumConstants();
            if (ArrayUtils.length(enums) > value) {
                return enums[(int) value];
            }
        }
        return StringUtils.isBlank(text) ? null : (T) Enum.valueOf((Class<Enum>) target, text);
    }

    public static <T> T convertWithoutParser(Object srcValue, Class<T> target, Type[] targetRealTypes) {
        if (srcValue == null) {
            return getDefaultValue(target);
        }
        //当带上泛型参数转换时，此处逻辑需要整改
        if (!needConvert(srcValue.getClass(), target, targetRealTypes)) {
            return (T) srcValue;
        }
        Object obj;
        if (target == boolean.class || target == Boolean.class) {
            obj = BooleanUtils.valueOf(srcValue);
        } else if (target == byte.class || target == Byte.class) {
            obj = ByteUtils.valueOf(srcValue);
        } else if (target == short.class || target == Short.class) {
            obj = ShortUtils.valueOf(srcValue);
        } else if (target == int.class || target == Integer.class) {
            obj = IntUtils.valueOf(srcValue);
        } else if (target == long.class || target == Long.class) {
            obj = LongUtils.valueOf(srcValue);
        } else if (target == float.class || target == Float.class) {
            obj = FloatUtils.valueOf(srcValue);
        } else if (target == double.class || target == Double.class) {
            obj = DoubleUtils.valueOf(srcValue);
        } else if (target == char.class || target == Character.class) {
            obj = CharUtils.valueOf(srcValue);
        } else if (target == BigInteger.class) {
            obj = BigIntegerUtils.valueOf(srcValue);
        } else if (target == BigDecimal.class) {
            obj = BigDecimalUtils.valueOf(srcValue);
        } else if (target == Date.class) {
            obj = DateUtils.valueOf(srcValue);
        } else if (target == Timestamp.class) {
            Date date = DateUtils.valueOf(srcValue);
            obj = (date == null ? null : new Timestamp(date.getTime()));
        } else if (target.isEnum()) {
            obj = convertEnum(srcValue, target);
        } else if (target == String.class) {
            obj = srcValue.toString();
        } else if (target.isArray()) {
            obj = ArrayUtils.convert(srcValue, target.getComponentType());
        } else if (Iterable.class.isAssignableFrom(target)) {
            int len = ArrayUtils.length(targetRealTypes);
            if (len > 1) {
                throw new IllegalArgumentException("泛型参数数量不匹配");
            }
            Class<Collection> collClass = Collection.class.isAssignableFrom(target) ? (Class<Collection>) target : Collection.class;
            obj = CollectionUtils.convert(srcValue, collClass, len == 0 ? null : targetRealTypes[0]);
            return (T) obj;
        } else if (Map.class.isAssignableFrom(target)) {
            int len = ArrayUtils.length(targetRealTypes);
            if (len == 0) {
                obj = MapUtils.convert(srcValue, (Class<Map>) target);
            } else if (len == 2) {
                obj = MapUtils.convert(srcValue, (Class<Map>) target, targetRealTypes[0], targetRealTypes[1]);
            } else {
                throw new IllegalArgumentException("泛型参数数量不匹配");
            }
        } else if (Void.TYPE == target) {
            return (T) Void.TYPE;
        } else if (Class.class == target) {
            try {
                return (T) Class.forName(srcValue.toString());
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        } else {
            return convertBean(srcValue, target, targetRealTypes);
        }
        return (T) obj;
    }

    /**
     * 实现基本数据类型、日期类型、封装类型间的智能转换，但是会先通过Parser
     *
     * @param srcValue 原始数据
     * @param target   转换类型
     * @return
     */
    public static <T> T convert(Object srcValue, Class<T> target) {
        ClassParser<T> parser = parserMap.get(target);
        if (parser == null) {
            return convertWithoutParser(srcValue, target, null);
        }
        return parser.parse(srcValue, target);
    }

    /**
     * 实现基本数据类型、日期类型、封装类型间的智能转换
     *
     * @param srcValue 原始数据
     * @param target   转换类型
     * @return
     */
    public static <T> T convert(Object srcValue, Class<T> target, Type... targetRealTypes) {
        ClassParser<T> parser = parserMap.get(target);
        if (parser == null) {
            return convertWithoutParser(srcValue, target, targetRealTypes);
        }
        return parser.parse(srcValue, target, targetRealTypes);
    }

    /**
     * 根据class获取类或接口对应泛型参数的实际类型，如果是类则只获取类的泛型参数，如果是接口则只获取接口的泛型参数
     *
     * @param clazz
     * @return
     */
    public static Map<Class<?>, Map<Type, Type>> getGenericTypeMap(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        Map<Class<?>, Map<Type, Type>> typeMap = genericTypeMap.get(clazz);
        if (typeMap != null) {
            return typeMap;
        }
        synchronized (clazz) {
            typeMap = genericTypeMap.get(clazz);
            if (typeMap != null) {
                return typeMap;
            }
            typeMap = getGenericTypeMap(clazz, new HashMap<Class<?>, Map<Type, Type>>(), null);
            genericTypeMap.put(clazz, typeMap);
        }
        return typeMap;
    }

    /**
     * @param clazz     泛型类
     * @param realTypes 泛型类泛型参数对应的实际类型
     * @param type
     * @return
     */
    public static Type getRealType(Class<?> clazz, Type[] realTypes, Class<?> parentClazz, Type type) {
        Map<Class<?>, Map<Type, Type>> typeMap = getGenericTypeMap(clazz, realTypes);
        if (MapUtils.isEmpty(typeMap)) {
            return type;
        }
        Map<Type, Type> tmap = typeMap.get(clazz);
        if (MapUtils.isEmpty(tmap)) {
            return type;
        }

        Type rtype = tmap.get(type);
        return rtype == null ? null : type;
    }

    public static Map<Class<?>, Map<Type, Type>> getGenericTypeMap(Class<?> clazz, Type[] realTypes) {
        if (clazz == null || ArrayUtils.isEmpty(realTypes)) {
            return getGenericTypeMap(clazz);
        }
        return getGenericTypeMap(clazz, new HashMap<Class<?>, Map<Type, Type>>(), realTypes);
    }

    /**
     * 根据class获取类或接口对应泛型参数的实际类型，如果是类则只获取类的泛型参数，如果是接口则只获取接口的泛型参数
     *
     * @param clazz    需要获取的类
     * @param classMap 获取得到的父类及超类对应的泛型类型
     * @param types    当前泛型类型
     * @return
     */
    public static Map<Class<?>, Map<Type, Type>> getGenericTypeMap(Class<?> clazz, Map<Class<?>, Map<Type, Type>> classMap, Type[] types) {
        if (isRootClass(clazz) || classMap.containsKey(clazz)) {
            return classMap;
        }

        Map<Type, Type> typeMap = new LinkedHashMap<>();
        classMap.put(clazz, typeMap);
        Type[] parameters = clazz.getTypeParameters();
        for (int i = 0, plen = parameters.length, tlen = (types == null ? 0 : types.length); i < plen; ++i) {
            typeMap.put(parameters[i], tlen > i ? types[i] : TypeUtils.getRealType(parameters[i], null));
        }

        Type type = clazz.getGenericSuperclass();
        Type[] itfTypes = clazz.getGenericInterfaces();
        int i = 0, len = itfTypes.length;
        do {
            if (type instanceof ParameterizedType) {
                ParameterizedType ptype = (ParameterizedType) type;
                Class<?> clz = (Class<?>) ptype.getRawType();
                Type[] typeArguments = ptype.getActualTypeArguments();
                typeArguments = TypeUtils.getRealType(typeArguments, typeMap);
                getGenericTypeMap(clz, classMap, typeArguments);
            } else if (type instanceof Class) {
                getGenericTypeMap((Class<?>) type, classMap, new Type[0]);
            }
            if (i >= len) {
                break;
            }
            type = itfTypes[i++];
        } while (true);
        return classMap;
    }

    public static Map<Type, Type> getGenericTypeMap(Class<?> children, Class<?> parent) {
        if (children == null || parent == null) {
            return null;
        }
        Map<Class<?>, Map<Type, Type>> typeMap = getGenericTypeMap(children);
        return typeMap.get(parent);
    }

    /**
     * 获取祖先类实际类型
     *
     * @param children 子类，一般不带泛型参数
     * @param parent   祖先类
     * @return
     */
    public static Type[] getActualType(Class<?> children, Class<?> parent) {
        Map<Type, Type> genericTypeMap = getGenericTypeMap(children, parent);
        if (MapUtils.isEmpty(genericTypeMap)) {
            return null;
        }
        return CollectionUtils.toArray(genericTypeMap.values(), Type.class);
    }

    /**
     * 获取祖先类实际类型
     *
     * @param children 子类，一般不带泛型参数
     * @param parent   祖先类
     * @return
     */
    public static Type getActualType(Class<?> children, Class<?> parent, Type type) {
        if (type instanceof Class) {
            return type;
        }
        Map<Type, Type> genericTypeMap = getGenericTypeMap(children, parent);
        if (MapUtils.isEmpty(genericTypeMap)) {
            return type;
        }
        Type ntype = genericTypeMap.get(type);
        return ntype == null ? type : ntype;
    }

    /**
     * 获取父类泛型参数
     *
     * @param instanceClass
     * @param instanceParentClass
     * @param instanceParentGenericIndex
     * @return 返回对应泛型参数的类型
     */
    public static Type getGenericType(Class<?> instanceClass, Class<?> instanceParentClass, int instanceParentGenericIndex) {
        Type[] realTypes = getActualType(instanceClass, instanceParentClass);
        return instanceParentGenericIndex < (realTypes == null ? 0 : realTypes.length) ? realTypes[instanceParentGenericIndex] : Object.class;
    }

    public static Class<?> getParentFirstGenericType(final Class<?> clazz) {
        return getParentGenericType(clazz, 0);
    }

    /**
     * 通过反射,获得Class定义中声明的父类的泛型参数的类型.
     *
     * @param clazz
     * @param index 泛型参数位置，从左到右由0开始
     * @return 如无法找到, 返回null
     */
    public static Class<?> getParentGenericType(final Class<?> clazz, final int index) {
        Type type = getGenericType(clazz, clazz.getSuperclass(), index);
        if (type instanceof Class) {
            return (Class<?>) type;
        } else {
            return null;
        }
    }

    public static <T> Constructor<T> getConstructor(Class<T> clz, Class<?>... parameterTypes) {
        try {
            return clz.getConstructor(parameterTypes);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T newInstance(Class<T> clz) {
        return newInstance(clz, null, null);
    }

    public static <T> T newInstance(Class<T> clz, Map<String, ?> valueMap) {
        return newInstance(clz, valueMap, null);
    }

    public static <T> T newInstance(Class<T> clz, Map<String, ?> valueMap, Type[] realTypes) {
        try {
            Constructor<?>[] contructors = clz.getConstructors();
            for (Constructor<?> constructor : contructors) {
                if (ArrayUtils.isEmpty(constructor.getParameterTypes())) {
                    T obj = clz.getConstructor().newInstance();
                    return MethodUtils.invokeSetterMethod(obj, valueMap, realTypes);
                }
            }
            Object builder = MethodUtils.invokeStaticMethod(clz, "newBuilder");
            MethodUtils.invokeSetterMethod(builder, valueMap, realTypes);
            return MethodUtils.invokeMethod(builder, "build");
        } catch (Exception e) {
            throw ExceptionUtils.uncheck(e);
        }
    }

    /**
     * 复制父类共有属性，要求两个类的共同父类对应的泛型参数类型一致
     *
     * @param source     源对象实例
     * @param target     目标类实例
     * @param superClass 公共父类
     */
    public static void clone(Object source, Object target, Class<?> superClass) {
        if (ObjectUtils.hasNull(superClass, source, target)) {
            return;
        }
        List<Field> fields = FieldUtils.getFields(superClass, Boolean.FALSE);
        if (CollectionUtils.isEmpty(fields)) {
            return;
        }

        for (Field f : fields) {
            if (Modifier.isFinal(f.getModifiers())) {
                continue;
            }
            FieldUtils.setValue(target, f, FieldUtils.getValue(source, f), false);
        }
    }

    /**
     * 通过父类实例创建子类实例，子类实例将继承父类实例的数据
     *
     * @param parent     父类实例
     * @param childClass 子类class
     * @return 返回子类实例
     */
    public static <P, C extends P> C cloneParent(P parent, Class<C> childClass) {
        if (childClass == null) {
            throw new NullPointerException();
        }
        try {
            C child = ClassUtils.newInstance(childClass);
            if (parent != null) {
                clone(parent, child, parent.getClass());
            }
            return child;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 通过子类实例创建父类实例，父类将拥有与子类共有的数据
     *
     * @param child       父类实例
     * @param parentClass 子类class
     * @return 返回子类实例
     */
    public static <P, C extends P> P cloneChild(C child, Class<P> parentClass) {
        if (parentClass == null) {
            throw new NullPointerException();
        }
        try {
            P parent = ClassUtils.newInstance(parentClass);
            if (child != null) {
                clone(child, parent, parentClass);
            }
            return parent;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 该方法仅对实体bean有效
     *
     * @param source
     * @param <T>
     * @return
     */
    public static <T> T clone(T source) {
        if (source == null) {
            return source;
        }
        Class<T> clz = (Class<T>) source.getClass();
        try {
            T target = ClassUtils.newInstance(clz);
            clone(source, target, clz);
            return target;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends Serializable> T deepClone(T source) {
        if (source == null) {
            return source;
        }
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream obs = new ObjectOutputStream(out);
            obs.writeObject(source);
            obs.close();

            //分配内存，写入原始对象，生成新对象
            bis = new ByteArrayInputStream(out.toByteArray());
            ois = new ObjectInputStream(bis);
            //返回生成的新对象
            return (T) ois.readObject();
        } catch (Exception e) {
            throw ExceptionUtils.uncheck(e);
        } finally {
            CloseUtils.close(bos, oos, bis, ois);
        }
    }

    public static interface CopyCallback {

        CopyCallback COPY_IGNORE_NULL = (field, value) -> value != null;

        CopyCallback IGNORE_ALLOW_NULL = (field, value) -> {
            if (value != null) {
                return true;
            }
            Modify modify = field.getAnnotation(Modify.class);
            return modify != null && modify.nullable();
        };

        boolean isCopy(Field field, Object value);
    }

    /**
     * 将源实例的非空字段数据拷贝到目标实例中
     *
     * @param src    源实例
     * @param target 目标实例
     * @return
     */
    public static <T> T copyIgnoreNull(T src, T target) {
        return copyIgnoreNull(src, target, true);
    }

    /**
     * 将源对象的数据拷贝到目标对象中
     *
     * @param src        源对象实例
     * @param target     目标对象实例
     * @param ignoreNull 是否忽略空数据
     * @return 返回拷贝后的对象实例
     */
    public static <T> T copyIgnoreNull(T src, T target, boolean ignoreNull) {
        return copy(src, target, ignoreNull ? CopyCallback.COPY_IGNORE_NULL : null);
    }

    public static <T> T copy(T src, T target, CopyCallback callback) {
        if (src == target) {
            return target;
        } else if (src == null) {
            return target;
        } else if (target == null) {
            return src;
        }
        Class<?> clz = src.getClass();
        List<Field> fields = FieldUtils.getFields(clz, Boolean.FALSE);
        if (CollectionUtils.isEmpty(fields)) {
            return target;
        }
        for (Field f : fields) {
            if (Modifier.isFinal(f.getModifiers())) {
                continue;
            }
            Object value = FieldUtils.getValue(src, f);
            if (callback == null || callback.isCopy(f, value)) {
                FieldUtils.setValue(target, f, value, false);
            }
        }
        return target;
    }

    public static <T> T copyProperties(T src, T target, String... properties) {
        return copyProperties(src, target, null, properties);
    }

    public static <T> T copyProperties(T src, T target, CopyCallback callback, String... properties) {
        if (src == target || ArrayUtils.isEmpty(properties)) {
            return target;
        }
        if (src == null) {
            return target;
        } else if (target == null) {
            return src;
        }
        for (String property : properties) {
            if (StringUtils.isBlank(property)) {
                continue;
            }
            Field f = FieldUtils.getField(src, property);
            if (f == null) {
                continue;
            }
            Object value = FieldUtils.getValue(src, f);
            if (callback == null || callback.isCopy(f, value)) {
                FieldUtils.setValue(target, f, value, false);
            }
        }
        return target;
    }

    public static <T> T copyIgnoreProperties(T src, T target, String... properties) {
        if (ArrayUtils.isEmpty(properties)) {
            return target;
        }
        if (src == null) {
            return target;
        } else if (target == null) {
            return src;
        }
        Class<?> clz = src.getClass();
        List<Field> fields = FieldUtils.getFields(clz, Boolean.FALSE);
        if (CollectionUtils.isEmpty(fields)) {
            return target;
        }
        List<Field> includeFields = new ArrayList<>();
        for (Field f : fields) {
            if (ArrayUtils.notIn(f.getName(), properties)) {
                includeFields.add(f);
            }
        }
        for (Field f : includeFields) {
            Object value = FieldUtils.getValue(clz, f);
            FieldUtils.setValue(target, f, value, false);
        }
        return target;
    }

    /**
     * 取得某个接口下所有实现这个接口的类
     *
     * @param parent
     * @param pack
     * @return
     */
    public static <T> List<Class<? extends T>> findSubclasses(Class<T> parent, Package pack) throws IOException {
        List<Class<? extends T>> classList = new ArrayList<>();
        if (parent == null) {
            return classList;
        }
        if (pack == null) {
            pack = parent.getPackage();
        }
        // 获取当前包下以及子包下所以的类
        List<Class<?>> allClass = PackageUtils.findClasses(pack);
        if (CollectionUtils.isEmpty(allClass)) {
            return classList;
        }
        for (Class classes : allClass) {
            if (parent.isAssignableFrom(classes) && !parent.equals(classes)) {
                classList.add(classes);
            }
        }
        return classList;
    }

    /**
     * 获取该类的所有子类
     *
     * @param parent
     * @param packageName
     * @return
     */
    public static <T> List<Class<? extends T>> findSubclasses(Class<T> parent, String packageName) throws IOException {
        return findSubclasses(parent, Package.getPackage(packageName));
    }

    /**
     * 从该的包目录下，获取该类的所有子类
     *
     * @param parent
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> List<Class<? extends T>> findSubclasses(Class<T> parent) throws IOException {
        return findSubclasses(parent, (Package) null);
    }
}
