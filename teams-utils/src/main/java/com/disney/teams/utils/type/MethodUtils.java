package com.disney.teams.utils.type;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.math.BigDecimal;
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
public abstract class MethodUtils {

    public static final Object[] EMPTY_ARRAY = new Object[0];
    /**
     * <code>key</code>为setter方法名的前缀
     * <code>value</code>为setter方法的参数类型，可以填写需要支持的超类
     */
    public static final Map<String, Class<?>> SETTER_METHOD_NAME_PREFFIX_MAP = new HashMap<>(1);

    /**
     * <code>key</code>为getter方法名的前缀
     * <code>value</code>为getter方法的返回参数类型，可以填写需要支持的超类
     */
    public static final Map<String, Class<?>> GETTER_METHOD_NAME_PREFFIX_MAP = new HashMap<>(1);
    static {
        //默认的setter方法
        putSetterMatcher("set", Object.class);

        //默认的getter方法
        putGetterMatcher("get", Object.class);
        putGetterMatcher("is", boolean.class);
    }

    public static void putSetterMatcher(String prefix, Class<?> clazz){
        if(prefix == null || clazz == null){
            throw new NullPointerException();
        }
        SETTER_METHOD_NAME_PREFFIX_MAP.put(prefix, clazz);
    }

    public static void putGetterMatcher(String prefix, Class<?> clazz){
        if(prefix == null || clazz == null){
            throw new NullPointerException();
        }
        GETTER_METHOD_NAME_PREFFIX_MAP.put(prefix, clazz);
    }

    private static boolean isMatches(Map<String, Class<?>> map, String methodName, Class<?> type){
        for(Map.Entry<String, Class<?>> entry : map.entrySet()){
            Class<?> expectClass = entry.getValue();
            //如果是Object.class,代表所有类型
            if(Object.class != expectClass && !expectClass.isAssignableFrom(type)){
                continue;
            }
            String key = entry.getKey();
            if(methodName.startsWith(key)){
                return true;
            }
        }
        return false;
    }

    public static boolean isGetterMethod(Method method){
        if(method == null){
            return false;
        }
        if(ArrayUtils.isNotEmpty(method.getParameterTypes())){
            return false;
        }
        if(void.class.equals(method.getReturnType())){
            return false;
        }
        String methodName = method.getName();
        Class<?> type = method.getReturnType();
        return isMatches(GETTER_METHOD_NAME_PREFFIX_MAP, methodName, type);
    }

    public static boolean isSetterMethod(Method method){
        if(method == null){
            return false;
        }
        Class<?>[] types = method.getParameterTypes();
        if(ArrayUtils.length(types) != 1){
            return false;
        }
        Class<?> type = types[0];
        String methodName = method.getName();
        return isMatches(SETTER_METHOD_NAME_PREFFIX_MAP, methodName, type);
    }

    public static String getGetterPropertyName(Method getterMethod){
        if(!isGetterMethod(getterMethod)){
            return null;
        }
        String methodName = getterMethod.getName();
        for(String preffix : GETTER_METHOD_NAME_PREFFIX_MAP.keySet()){
            if(methodName.startsWith(preffix)){
                methodName =  methodName.substring(preffix.length());
                break;
            }
        }
        return StringUtils.uncapitalize(methodName);
    }

    public static String getSetterPropertyName(Method setterMethod){
        if(!isSetterMethod(setterMethod)){
            return null;
        }
        String methodName = setterMethod.getName();
        for(String preffix : SETTER_METHOD_NAME_PREFFIX_MAP.keySet()){
            if(methodName.startsWith(preffix)){
                methodName =  methodName.substring(preffix.length());
                break;
            }
        }
        return StringUtils.uncapitalize(methodName);
    }

    public static Map<Type, Type> getGenericTypeMap(Method method, Class<?> instanceClass){
        return getGenericTypeMap(method, instanceClass, null);
    }

    public static Map<Type, Type> getGenericTypeMap(Method method, Class<?> instanceClass, Type[] realTypes){
        Map<Type, Type> typeMap = null;
        Map<Class<?>, Map<Type, Type>> classTypeMap = ClassUtils.getGenericTypeMap(instanceClass, realTypes);
        if(MapUtils.isNotEmpty(classTypeMap)){
            Class<?> targetClass = method.getDeclaringClass();
            typeMap = classTypeMap.get(targetClass);
        }
        typeMap = (typeMap == null ? new LinkedHashMap<Type, Type>() : new LinkedHashMap<>(typeMap));
        return typeMap;
    }

    public static Type[] getParameterGenericTypes(Method method, Class<?> instanceClass){
        return getParameterGenericTypes(method, instanceClass, null);
    }

    public static Type[] getParameterGenericTypes(Method method, Class<?> instanceClass, Type[] realTypes){
        if(method == null){
            return null;
        }
        if(instanceClass == null){
            return method.getParameterTypes();
        }

        //获取方法的所有参数类型，包括泛型参数
        Type[] types = method.getGenericParameterTypes();
        if(TypeUtils.isAllClass(types)){
            return types;
        }
        //获取类(instanceClass)的泛型参数和实际类型的MAP
        Map<Type, Type> typeMap = getGenericTypeMap(method, instanceClass, realTypes);
        //解析方法前的泛型参数,
//        TypeVariable[] typeParameters = method.getTypeParameters();
//        if(ArrayUtils.isNotEmpty(typeParameters)){
//            typeMap = new HashMap<>(typeMap);
//            for(TypeVariable tv : typeParameters){
//                Type[] bounds = tv.getBounds();
//                typeMap.put(tv, ArrayUtils.isEmpty(bounds) ? Object.class : bounds[0]);
//            }
//        }
        return TypeUtils.getRealType(types, typeMap);
    }

    public static Type getReturnGenericType(Method method, Class<?> instanceClass){
        return getReturnGenericType(method, instanceClass, null);
    }

    public static <T> Class<T> getReturnClass(Method method, Class<?> instanceClass){
        Type type = getReturnGenericType(method, instanceClass, null);
        return TypeUtils.toClass(type);
    }

    public static Type getReturnGenericType(Method method, Class<?> instanceClass, Type[] realTypes){
        if(method == null){
            return null;
        }
        if(instanceClass == null){
            return method.getReturnType();
        }
        Map<Type, Type> typeMap = getGenericTypeMap(method, instanceClass, realTypes);
        Type rtType = method.getGenericReturnType();
        return TypeUtils.getRealType(rtType, typeMap);
    }

    public static <T> Class<T> getReturnClass(Method method, Class<?> instanceClass, Type[] realTypes){
        Type type = getReturnGenericType(method, instanceClass, realTypes);
        return TypeUtils.toClass(type);
    }

    private static final Method[] removeObjectMethod(Method[] methods){
        if(ArrayUtils.isEmpty(methods)){
            return methods;
        }

        int last = 0, len = methods.length;

        for(int i = 0; i < len; ++i){
            Method method = methods[i];
            if(method.getDeclaringClass() != Object.class){
                methods[last++] = method;
            }
        }

        return last >= len ? methods : Arrays.copyOf(methods, last);
    }

    private static final Map<Class<?>, List<Method>> methodMap = new HashMap<>();

    private static List<Method> fillMethod(Class<?> clz, List<Method> list){
        if(clz == null) {
            return list;
        }
        Method[] methods = removeObjectMethod(clz.getMethods());
        for(int i = 0 , len = methods.length; i < len; ++i){
            Method m = methods[i];
            //当类的方法中有泛型参数时，会多生成一个bridge方法，此方法忽略
            if(!m.isBridge()){
                list.add(m);
            }
        }
        return list;
    }

    private static List<Method> getAllMethods(Class<?> clz){
        if(clz == null){
            return null;
        }
        List<Method> list = methodMap.get(clz);
        if(list != null) {
            return list;
        }
        synchronized (clz) {
            list = methodMap.get(clz);
            if(list != null){
                return list;
            }
            list = new ArrayList<>();
            list = fillMethod(clz, list);
            methodMap.put(clz, list);
            return list;
        }
    }

    public static Method filterFirst(List<Method> methods, String methodName, int parameterSize){
        if(CollectionUtils.isEmpty(methods)){
            return null;
        }
        methods = MemberUtils.filter(methods, methodName);
        for(Method method :methods){
            if(isMatchParameterNumber(method, parameterSize)){
                return method;
            }
        }
        return null;
    }

    /**
     * 待优化
     * @param child
     * @param method
     * @param params
     * @return
     */
    public static boolean isMatchParams(Class<?> child, Method method, Object... params) {
        int percent = paramsMatchPercent(child, method, params);
        return percent > 0;
    }

    /**
     * 返回数值越大，匹配率更高，100表示100%匹配，匹配算法参数个数相同，匹配率高，无数组参数匹配率高，
     * @param child
     * @param method
     * @param params
     * @return
     */
    public static int paramsMatchPercent(Class<?> child, Method method, Object... params) {
        Map<Type, Type> typeMap = ClassUtils.getGenericTypeMap(child, method.getDeclaringClass());
        Type[] types = method.getGenericParameterTypes();
        if(ArrayUtils.isEmpty(types)) {
            return ArrayUtils.isEmpty(params) ? 100 : 0;
        }

        int tlen = types.length;
        boolean isLastTypeArray = TypeUtils.toClass(types[tlen - 1]).isArray() || types[tlen - 1] instanceof GenericArrayType;
        int plen = ArrayUtils.length(params);
        if(tlen > plen) {
            if(!isLastTypeArray || tlen != plen + 1) {
                return 0;
            }
        } else  if(tlen < plen) {
            if(isLastTypeArray) {
                for(int i = 0, len = tlen - 1; i < len; ++i) {
                    Type real = TypeUtils.getRealType(types[i], typeMap);
                    Object obj = params[i];
                    if(obj == null) {
                        continue;
                    }
                    Class<?> clazz = obj.getClass();
                    if(!TypeUtils.toClass(real).isAssignableFrom(clazz)) {
                        return 0;
                    }
                }
                //此处未判断数组类型是否匹配
                return 50;
            } else {
                return 0;
            }
        }
        for(int i = 0, len = Math.min(plen, tlen); i < len; ++i) {
            Type real = TypeUtils.getRealType(types[i], typeMap);
            Object obj = params[i];
            if(obj == null) {
                continue;
            }
            Class<?> clazz = obj.getClass();
            if(!TypeUtils.toClass(real).isAssignableFrom(clazz)) {
                return 0;
            }
        }
        return plen == tlen ? (isLastTypeArray ? 80 : 100) : 70;
    }

    public static List<Method> filterByParams(List<Method> methods, Class<?> clazz, Object... params) {
        if(CollectionUtils.isEmpty(methods)) {
            return methods;
        }
        List<Method> methodList = new ArrayList<>();
        for(Method method : methods) {
            if(isMatchParams(clazz, method, params)) {
                methodList.add(method);
            }
        }
        return methodList;
    }

    public static Method filterSimilarestByParams(List<Method> methods, Class<?> clazz, Object... params) {
        if(CollectionUtils.isEmpty(methods)) {
            return null;
        }
        int percent = 0;
        Method matches = null;
        for(Method method : methods) {
            int p = paramsMatchPercent(clazz, method, params);
            if(p > percent) {
                matches = method;
                percent = p;
            }
        }
        return matches;
    }

    public static boolean isMatchParameterNumber(Method method, int parameterNumber) {
        Type[] types = method.getGenericParameterTypes();
        if(ArrayUtils.isEmpty(types)) {
            return parameterNumber < 1;
        }
        int len = types.length;
        if(len == parameterNumber) {
            return true;
        }

        boolean isLastTypeArray = TypeUtils.toClass(types[len - 1]).isArray() || types[len - 1] instanceof GenericArrayType;
        if(!isLastTypeArray) {
            return false;
        }
        return len < parameterNumber || len == parameterNumber + 1;
    }

    public static List<Method> filterBySize(List<Method> methods, int parameterNumber) {
        if(CollectionUtils.isEmpty(methods)) {
            return methods;
        }
        List<Method> methodList = new ArrayList<>();
        for(Method m : methods) {
            if(isMatchParameterNumber(m, parameterNumber)) {
                methodList.add(m);
            }
        }
        return methodList;
    }

    public static List<Method> getMethods(Class<?> clz, Boolean isStatic){
        List<Method> methodList = getAllMethods(clz);
        return MemberUtils.filter(methodList, isStatic);
    }

    public static List<Method> getMethods(Class<?> clz){
        return getMethods(clz, null);
    }

    public static List<Method> getPublicMethods(Class<?> clz, Boolean isStatic){
        List<Method> list = getMethods(clz, isStatic);
        List<Method> publicMethods = MemberUtils.filter(list, Modifier.PUBLIC);
        return publicMethods;
    }

    public static List<Method> getPublicMethods(Class<?> clz){
        return getPublicMethods(clz, Boolean.FALSE);
    }

    public static List<Method> getGetterMethods(Class<?> clz){
        List<Method> methods = getPublicMethods(clz);
        if(CollectionUtils.isEmpty(methods)){
            return methods;
        }
        List<Method> getterMethods = new ArrayList<>();
        for(Method method : methods){
            if(isGetterMethod(method)){
                getterMethods.add(method);
            }
        }
        return getterMethods;
    }

    private static List<Method> filterByPropertyName(List<Method> methods, String propertyName, Map<String, Class<?>> methodNamePrefixMap) {
        if(CollectionUtils.isEmpty(methods)){
            return methods;
        }
        propertyName = StringUtils.capitalize(propertyName);
        List<Method> methodList = new ArrayList<>(1);
        for(Method method : methods){
            String methodName = method.getName();
            for(String preffix : methodNamePrefixMap.keySet()){
                if(methodName.startsWith(preffix)){
                    int index = methodName.lastIndexOf(propertyName);
                    if(index == preffix.length() && index + propertyName.length() == methodName.length()){
                        methodList.add(method);
                    }
                }
            }
        }
        return methodList;
    }

    private static List<Method> getMethodsByPropertyName(Class<?> clz, String propertyName, Map<String, Class<?>> methodNamePrefixMap){
        List<Method> methods = getMethods(clz, Boolean.FALSE);
        return filterByPropertyName(methods, propertyName, methodNamePrefixMap);
    }

    public static Method getGetterMethodByPropertyName(Class<?> clz, String propertyName){
        List<Method> methods = getGetterMethodsByPropertyName(clz, propertyName);
        return CollectionUtils.isEmpty(methods) ? null : methods.get(0);
    }

    public static List<Method> getGetterMethodsByPropertyName(Class<?> clz, String propertyName){
        List<Method> methods = getGetterMethods(clz);
        return filterByPropertyName(methods, propertyName, GETTER_METHOD_NAME_PREFFIX_MAP);
    }

    public static Method getSetterMethodByPropertyName(Class<?> clz, String propertyName){
        List<Method> methods = getSetterMethodsByPropertyName(clz, propertyName);
        return CollectionUtils.isEmpty(methods) ? null : methods.get(0);
    }

    public static List<Method> getSetterMethodsByPropertyName(Class<?> clz, String propertyName){
        List<Method> methods = getSetterMethods(clz);
        return filterByPropertyName(methods, propertyName, SETTER_METHOD_NAME_PREFFIX_MAP);
    }

    public static List<Method> getSetterMethods(Class<?> clz){
        List<Method> methods = getPublicMethods(clz);
        if(CollectionUtils.isEmpty(methods)){
            return methods;
        }
        List<Method> setterMethods = new ArrayList<>();
        for(Method method : methods){
            if(isSetterMethod(method)){
                setterMethods.add(method);
            }
        }
        return setterMethods;
    }

    public static Method getPublicMethod(Class<?> clz, String methodName, Boolean isStatic){
        List<Method> methodList = getPublicMethods(clz, isStatic);
        if(CollectionUtils.isEmpty(methodList)){
            return null;
        }
        for(Method m : methodList){
            if(m.getName().equals(methodName)){
                return m;
            }
        }
        return null;
    }

    public static List<Method> getPublicMethods(Class<?> clz, String methodName, Boolean isStatic){
        List<Method> methodList = getPublicMethods(clz, isStatic);
        if(CollectionUtils.isEmpty(methodList)){
            return null;
        }
        List<Method> methods = new ArrayList<>();
        for(Method m : methodList){
            if(m.getName().equals(methodName)){
                methods.add(m);
            }
        }
        return methods;
    }

    public static Method getPublicMethod(Class<?> clz, String methodName){
        return getPublicMethod(clz, methodName, Boolean.FALSE);
    }

    public static <T extends Annotation> List<Method> getMethodByAnno(Class<?> clz, Class<T> annoClz){
        List<Method> methodList = getMethods(clz);
        if(CollectionUtils.isEmpty(methodList)){
            return methodList;
        }
        List<Method> annoMethods = new ArrayList<>();
        for(Method method : methodList){
            T anno = method.getAnnotation(annoClz);
            if(anno != null){
                MemberUtils.setAccessible(method);
                annoMethods.add(method);
            }
        }
        return annoMethods;
    }

    /**
     * 通过反射调实例的方法
     * @param obj 对象实例
     * @param methodName 方法名
     * @param argTypes 方法参数类型
     * @param args 方法参数
     * @return
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static <T> T invokeMethod(Object obj, String methodName, Class<?>[] argTypes, Object[] args) throws SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException{
        return invokeMethod(obj, obj.getClass(), methodName, argTypes, args);
    }

    /**
     * 通过反射调用实例父类的方法
     * @param obj 对象实例
     * @param parentClz 父对象
     * @param methodName 父对象对应的方法名
     * @param argTypes 方法参数类型
     * @param args 方法参数
     * @return
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static <T> T invokeMethod(Object obj, Class<?> parentClz, String methodName, Class<?>[] argTypes, Object[] args) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
        Method method = parentClz.getDeclaredMethod(methodName, argTypes);
        MemberUtils.setAccessible(method);
        return (T)method.invoke(obj, args);
    }

    public static <T> T invokeMethod(Object obj, String methodName){
        if(obj == null){
            return null;
        }
        List<Method> methods = getMethods(obj.getClass(), null);
        Method method = filterFirst(methods, methodName, 0);
        return invokeMethod(obj, method);
    }

    public static <T> T invokeMethod(Object obj, Method method){
        try {
            return (T)method.invoke(obj);
        } catch (IllegalAccessException e) {
            //                e.printStackTrace();
            return null;
        } catch (InvocationTargetException e) {
            //                e.printStackTrace();
            return null;
        }
    }

    public static <T> T invokeMethod(Object obj, Method method, Object... params){
        try {
            return (T)method.invoke(obj, params);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw ExceptionUtils.uncheck(e);
        }
    }

    public static <T> T invokeSetterMethod(T obj, String fieldName, Class<?> type, Object value){
        if(obj == null){
            return null;
        }
        Class<?> clz = obj.getClass();
        String methodName = "set" + StringUtils.capitalize(fieldName);
        List<Method> methodList = getPublicMethods(clz, methodName, Boolean.FALSE);
        if(CollectionUtils.isEmpty(methodList)){
            throw new UnsupportedOperationException(String.format("No such method " + clz.getName() + "." + methodName));
        }
        Method m = null;
        for (Method method : methodList) {
            if(isSetterMethod(method)) {
                Class<?>[] types = method.getParameterTypes();
                if (types[0].equals(type)) {
                    m = method;
                    break;
                }
            }
        }
        if(m == null){
            throw new UnsupportedOperationException(String.format("No such method " + clz.getName() + "." + methodName + " with one parameter"));
        }
        value = ClassUtils.convert(value, type);
        invokeMethod(obj, m, value);
        return obj;
    }

    public static <T> T invokeSetterMethod(T obj, String propertyName, Object value){
        if(obj == null){
            return null;
        }
        Class<?> clz = obj.getClass();
        List<Method> methodList = getSetterMethodsByPropertyName(clz, propertyName);
        if(CollectionUtils.isEmpty(methodList)){
            throw new UnsupportedOperationException(String.format("No such getter method " + clz.getName() + "." + propertyName));
        }

        Type type = null;
        Method m = null;
        for (Method method : methodList) {
            Type[] types = method.getGenericParameterTypes();
            //优先选择非成员类型参数
            if(types[0] instanceof Class && !((Class)types[0]).isMemberClass()){
                type = types[0];
                m = method;
                break;
            } else if(m == null){
                type = types[0];
                m = method;
            } else {
                throw new UnsupportedOperationException(String.format("There are several methods " + clz.getName() + "." + propertyName + " with one parameter"));
            }
        }
        if(m == null){
            throw new UnsupportedOperationException(String.format("No such method " + clz.getName() + "." + propertyName + " with one parameter"));
        }
        Map<Type, Type> typeMap = ClassUtils.getGenericTypeMap(clz, m.getDeclaringClass());
        type = TypeUtils.getRealType(type, typeMap);
        value = TypeUtils.convert(value, type);
        invokeMethod(obj, m, value);
        return obj;
    }

    public static <T> T invokeIncreMethod(T obj, Map<String, ? extends Number> valueMap){
        if(MapUtils.isEmpty(valueMap)){
            return obj;
        }
        for(Map.Entry<String, ? extends Number> entry : valueMap.entrySet()) {
            String key = entry.getKey();
            Number incre = entry.getValue();
            if(incre == null) {
                continue;
            }
            Number number = MethodUtils.invokeGetterMethod(obj, key);
            if(number != null) {
                BigDecimal old = BigDecimalUtils.valueOf(number);
                incre = old.add(BigDecimalUtils.valueOf(incre));
            }
            MethodUtils.invokeSetterMethod(obj, key, incre);
        }
        return obj;
    }

    public static <T> T invokeSetterMethod(T obj, Map<String, ?> valueMap){
        if(MapUtils.isEmpty(valueMap)){
            return obj;
        }
        for(Map.Entry<String, ?> entry : valueMap.entrySet()){
            invokeSetterMethod(obj, entry.getKey(), entry.getValue());
        }
        return obj;
    }

    public static <T> T invokeSetterMethod(T obj, Map<String, ?> valueMap, Type[] objRealTypes){
        if(obj == null || MapUtils.isEmpty(valueMap)){
            return obj;
        }

        RuntimeException exp = null;
        String expName = null;

        Map<String, Object> map = new HashMap<>(valueMap);
        Class<?> targetClass = obj.getClass();
        List<Method> setterMethods = MethodUtils.getSetterMethods(obj.getClass());
        for(Method method : setterMethods){
            Type realType = MethodUtils.getParameterGenericTypes(method, targetClass, objRealTypes)[0];
            String name = MethodUtils.getSetterPropertyName(method);
            Object value = map.remove(name);
            if(value == null){
                continue;
            }
            try {
                value = TypeUtils.convert(value, realType);
                MethodUtils.invokeMethod(obj, method, value);
                if(name.equals(expName)){
                    exp = null;
                    expName = null;
                }
            } catch (RuntimeException e){
                exp = e;
                expName = name;
                map.put(name, value);
            }
        }

        if(exp != null){
            throw exp;
        }
        return obj;
    }

    private static final Map<Annotation, Map<String, Object>> annoValueMap = new HashMap<>();
    public static <P extends Annotation> Map<String, Object> invokeAllMethod(P anno){
        if (anno == null) {
            return null;
        }

        Map<String, Object> valueMap = annoValueMap.get(anno);
        if(valueMap != null){
            return new HashMap<>(valueMap);
        }

        Method[] methods = anno.getClass().getDeclaredMethods();
        valueMap = new HashMap<>(methods.length);
        for(Method method : methods) {
            if(void.class.equals(method.getReturnType())){
                continue;
            } else if(!Modifier.isPublic(method.getModifiers())){
                continue;
            } else if(ArrayUtils.isNotEmpty(method.getParameterTypes())){
                continue;
            }

            try {
                Object obj = method.invoke(anno);
                valueMap.put(method.getName(), obj);
            } catch (IllegalAccessException e) {
                //                e.printStackTrace();
                //                return null;
            } catch (InvocationTargetException e) {
                //                e.printStackTrace();
                //                return null;
            }
        }
        annoValueMap.put(anno, valueMap);
        return new HashMap<>(valueMap);
    }

    //grpc对应repeated定义的字段，会自动在getter方法名后多加一个List, 与标准的bean对应需要进行截取
//    private static String substringFieldName(Class<?> beanClass, String fieldName){
//        Class<?> superClass = beanClass.getSuperclass();
//        if(superClass != null && "com.google.protobuf.GeneratedMessage".equals(superClass.getName())){
//            fieldName = fieldName.substring(0, fieldName.length() - 4);
//        }
//        return fieldName;
//    }

    public static <T> T invokeGetterMethod(Object bean, String propertyName) {
        if(ObjectUtils.hasNull(bean, propertyName)) {
            return null;
        }
        Method method = getGetterMethodByPropertyName(bean.getClass(), propertyName);
        if(method == null) {
            return null;
        }
        try {
            return (T) method.invoke(bean);
        } catch (Exception e) {
            throw ExceptionUtils.uncheck(e);
        }
    }

    public static <T> T invokeGetterMethod(Object bean, String... propertyNames) {
        if(ObjectUtils.hasNull(bean, propertyNames) || ArrayUtils.isEmpty(propertyNames)) {
            return null;
        }
        for(String propertyName : propertyNames) {
            if(StringUtils.isBlank(propertyName)) {
                continue;
            }
            if(bean == null) {
                return null;
            }
            Method method = getGetterMethodByPropertyName(bean.getClass(), propertyName);
            if (method == null) {
                return null;
            }
            try {
                bean = method.invoke(bean);
            } catch (Exception e) {
                throw ExceptionUtils.uncheck(e);
            }
        }
        return (T) bean;
    }

    public static Map<String, Object> invokeAllGetterMethod(Object bean){
        if(bean == null){
            return null;
        }
        Class<?> beanClass = bean.getClass();
        List<Method> methods = getGetterMethods(beanClass);
        if(CollectionUtils.isEmpty(methods)){
            return null;
        }
        Map<String, Object> map = new HashMap<>(methods.size());
        for(Method method : methods){
            try {
                Object value = method.invoke(bean);
                String fieldName = getGetterPropertyName(method);
                map.put(fieldName, value);
            } catch (Exception e) {
                //method from getGetterMethod couldn't throw any exception
            }

        }
        return map;
    }

    /**
     * 通过给定的正则式，调用指定对象中名称匹配的公有无参方法，且不包括静态方法
     * @param obj
     * @param methodRegex
     */
    public static <T> void invokeMethodByRegex(T obj, String methodRegex){
        if(obj == null){
            return;
        }
        Class<?> clz = obj.getClass();
        do{
            Method[] methods = clz.getDeclaredMethods();
            for(Method method : methods){
                int modifier = method.getModifiers();
                if(Modifier.isStatic(modifier) || !Modifier.isPublic(modifier)){
                    continue;
                }
                if(!method.getName().matches(methodRegex)){
                    continue;
                }
                int len = method.getParameterTypes().length;
                if(len < 1){
                    try {
                        method.invoke(obj, new Object[0]);
                    } catch (Exception e) {
                        //e.printStackTrace();
                    }
                }
            }
            clz = clz.getSuperclass();
        }while(ClassUtils.isNotRootClass(clz));
    }

    private static Object[] convertArgs(Method method, Object... args) {
        Type[] types = method.getGenericParameterTypes();
        if(ArrayUtils.isEmpty(types)) {
            return new Object[0];
        }

        int typeLen = types.length;
        int argSize = ArrayUtils.length(args);

        boolean isLastTypeArray = TypeUtils.toClass(types[typeLen - 1]).isArray() || types[typeLen - 1] instanceof GenericArrayType;
        Object[] objs = new Object[typeLen];
        int len;
        if(typeLen == argSize) {
            len = typeLen - 1;
            objs[len] = TypeUtils.convert(args[len], types[len]);
        } else if(typeLen < argSize) {
            len = typeLen - 1;
            if(isLastTypeArray) {
                objs[len] = TypeUtils.convert(Arrays.copyOfRange(args, len, argSize), types[len]);
            } else {
                objs[len] = TypeUtils.convert(args[len], types[len]);
            }
        } else {
            len = argSize;
            if(typeLen == argSize + 1) {
                objs[argSize] = TypeUtils.convert(new Object[0], types[argSize]);
            }
        }

        for(int i = 0; i < len; ++i) {
            objs[i] = TypeUtils.convert(args[i], types[i]);
        }
        return objs;
    }

    public static <T> T invokeStaticMethod(Class<?> clz, String methodName){
        List<Method> methods = getMethods(clz, Boolean.TRUE);
        Method method = filterFirst(methods, methodName, 0);
        try {
            if(method == null){
                throw new NoSuchMethodException(clz.getName() + "#" + methodName);
            }
            MemberUtils.setAccessible(method);
            Object value = method.invoke(null, convertArgs(method, EMPTY_ARRAY));
            return (T)value;
        } catch (Exception e) {
            throw ExceptionUtils.uncheck(e);
        }
    }

    /**
     * 建议在方法名唯一情况下使用
     * @param clz
     * @param methodName
     * @param args
     * @param <T>
     * @return
     */
    public static <T> T invokeStaticMethod(Class<?> clz, String methodName, Object... args){
        try {
            List<Method> methodList = getMethods(clz, Boolean.TRUE);
            Method method = filterFirst(methodList, methodName, ArrayUtils.length(args));
            MemberUtils.setAccessible(method);
            Object value = method.invoke(null, convertArgs(method, args));
            return (T) value;
        } catch (Exception e) {
            throw ExceptionUtils.uncheck(e);
        }
    }

    public static <T> T invokeStaticMethod(Method method, Object... args){
        try {
            MemberUtils.setAccessible(method);
            Object value = method.invoke(null, convertArgs(method, args));
            return (T)value;
        } catch (Exception e) {
            throw ExceptionUtils.uncheck(e);
        }
    }

    public static <T> T invokeStaticMethod(Class<?> clz, String methodName, Class<?>[] types, Object[] args){
        try {
            Method method = clz.getDeclaredMethod(methodName, types);
            MemberUtils.setAccessible(method);
            Object value = method.invoke(null, convertArgs(method, args));
            return (T)value;
        } catch (Exception e) {
            throw ExceptionUtils.uncheck(e);
        }
    }

}
