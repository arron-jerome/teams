package com.disney.teams.utils.type;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author arron.zhou
 * @version 1.0.0
 * @date 2022/12/16
 * Modification  History:
 * Date         Author        Version        Description
 * ------------------------------------------------------
 * 2022/12/16       arron.zhou      1.0.0          create
 */
public abstract class CollectionUtils {

    private static final Map<Class<?>, Constructor<?>> defaultInstance = new HashMap<>();

    static {
        try {
            defaultInstance.put(Collection.class, ArrayList.class.getConstructor());
            defaultInstance.put(List.class, ArrayList.class.getConstructor());
            defaultInstance.put(Set.class, HashSet.class.getConstructor());
            defaultInstance.put(SortedSet.class, TreeSet.class.getConstructor());
            defaultInstance.put(NavigableSet.class, TreeSet.class.getConstructor());
            defaultInstance.put(Queue.class, LinkedList.class.getConstructor());
            defaultInstance.put(BlockingQueue.class, LinkedBlockingQueue.class.getConstructor());
            defaultInstance.put(TransferQueue.class, LinkedTransferQueue.class.getConstructor());
            defaultInstance.put(Deque.class, LinkedList.class.getConstructor());
            defaultInstance.put(BlockingDeque.class, LinkedBlockingDeque.class.getConstructor());
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        for (Constructor<?> constructor : defaultInstance.values()) {
            if (!MemberUtils.isPublic(constructor)) {
                throw new RuntimeException(constructor + " is not accessible");
            }
        }
    }

    private CollectionUtils() {
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static <T> boolean in(T value, Collection<T> values) {
        if (isEmpty(values)) {
            return false;
        }
        for (Object v : values) {
            if (ObjectUtils.eq(value, v)) {
                return true;
            }
        }
        return false;
    }

    public static <T> boolean notIn(T value, Collection<T> values) {
        return !in(value, values);
    }

    public static void clear(Collection<?> collection) {
        if (collection != null) {
            collection.clear();
        }
    }

    public static <T> T[] toArray(Collection<T> collection) {
        return ArrayUtils.toArray(collection);
    }

    public static <T> T[] toArray(Collection<T> collection, Class<T> clazz) {
        return ArrayUtils.toArray(collection, clazz);
    }

    public static <T> String toString(Collection<T> values) {
        return toString(values, "");
    }

    public static <T> String toString(Collection<T> values, char gap) {
        if (isEmpty(values)) {
            return "";
        }
        Iterator<T> itr = values.iterator();
        if (values.size() == 1) {
            return String.valueOf(itr.next());
        }
        StringBuilder sb = new StringBuilder();
        sb.append(itr.next());
        while (itr.hasNext()) {
            sb.append(gap).append(itr.next());
        }
        return sb.toString();
    }

    public static <T> String toString(Collection<T> values, String gap) {
        if (isEmpty(values)) {
            return "";
        }
        Iterator<T> itr = values.iterator();
        if (values.size() == 1) {
            return String.valueOf(itr.next());
        }
        StringBuilder sb = new StringBuilder();
        sb.append(itr.next());
        while (itr.hasNext()) {
            sb.append(gap).append(itr.next());
        }
        return sb.toString();
    }

    private static <T extends Collection<?>> T newInstance(Class<T> targetClass) {
        try {
            Constructor<?> constructor = defaultInstance.get(targetClass == null ? Collection.class : targetClass);
            if (constructor == null) {
                constructor = targetClass.getConstructor();
            }
            Object target = constructor.newInstance();
            return (T) target;
        } catch (Exception e) {
            throw ExceptionUtils.uncheck(e);
        }
    }

    public static <T extends Collection<Q>, Q> T convert(Collection<Q> src, Class<T> targetClass) {
        //如果实现类是当前的子类，则直接返回
        if (targetClass == null || targetClass.isAssignableFrom(src.getClass())) {
            return (T) src;
        }
        T target = newInstance(targetClass);
        target.addAll(src);
        return target;
    }

    public static <T extends Collection<Q>, Q> T convert(Object src, Class<T> targetClass, Type targetComponetType) {
        if (targetComponetType == null) {
            targetComponetType = ClassUtils.getGenericType(targetClass, Collection.class, 0);
        }
        if (src == null) {
            return null;
        } else if (src.getClass().isArray()) {
            T target = newInstance(targetClass);
            for (int i = 0, len = Array.getLength(src); i < len; ++i) {
                Q value = TypeUtils.convert(Array.get(src, i), targetComponetType);
                target.add(value);
            }
            return target;
        } else if (src instanceof Iterable) {
            Iterable<?> srcColl = (Iterable<?>) src;
            T target = newInstance(targetClass);
            Iterator<?> itr = srcColl.iterator();
            while (itr.hasNext()) {
                Q value = TypeUtils.convert(itr.next(), targetComponetType);
                target.add(value);
            }
            return target;
        } else {
            throw new UnsupportedOperationException("Couldn't convert " + src.getClass() + " to " + targetClass);
        }
    }

    public static List arrayToList(Object source) {
        return Arrays.asList(ObjectUtils.toObjectArray(source));
    }

    public static <E> void mergeArrayIntoCollection(Object array, Collection<E> collection) {
        if (collection == null) {
            throw new IllegalArgumentException("Collection must not be null");
        } else {
            Object[] arr = ObjectUtils.toObjectArray(array);
            Object[] var3 = arr;
            int var4 = arr.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                Object elem = var3[var5];
                collection.add((E) elem);
            }
        }
    }

    public static <K, V> void mergePropertiesIntoMap(Properties props, Map<K, V> map) {
        if (map == null) {
            throw new IllegalArgumentException("Map must not be null");
        } else {
            String key;
            Object value;
            if (props != null) {
                for (Enumeration en = props.propertyNames(); en.hasMoreElements(); map.put((K) key, (V) value)) {
                    key = (String) en.nextElement();
                    value = props.getProperty(key);
                    if (value == null) {
                        value = props.get(key);
                    }
                }
            }

        }
    }

    public static boolean contains(Iterable<?> iterable, Object element) {
        return iterable != null && contains(iterable.iterator(), element);
    }

    public static boolean contains(Iterator<?> iterator, Object element) {
        if (iterator != null) {
            while (iterator.hasNext()) {
                Object candidate = iterator.next();
                if (ObjectUtils.nullSafeEquals(candidate, element)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean contains(Enumeration<?> enumeration, Object element) {
        if (enumeration != null) {
            while (enumeration.hasMoreElements()) {
                Object candidate = enumeration.nextElement();
                if (ObjectUtils.nullSafeEquals(candidate, element)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean containsInstance(Collection<?> collection, Object element) {
        if (collection != null) {
            Iterator var2 = collection.iterator();

            while (var2.hasNext()) {
                Object candidate = var2.next();
                if (candidate == element) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean containsAny(Collection<?> source, Collection<?> candidates) {
        if (!isEmpty(source) && !isEmpty(candidates)) {
            Iterator var2 = candidates.iterator();

            Object candidate;
            do {
                if (!var2.hasNext()) {
                    return false;
                }

                candidate = var2.next();
            } while (!source.contains(candidate));

            return true;
        } else {
            return false;
        }
    }

    public static <E> E findFirstMatch(Collection<?> source, Collection<E> candidates) {
        if (!isEmpty(source) && !isEmpty(candidates)) {
            Iterator<E> var2 = candidates.iterator();
            E candidate;
            do {
                if (!var2.hasNext()) {
                    return null;
                }
                candidate = var2.next();
            } while (!source.contains(candidate));
            return candidate;
        } else {
            return null;
        }
    }

    public static <T> T findValueOfType(Collection<?> collection, Class<T> type) {
        if (isEmpty(collection)) {
            return null;
        } else {
            Object value = null;
            Iterator var3 = collection.iterator();

            while (true) {
                Object element;
                do {
                    if (!var3.hasNext()) {
                        return (T) value;
                    }

                    element = var3.next();
                } while (type != null && !type.isInstance(element));

                if (value != null) {
                    return null;
                }

                value = element;
            }
        }
    }

    public static Object findValueOfType(Collection<?> collection, Class<?>[] types) {
        if (isNotEmpty(collection) && ArrayUtils.isNotEmpty(types)) {
            Class[] var2 = types;
            int var3 = types.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                Class type = var2[var4];
                Object value = findValueOfType(collection, type);
                if (value != null) {
                    return value;
                }
            }

            return null;
        } else {
            return null;
        }
    }

    public static boolean hasUniqueObject(Collection<?> collection) {
        if (isEmpty(collection)) {
            return false;
        } else {
            boolean hasCandidate = false;
            Object candidate = null;
            Iterator var3 = collection.iterator();

            while (var3.hasNext()) {
                Object elem = var3.next();
                if (!hasCandidate) {
                    hasCandidate = true;
                    candidate = elem;
                } else if (candidate != elem) {
                    return false;
                }
            }

            return true;
        }
    }

    public static Class<?> findCommonElementType(Collection<?> collection) {
        if (isEmpty(collection)) {
            return null;
        } else {
            Class candidate = null;
            Iterator var2 = collection.iterator();

            while (var2.hasNext()) {
                Object val = var2.next();
                if (val != null) {
                    if (candidate == null) {
                        candidate = val.getClass();
                    } else if (candidate != val.getClass()) {
                        return null;
                    }
                }
            }

            return candidate;
        }
    }

    public static <A, E extends A> A[] toArray(Enumeration<E> enumeration, A[] array) {
        ArrayList elements = new ArrayList();

        while (enumeration.hasMoreElements()) {
            elements.add(enumeration.nextElement());
        }

        return (A[]) elements.toArray(array);
    }

    public static <E> Iterator<E> toIterator(Enumeration<E> enumeration) {
        return new EnumerationIterator(enumeration);
    }

    public static int size(Collection<?> allFields) {
        return allFields == null ? 0 : allFields.size();
    }

    private static class EnumerationIterator<E> implements Iterator<E> {
        private final Enumeration<E> enumeration;

        public EnumerationIterator(Enumeration<E> enumeration) {
            this.enumeration = enumeration;
        }

        public boolean hasNext() {
            return this.enumeration.hasMoreElements();
        }

        public E next() {
            return this.enumeration.nextElement();
        }

        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Not supported");
        }
    }

    /**
     * 合并集合
     *
     * @param src
     * @param from
     * @param <T>
     * @return 如果src不为空，则返回的集合为src
     */
    public static <T> Collection<T> merge(Collection<T> src, Collection<T> from) {
        if (src == null) {
            return from;
        }
        if (isEmpty(from)) {
            return src;
        }
        src.addAll(from);
        return src;
    }

    /**
     * 合并到新的集合中，如果有必要
     *
     * @param a
     * @param b
     * @param <T>
     * @return 如果src不为空，则返回的集合为src
     */
    public static <T> Collection<T> mergeToNewIfNecessary(Collection<T> a, Collection<T> b) {
        if (a == null) {
            return b;
        } else if (b == null) {
            return a;
        } else {
            List<T> n = new ArrayList<>(a.size() + b.size());
            n.addAll(a);
            n.addAll(b);
            return n;
        }
    }

    private static <K, BEAN> K getValue(BEAN bean, String propertyName) {
        K property;
        if (bean instanceof Map) {
            property = (K) ((Map) bean).get(propertyName);
        } else {
            property = MethodUtils.invokeGetterMethod(bean, propertyName);
        }
        return property;
    }

    public static <K, BEAN> LinkedHashMap<K, List<BEAN>> groupByToValueList(Collection<BEAN> beanList, String propertyName) {
        return groupByToValueList(beanList, bean -> getValue(bean, propertyName));
    }

    public static <K, BEAN> LinkedHashMap<K, List<BEAN>> groupByToValueList(Collection<BEAN> beanList, Function<BEAN, K> keyFunction) {
        return groupByToValueList(beanList, keyFunction, bean -> bean);
    }

    public static <BEAN, K, V> LinkedHashMap<K, List<V>> groupByToValueList(Collection<BEAN> beanList, Function<BEAN, K> keyFunction, Function<BEAN, V> valueFunction) {
        LinkedHashMap<K, List<V>> map = new LinkedHashMap<>();
        if (isEmpty(beanList)) {
            return map;
        }
        for (BEAN bean : beanList) {
            if (bean == null) {
                continue;
            }

            K property = keyFunction.apply(bean);
            List<V> list = map.get(property);
            if (list == null) {
                list = new ArrayList<>();
                map.put(property, list);
            }
            list.add(valueFunction.apply(bean));
        }
        return map;
    }

    public static <K, BEAN> LinkedHashMap<K, BEAN> groupBy(Collection<BEAN> beanList, String propertyName) {
        return groupBy(beanList, bean -> getValue(bean, propertyName));
    }

    public static <K, BEAN> LinkedHashMap<K, BEAN> groupBy(Collection<BEAN> beanList, Function<BEAN, K> keyFunction) {
        LinkedHashMap<K, BEAN> map = new LinkedHashMap<>();
        if (isEmpty(beanList)) {
            return map;
        }
        for (BEAN bean : beanList) {
            if (bean == null) {
                continue;
            }
            map.put(keyFunction.apply(bean), bean);
        }
        return map;
    }

    public static <K, V, BEAN> LinkedHashMap<K, V> groupBy(Collection<BEAN> beanList, Function<BEAN, K> keyFunction, Function<BEAN, V> valueFunction) {
        LinkedHashMap<K, V> map = new LinkedHashMap<>();
        if (isEmpty(beanList)) {
            return map;
        }
        for (BEAN bean : beanList) {
            if (bean == null) {
                continue;
            }
            map.put(keyFunction.apply(bean), valueFunction.apply(bean));
        }
        return map;
    }

    public static <K, BEAN> Collection<K> project(Collection<BEAN> beanList, String propertyName) {
        return project(beanList, bean -> getValue(bean, propertyName));
    }

    public static <K, BEAN> Collection<K> project(Collection<BEAN> beanList, Function<BEAN, K> valueFunction) {
        List<K> list = new ArrayList<>();
        if (isEmpty(beanList)) {
            return list;
        }
        for (BEAN bean : beanList) {
            if (bean == null) {
                continue;
            }
            K property = valueFunction.apply(bean);
            list.add(property);
        }
        return list;
    }

    public static <T> List<T> reverseSelf(List<T> list) {
        if (isEmpty(list)) {
            return list;
        }
        T tmp;
        for (int i = 0, len = list.size(), halfLen = len-- >>> 1; i < halfLen; ++i) {
            tmp = list.get(i);
            list.set(i, list.get(len - i));
            list.set(len - i, tmp);
        }
        return list;
    }

    public static <T> List<T> reverse(List<T> list) {
        if (list == null) {
            return list;
        } else if (list.isEmpty()) {
            return new ArrayList<>();
        }
        List<T> newList = new ArrayList<>(list.size());
        for (int len = list.size() - 1; len > -1; --len) {
            newList.add(list.get(len));
        }
        return newList;
    }

    public static <T> Collection<T> removeNullValue(Collection<T> coll) {
        if (isEmpty(coll)) {
            return coll;
        }
        Iterator<T> itr = coll.iterator();
        while (itr.hasNext()) {
            if (itr.next() == null) {
                itr.remove();
            }
        }
        return coll;
    }

    public static <T> Collection<T> getNotNullValue(Collection<T> coll) {
        Collection<T> list = new ArrayList<>();
        if (isEmpty(coll)) {
            return list;
        }
        for (T d : coll) {
            if (d != null) {
                list.add(d);
            }
        }
        return list;
    }

    public static <T> List<List<T>> groupBySize(List<T> pos, int size) {
        int len = CollectionUtils.size(pos);
        List<List<T>> groupList = new ArrayList<>(len / size + 1);
        if (len < size) {
            groupList.add(pos);
            return groupList;
        }
        List<T> list = new ArrayList<>(size);
        for (int i = 0; i < pos.size(); i++) {
            list.add(pos.get(i));
            if (list.size() == size) {
                groupList.add(list);
                list = new ArrayList<>();
            }
        }

        if (!list.isEmpty()) {
            groupList.add(list);
        }
        return groupList;
    }

    public static <T> T first(Iterable<T> iterable) {
        if (iterable == null) {
            return null;
        }
        Iterator<T> itr = iterable.iterator();
        return itr.hasNext() ? itr.next() : null;
    }

    public static <T> List<T> subList(List<T> list, int start, int end) {
        if (isEmpty(list)) {
            return list;
        }
        int size = list.size();
        if (end > size) {
            end = size;
        }

        //如果子链表是整链表，则直接返回原链接
        if (start == 0 && end == size) {
            return list;
        }

        if (start >= end) {
            return new ArrayList<>();
        }
        List<T> rs = new ArrayList<>(end - start);
        for (int i = start; i < end; ++i) {
            rs.add(list.get(i));
        }
        return rs;
    }

    private static <T extends Comparable<T>> T maxByNegtive(Collection<T> collection, int negtive) {
        if (isEmpty(collection)) {
            return null;
        }
        Iterator<T> itr = collection.iterator();
        T max = itr.next();
        while (itr.hasNext()) {
            T data = itr.next();
            if (data == null) {
                continue;
            } else if (max == null || max.compareTo(data) * negtive < 0) {
                max = data;
            }
        }
        return max;
    }

    public static <T extends Comparable<T>> T max(Collection<T> collection) {
        return maxByNegtive(collection, 1);
    }

    public static <T extends Comparable<T>> T min(Collection<T> collection) {
        return maxByNegtive(collection, -1);
    }

    public static <T> T get(List<T> list, int i) {
        if (i < 0 || isEmpty(list)) {
            return null;
        }
        if (i >= list.size()) {
            return null;
        }
        return list.get(i);
    }

    public static <T> List<T> filter(Collection<T> list, Predicate<T> predicate) {
        if (isEmpty(list)) {
            return new ArrayList<>();
        }
        int size = list.size();
        List<T> filerList = new ArrayList<>(size > 16 ? (size >>> 1) : size);
        for (T one : list) {
            if (predicate.test(one)) {
                filerList.add(one);
            }
        }
        return filerList;
    }

    public static <T> List<T> toList(Collection<T> coll) {
        if (coll == null) {
            return null;
        } else if (coll instanceof List) {
            return (List<T>) coll;
        } else {
            return new ArrayList<>(coll);
        }
    }

    public static boolean eq(Collection<?> src, Collection<?> dest) {
        if (src == null) {
            return dest == null;
        }
        if (dest == null) {
            return false;
        }

        if (src.size() != dest.size()) {
            return false;
        }

        Iterator<?> itr = src.iterator();
        Iterator<?> destItr = dest.iterator();
        while (itr.hasNext()) {
            Object s = itr.next();
            Object d = destItr.next();
            if (ObjectUtils.ne(s, d)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断两个集合中的元素是否相等，忽略顺序
     *
     * @param src
     * @param dest
     * @return
     */
    public static boolean eqIgSep(Collection<?> src, Collection<?> dest) {
        if (src == null || src.isEmpty()) {
            return dest == null || dest.isEmpty();
        }
        if (dest == null || dest.isEmpty()) {
            return false;
        }

        if (src.size() != dest.size()) {
            return false;
        }

        List<?> news = new LinkedList<>(src);
        for (Object d : dest) {
            Iterator<?> itr = news.iterator();
            boolean contains = false;
            while (itr.hasNext()) {
                Object s = itr.next();
                if (s.equals(d)) {
                    itr.remove();
                    contains = true;
                    break;
                }
            }
            if (!contains) {
                return false;
            }
        }
        return true;
    }

}

