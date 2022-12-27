package com.disney.teams.utils.type;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.BiConsumer;

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
public class MapUtils {

    private static final Map<Class<?>, Constructor<?>> defaultInstance = new HashMap<>();
    static {
        try {
            defaultInstance.put(Map.class, HashMap.class.getConstructor());
            defaultInstance.put(SortedMap.class, TreeMap.class.getConstructor());
            defaultInstance.put(NavigableMap.class, TreeMap.class.getConstructor());
            defaultInstance.put(ConcurrentMap.class, ConcurrentHashMap.class.getConstructor());
            defaultInstance.put(ConcurrentNavigableMap.class, ConcurrentSkipListMap.class.getConstructor());
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        for(Constructor<?> constructor : defaultInstance.values()){
            if(!MemberUtils.isPublic(constructor)){
                throw new RuntimeException(constructor + " is not accessible");
            }
        }
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNotEmpty(Map<?, ?> map){
        return !isEmpty(map);
    }

    public static int size(Map<?, ?> map){
        return map == null ? 0 : map.size();
    }

    public static void clear(Map<?, ?> map){
        if(map != null){
            map.clear();
        }
    }

    public static <K, V> Map<K, V> newHashMap(K key, V value){
        Map<K, V> map = new HashMap<>(1);
        map.put(key, value);
        return map;
    }

    public static <K, V> Map<K, V> newLinkedHashMap(K key, V value){
        Map<K, V> map = new LinkedHashMap<>(1);
        map.put(key, value);
        return map;
    }

    public static <K, V> Map<K, V> newConcurrentHashMap(K key, V value){
        Map<K, V> map = new ConcurrentHashMap<>(1);
        map.put(key, value);
        return map;
    }

    private static <T extends Map<?, ?>> T newInstance(Class<T> targetClass){
        try {
            Constructor<?> contructor = defaultInstance.get(targetClass == null ? Map.class : targetClass);
            if(contructor == null){
                contructor = targetClass.getConstructor();
            }
            Object target = contructor.newInstance();
            return (T) target;
        } catch (Exception e) {
            throw ExceptionUtils.uncheck(e);
        }
    }

    public static <T extends Map<K, V>, K, V> T convert(Map<K, V> src, Class<T> targetClass){
        //如果实现类是当前的子类，则直接返回
        if(targetClass == null || targetClass.isAssignableFrom(src.getClass())){
            return (T) src;
        }
        T target = newInstance(targetClass);
        target.putAll(src);
        return target;
    }

    public static <T, K> Map<T, K> convert(Map<?, ?> srcMap, Class<T> keyType, Class<K> valueType){
        if(srcMap == null){
            return null;
        } else if(srcMap.isEmpty()){
            return new HashMap<>();
        }
        Map<T, K> targetMap = new HashMap<>(srcMap.size());
        for(Map.Entry<?, ?> entry : srcMap.entrySet()){
            T key = ClassUtils.convert(entry.getKey(), keyType);
            K value = ClassUtils.convert(entry.getValue(), valueType);
            targetMap.put(key, value);
        }
        return targetMap;
    }

    public static <T extends Map<K, V>, K, V> T convert(Object src, Class<T> targetClass) {
        return convert(src, targetClass, null, null);
    }

    public static <T extends Map<K, V>, K, V> T convert(Object src, Class<T> targetClass, Type keyType, Type valueType) {
        if(targetClass == null){
            throw new NullPointerException();
        }
        if(src == null){
            return null;
        }
        Map<?, ?> srcMap;
        if(src instanceof Map){
            srcMap = (Map<?, ?>)src;
        } else {
            srcMap = MethodUtils.invokeAllGetterMethod(src);
        }
        T target = newInstance(targetClass);
        Iterator<?> itr = srcMap.entrySet().iterator();
        while(itr.hasNext()){
            Map.Entry entry = (Map.Entry)itr.next();
            K key = TypeUtils.convert(entry.getKey(), keyType);
            V value = TypeUtils.convert(entry.getValue(), valueType);
            target.put(key, value);
        }
        return target;
    }

    public static <K, V> V get(Map<K, V> map, K key){
        return isEmpty(map) ? null : map.get(key);
    }

    public static <K, V> V remove(Map<K, V> map, K key){
        return isEmpty(map) ? null : map.remove(key);
    }

    public static <K, V> V getFirst(Map<K, V> map){
        if(isEmpty(map)){
            return null;
        }
        return map.values().iterator().next();
    }

    public static <K, V> Map<K, V> merge(Map<K, V> src, Map<K, V> dest){
        if(src == null){
            return dest == null ? null : new HashMap<>(dest);
        }
        Map<K, V> map = new HashMap<>(src);
        if(isEmpty(dest)){
            return map;
        }
        map.putAll(dest);
        return map;
    }

    public static <K, V> Map<K, V> removeNullKey(Map<K, V> map) {
        if(isEmpty(map)) {
            return map;
        }
        Iterator<Map.Entry<K, V>> itr = map.entrySet().iterator();
        while(itr.hasNext()) {
            Map.Entry<K, V> entry = itr.next();
            if(entry.getKey() == null) {
                itr.remove();
            }
        }
        return map;
    }

    public static <K, V> Map<K, V> removeNullValue(Map<K, V> map) {
        if(isEmpty(map)) {
            return map;
        }
        Iterator<Map.Entry<K, V>> itr = map.entrySet().iterator();
        while(itr.hasNext()) {
            Map.Entry<K, V> entry = itr.next();
            if(entry.getValue() == null) {
                itr.remove();
            }
        }
        return map;
    }

    public static <K, V> Map<K, V> removeNullKeyValue(Map<K, V> map) {
        if(isEmpty(map)) {
            return map;
        }
        Iterator<Map.Entry<K, V>> itr = map.entrySet().iterator();
        while(itr.hasNext()) {
            Map.Entry<K, V> entry = itr.next();
            if(entry.getValue() == null || entry.getKey() == null) {
                itr.remove();
            }
        }
        return map;
    }

    public static Map<String, String> fromText(String text, String keyGap, String kvGap) {
        if(StringUtils.isBlank(text)) {
            return null;
        }
        Map<String, String> map = new HashMap<>();
        String[] kvs = StringUtils.toArray(text, kvGap);
        for(String kv : kvs) {
            if(StringUtils.isBlank(kv)) {
                continue;
            }
            int index = kv.indexOf(keyGap);
            if(index < 1 || index == kv.length() - 1) {
                continue;
            }
            String key = kv.substring(0, index);
            String value = kv.substring(index + 1);
            map.put(key, value);
        }
        return map;
    }

    /**
     * 参数转数组
     * @param oldValue
     * @param newValue
     * @param <T>
     * @return
     */
    private static <T> T[] toArray(T oldValue, T newValue) {
        T[] values = (T[])Array.newInstance(newValue.getClass(), 2);
        values[0] = oldValue;
        values[1] = newValue;
        return values;
    }

    /**
     * 将source的map键值对替换成新的Map里的键值对，如果数据不一致，一致的数据不做替换
     * @param source 待替换的Map
     * @param replace 替换Map
     * @param addCallback
     * @param changeCallback value数据固定为2个元素，第一个元素表示老数据，第二个元素表示新数据
     * @param removeCallback
     * @param <K>
     * @param <V>
     */
    public static <K, V> void replaceValueByNotEquals(Map<K, V> source, Map<K, V> replace, BiConsumer<K, V> addCallback, BiConsumer<K, V[]> changeCallback, BiConsumer<K, V> removeCallback) {
        if(source == null) {
            if(isEmpty(replace)) {
                return;
            }
            if(removeCallback != null) {
                replace.forEach(removeCallback);
            }
            return;
        }
        if(isEmpty(replace)) {
            if(addCallback != null) {
                source.forEach(addCallback);
            }
            return;
        }

        Iterator<Map.Entry<K, V>> sourceItr = source.entrySet().iterator();
        while(sourceItr.hasNext()) {
            Map.Entry<K, V> entry = sourceItr.next();
            K k = entry.getKey();
            V v = replace.get(entry.getKey());
            if(v == null) {
                sourceItr.remove();
                if(removeCallback != null) {
                    removeCallback.accept(k, entry.getValue());
                }
                continue;
            }
            V old = entry.getValue();
            if(v.equals(old)) {
                continue;
            }

            entry.setValue(v);
            if(changeCallback != null) {
                changeCallback.accept(k, toArray(old, v));
            }
        }

        for(Map.Entry<K, V> entry : replace.entrySet()) {
            K k = entry.getKey();
            V v = source.get(k);
            if(v == null) {
                source.put(k, entry.getValue());
                if(addCallback != null) {
                    addCallback.accept(k, v);
                }
            }
        }
    }
}
