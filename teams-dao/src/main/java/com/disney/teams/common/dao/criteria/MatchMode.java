package com.disney.teams.common.dao.criteria;


/**
 * 匹配方式枚举类型
 */
public enum MatchMode {
    /**
     * 为空
     */
    NULL,

    /**
     * 不为空
     */
    NOTNULL,

    /**
     * 相等
     */
    EQ,

    /**
     * 不相等
     */
    NE,

    /**
     * 模糊匹配
     */
    LIKE,

    /**
     * 字符串头部精确匹配，尾部模糊匹配
     */
    LIKESTART,

    /**
     * 字符串头部模糊匹配，尾部精确匹配
     */
    LIKEEND,

    /**
     * 忽略大小写模糊匹配
     */
    ILIKE,

    /**
     * 忽略大小写尾部模糊匹配
     */
    ILIKESTART,

    /**
     * 忽略大小写头部模糊匹配
     */
    ILIKEEND,

    /**
     * 大于
     */
    GT,

    /**
     * 大于等于
     */
    GTE,

    /**
     * 小于
     */
    LT,

    /**
     * 小于等于
     */
    LTE,

    /**
     * 集合或数组中存在
     */
    IN,

    /**
     * 集合或数组中不存在
     */
    NOTIN,

    /**
     * 正则匹配，关系数据库不支持
     */
    REGEX,

    /**
     * 分词匹配，目前仅es有实现
     */
    MATCH,

    /**
     * 是否在指定范围内
     */
    BETWEEN;

    /*
    EMPTY, NOTEMPTY, BETWEEN, OR;
    */
    public static MatchMode fromString(String matchMode) {
        return isEmpty(matchMode) ? EQ : MatchMode.valueOf(matchMode.toUpperCase());
    }

    public static boolean isEmpty(String text) {
        if (text != null && text.length() != 0) {
            int i, len = text.length();
            for (i = 0; i < len; ++i) {
                if (!Character.isWhitespace(text.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }
}
