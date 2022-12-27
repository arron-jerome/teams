package com.disney.teams.common.dao.criteria;



import com.disney.teams.common.dao.anno.NoQuery;
import com.disney.teams.common.dao.anno.Query;
import com.disney.teams.model.pagination.Pager;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * 数据库查询条件抽象父类
 *
 * @param <PO>
 */
public class GenericCriteria<PO> implements Serializable {

    private static final long serialVersionUID = 3315957722583921810L;

    /**
     * 此集合的条件将排在子类字段定义的查询条件之前
     */
    private List<Condition> conditions = new ArrayList<>();

    /**
     * 页号
     */
    @NoQuery
    private int pageNo;

    /**
     * 页大小
     */
    @NoQuery
    private int pageSize;

    private boolean pageSizeLimit = true;

    /**
     * 标准的sql语法<code>order by</code><br>
     * 单个排序写法<strong>排序字段名+空格+DESC或ASC<strong><br>
     * 其中ASC和DESC不区分大小写<br>
     * 排序字段名后不带ASC或DESC，默认为ASC排序<br>
     * 多个排序字段以英文逗号','隔开<br>
     * 样例<code>createDate,id DESC,status DESC</code>
     */
    @NoQuery
    private String orderBy;

    /**
     * 返回数据仅包含下列字段
     */
    private String[] includeProperties;

    public GenericCriteria() {
    }

    public GenericCriteria(String name, Object value) {
        this();
        eq(name, value);
    }

    public GenericCriteria(Map<String, Object> eqMap) {
        this();
        eq(eqMap);
    }


    private static void fillOrder(LinkedHashMap<String, Direction> map, String fieldOrder) {
        int gapIndex = fieldOrder.indexOf(' ');
        if (gapIndex < 0) {
            map.put(fieldOrder, Direction.ASC);
        } else {
            String field = fieldOrder.substring(0, gapIndex);
            String orderType = fieldOrder.substring(gapIndex + 1);
            if ("DESC".equalsIgnoreCase(orderType)) {
                map.put(field, Direction.DESC);
            } else {
                map.put(field, Direction.ASC);
            }
        }
    }

    public static LinkedHashMap<String, Direction> orderBy(String orderByStr) {
        LinkedHashMap<String, Direction> map = new LinkedHashMap<>();
        if (orderByStr == null) {
            return map;
        }
        final String orderBy = orderByStr.replaceAll("\\s+", " ");
        int previous = 0;
        int index, len = orderBy.length();
        while ((index = orderBy.indexOf(',', previous)) > -1) {
            String fieldOrder = orderBy.substring(previous, index).trim();
            if (!fieldOrder.isEmpty()) {
                fillOrder(map, fieldOrder);
            }
            previous = index + 1;
        }
        String fieldOrder = orderBy.substring(previous, len).trim();
        if (!fieldOrder.isEmpty()) {
            fillOrder(map, fieldOrder);
        }
        return map;
    }

    public LinkedHashMap<String, Direction> orderBy() {
        return orderBy(this.orderBy);
    }

    public Pager getPager() {
        return new Pager(pageNo, pageSize);
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }

    public GenericCriteria<PO> eq(String name, Object value) {
        conditions.add(new Condition(name, value, MatchMode.EQ));
        return this;
    }

    public GenericCriteria<PO> eq(Map<String, Object> eqMap) {
        if (eqMap == null) {
            return this;
        }
        for (Map.Entry<String, Object> entry : eqMap.entrySet()) {
            eq(entry.getKey(), entry.getValue());
        }
        return this;
    }

    public GenericCriteria<PO> ne(String name, Object value) {
        return add(name, value, MatchMode.NE);
    }

    public GenericCriteria<PO> gt(String name, Object value) {
        return add(name, value, MatchMode.GT);
    }

    public GenericCriteria<PO> gte(String name, Object value) {
        return add(name, value, MatchMode.GTE);
    }

    public GenericCriteria<PO> lt(String name, Object value) {
        return add(name, value, MatchMode.LT);
    }

    public GenericCriteria<PO> lte(String name, Object value) {
        return add(name, value, MatchMode.LTE);
    }

    /**
     * @param name
     * @param min
     * @param max
     * @return
     * @see #between(String, Object, Object, Class)
     */
    @Deprecated
    public GenericCriteria<PO> between(String name, Object min, Object max) {
        Class clazz = null;
        if (min != null) {
            clazz = min.getClass();
        } else if (max != null) {
            clazz = max.getClass();
        }

        if (clazz == null) {
            return add(name, new Object[]{min, max}, MatchMode.BETWEEN);
        } else {
            return between(name, min, max, clazz);
        }
    }

    /**
     * @param name
     * @param min
     * @param max
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> GenericCriteria<PO> between(String name, T min, T max, Class<T> clazz) {
        T[] array = (T[]) Array.newInstance(clazz, 2);
        array[0] = min;
        array[1] = max;
        return add(name, array, MatchMode.BETWEEN);
    }

    public GenericCriteria<PO> in(String name, Object value) {
        return add(name, value, MatchMode.IN);
    }

    public GenericCriteria<PO> notIn(String name, Object value) {
        return add(name, value, MatchMode.NOTIN);
    }

    public GenericCriteria<PO> isNull(String name) {
        return add(name, null, MatchMode.NULL);
    }

    public GenericCriteria<PO> notNull(String name) {
        return add(name, null, MatchMode.NOTNULL);
    }

    public GenericCriteria<PO> like(String name, Object value) {
        return add(name, value, MatchMode.LIKE);
    }

    public GenericCriteria<PO> likeStart(String name, Object value) {
        return add(name, value, MatchMode.LIKESTART);
    }

    public GenericCriteria<PO> likeEnd(String name, Object value) {
        return add(name, value, MatchMode.LIKEEND);
    }

    public GenericCriteria<PO> iLike(String name, Object value) {
        return add(name, value, MatchMode.ILIKE);
    }

    public GenericCriteria<PO> iLikeStart(String name, Object value) {
        return add(name, value, MatchMode.ILIKESTART);
    }

    public GenericCriteria<PO> iLikeEnd(String name, Object value) {
        return add(name, value, MatchMode.ILIKEEND);
    }

    public GenericCriteria<PO> match(String name, Object value) {
        return add(name, value, MatchMode.MATCH);
    }

    public GenericCriteria<PO> regex(String name, String value) {
        return add(name, value, MatchMode.REGEX);
    }

    public GenericCriteria<PO> add(String name, Object value, MatchMode matchMode) {
        conditions.add(new Condition(name, value, matchMode));
        return this;
    }

    public GenericCriteria<PO> add(Condition condition) {
        if (condition == null) {
            return this;
        }
        conditions.add(condition);
        return this;
    }

    public List<Condition> conditions() {
        return conditions;
    }

    public GenericCriteria<PO> conditions(List<Condition> conditions) {
        this.conditions = conditions;
        return this;
    }

    public static final boolean isValid(Condition c) {
        if (c == null) {
            return false;
        }
        MatchMode mode = c.matchMode();
        if (mode == null) {
            return true;
        }
        switch (mode) {
            case ILIKE:
            case LIKE:
            case LIKESTART:
            case LIKEEND:
                return !GenericCriteria.isBlank(c.value());
            default:
                return true;
        }
    }

    public List<Condition> conditionsWithFields() {
        List<Condition> conditionList = new ArrayList<>(conditions.size());
        for (Condition c : conditions) {
            if (isValid(c)) {
                conditionList.add(c.clone());
            }
        }
        if (GenericCriteria.class.equals(getClass())) {
            return conditionList;
        }
        Class<?> clz = getClass();
        while (!GenericCriteria.class.equals(clz)) {
            Field[] fields = clz.getDeclaredFields();
            try {
                for (Field field : fields) {
                    if (!Modifier.isStatic(field.getModifiers())) {
                        NoQuery nq = field.getAnnotation(NoQuery.class);
                        if (nq != null) {
                            continue;
                        }
                        field.setAccessible(true);
                        Query q = field.getAnnotation(Query.class);
                        MatchMode mode = (q == null ? MatchMode.EQ : q.matchMode());
                        Object value = field.get(this);
                        //忽略空值查询条件
//                        if((q == null || q.ignoreNull()) &&  mode != MatchMode.NULL && mode != MatchMode.NOTNULL && value == null){
                        if ((q == null || q.ignoreNull()) && value == null) {
                            continue;
                        }
                        String f = (q == null || isBlank(q.field()) ? field.getName() : q.field());
                        switch (mode) {
                            case NULL:
                                if (Boolean.FALSE.equals(value)) {
                                    mode = MatchMode.NOTNULL;
                                }
                                value = null;
                                break;
                            case NOTNULL:
                                if (Boolean.FALSE.equals(value)) {
                                    mode = MatchMode.NULL;
                                }
                                value = null;
                                break;
                            default:
                                break;
                        }
                        Condition condition = Condition.make(f, value, mode);
                        if (isValid(condition)) {
                            conditionList.add(condition);
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            clz = clz.getSuperclass();
        }
        return conditionList;
    }

    public void clearConditions() {
        if (conditions != null) {
            conditions.clear();
        }
    }

    public void removeCondition(String name, Object value, MatchMode matchMode) {
        if (conditions == null || conditions.isEmpty()) {
            return;
        }
        Iterator<Condition> itr = conditions.iterator();
        while (itr.hasNext()) {
            Condition c = itr.next();
            if (c == null || c.equals(name, value, matchMode)) {
                itr.remove();
            }
        }
    }

    public void removeCondition(Condition condition) {
        if (condition == null) {
            return;
        }
        removeCondition(condition.getName(), condition.getValue(), condition.getMatchMode());
    }

    public GenericCriteria<PO> criteria() {
        if (GenericCriteria.class.equals(getClass())) {
            return this;
        }
        List<Condition> conditions = conditionsWithFields();
        GenericCriteria<PO> gc = new GenericCriteria<>();
        gc.conditions = conditions;
        gc.orderBy = this.orderBy;
        gc.pageNo = this.pageNo;
        gc.pageSize = this.pageSize;
        gc.includeProperties = includeProperties;
        return gc;
    }

    public String[] getIncludeProperties() {
        return includeProperties;
    }

    public void setIncludeProperties(String[] includeProperties) {
        this.includeProperties = includeProperties;
    }

    public void includeProperties(String... includeProperties) {
        this.includeProperties = includeProperties;
    }

    /**
     * 判断字符串是否为空
     *
     * @return
     */
    public static boolean isBlank(Object val) {
        if (val == null) {
            return true;
        }
        String text;
        if (val instanceof String) {
            text = (String) val;
        } else {
            return false;
        }
        if (text.length() == 0) {
            return true;
        } else {
            int i, len = text.length();
            for (i = 0; i < len; ++i) {
                if (Character.isWhitespace(text.charAt(i)) == false) {
                    return false;
                }
            }
            return true;
        }
    }

    public boolean pageSizeLimit() {
        return pageSizeLimit;
    }

    public GenericCriteria pageSizeLimit(boolean pageSizeLimit) {
        this.pageSizeLimit = pageSizeLimit;
        return this;
    }
}
