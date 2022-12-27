package com.disney.teams.utils;

import com.disney.teams.utils.type.ArrayUtils;
import com.disney.teams.utils.type.CollectionUtils;
import com.disney.teams.utils.type.MethodUtils;
import com.disney.teams.utils.type.StringUtils;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author arron.zhou
 * @version 1.0.0
 * @date 2022/12/17
 * Description:
 * Modification  History:
 * Date         Author        Version        Description
 * ------------------------------------------------------
 * 2022/12/17       arron.zhou      1.0.0          create
 */
public abstract class ScriptUtils {

    public static String replaceText(String text, String key, Object value) {
        return replaceText(text, new char[]{'$', '{'}, new char[]{'}'}, key, value);
    }

    /**
     * 将文本中，被${}包围的参数替换成指定数值，如果对应的参数数据不存在，则不替换
     *
     * @param text  文本
     * @param value 参数名称对应数值的Map
     * @return
     * @see #replaceText(String, char[], char[], Map)
     */
    public static String replaceText(String text, Map<String, ?> value) {
        return replaceText(text, new char[]{'$', '{'}, new char[]{'}'}, value);
    }

    public static String replaceText(String text, Function<String, String> nameToValue) {
        return replaceText(text, new char[]{'$', '{'}, new char[]{'}'}, nameToValue);
    }

    public static String replaceText(String text, char start, char end, String key, Object value) {
        return replaceText(text, new char[]{start}, new char[]{end}, key, value);
    }

    /**
     * 替换字符串中的特定字符串<br>
     * 如text="AAA${_descr}BBB",start="${",end="}",value={"_descr":"正常"}
     * 替换后字符串为   "AAA正常BBB"
     *
     * @param text  需要替换的字符串
     * @param start 标记字符串的头
     * @param end   标记字符串的头
     * @param value 标记字符串对应的值
     * @return TODO
     */
    public static String replaceText(String text, char start, char end, Map<String, ?> value) {
        return replaceText(text, new char[]{start}, new char[]{end}, value);
    }

    /**
     * 替换字符串中的特定字符串
     * 如text="AAA${_descr}BBB",start="${",end="}",value={"_descr":"正常"}
     * 替换后字符串为   "AAA正常BBB"
     * 替换后字符串为AAA正常BBB
     *
     * @param text  需要替换的字符串
     * @param start 标记字符串的头
     * @param end   标记字符串的头
     * @param value 标记字符串对应的值
     * @return TODO
     */
    public static String replaceText(String text, String start, String end, String key, Object value) {
        if (start == null || end == null) {
            return text;
        }
        Map<String, Object> valueMap = new HashMap<>(1);
        valueMap.put(key, value);
        return replaceText(text, start.toCharArray(), end.toCharArray(), valueMap);
    }

    private static String format(Object value, DecimalFormat decimalFormat) {
        if (value == null) {
            return "";
        }
        if (value instanceof Number) {
            if (decimalFormat != null) {
                return decimalFormat.format(value);
            }
        }
        return value.toString();
    }

    public static String replaceText(String text, char[] start, char[] end, String key, Object value) {
        Map<String, Object> valueMap = new HashMap<>(1);
        valueMap.put(key, value);
        return replaceText(text, start, end, valueMap);
    }

    public static int findEndWithoutEscape(String text, int start, char c) {
        if (text == null || start >= text.length()) {
            return -1;
        }
        if (text.charAt(start) == c) {
            return start;
        }
        int i = start;
        while ((i = text.indexOf(c, ++i)) > 0) {
            int k = i;
            while (--k > -1 && text.charAt(k) == '\\') ;
            //前面有偶数个'\'表示字符没有转义
            if (((i - k) & 1) == 1) {
                return i;
            }
        }
        return -1;
    }

    public static Object eval(String script, Map<String, ?> paramMap) {
        int left = script.indexOf('(');
        if (left < 1) {
            return paramMap.get(script);
        }
        int right = script.lastIndexOf(')');
        if (right < left) {
            return paramMap.get(script);
        }
        int methodDot = script.lastIndexOf('.', left);
        if (methodDot < 1) {
            return paramMap.get(script);
        }
        String className;
        int packageDot = script.lastIndexOf('.', methodDot - 1);
        if (packageDot < 1) {
            className = ("java.lang." + script.substring(packageDot == 0 ? 1 : 0, methodDot));
        } else {
            className = script.substring(0, methodDot);
        }
        Class<?> clz;
        try {
            clz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            return paramMap.get(script);
        }

        String methodName = script.substring(methodDot + 1, left);

        String params = script.substring(left + 1, right);
        Object[] paramValues;
        if (StringUtils.isBlank(params)) {
            paramValues = new Object[0];
        } else {
            List<Object> objs = new ArrayList<>();
            int start = 0, len = params.length();
            while (start < len) {
                if (Character.isWhitespace(params.charAt(start))) {
                    start++;
                    continue;
                }
                if (params.charAt(start) == '"') {
                    int end = findEndWithoutEscape(params, start + 1, '"');
                    if (end > start) {
                        objs.add(params.substring(start + 1, end).replace("\\\"", "\"").replace("\\\\", "\\"));
                        start = params.indexOf(',', end + 1);
                        start = (start < 0) ? len : start + 1;
                        continue;
                    }
                }

                int end = params.indexOf(',', start + 1);
                if (end < 0) {
                    objs.add(paramMap.get(params.substring(start).trim()));
                    start = len;
                } else {
                    objs.add(paramMap.get(params.substring(start, end).trim()));
                    start = end + 1;
                }
            }
            paramValues = objs.toArray();
        }
        List<Method> methods = MethodUtils.getPublicMethods(clz, methodName, Boolean.TRUE);
        if (CollectionUtils.isEmpty(methods)) {
            return paramMap.get(script);
        }

        Method method = MethodUtils.filterSimilarestByParams(methods, clz, paramValues);
        if (method != null) {
            return MethodUtils.invokeStaticMethod(method, paramValues);
        }

        List<Method> sizeMatchMethods = MethodUtils.filterBySize(methods, ArrayUtils.length(paramValues));
        if (CollectionUtils.isNotEmpty(sizeMatchMethods)) {
            return MethodUtils.invokeStaticMethod(sizeMatchMethods.get(0), paramValues);
        }
        return MethodUtils.invokeStaticMethod(methods.get(0), paramValues);
    }

    /**
     * 替换字符串中的特定字符串<br>
     * 如  text="AAA${_descr}BBB",start="${",end="}",value={"_descr":"正常"}
     * 替换后字符串为   "AAA正常BBB"
     *
     * @param text  需要替换的字符串
     * @param start 标记字符串的头
     * @param end   标记字符串的头
     * @param value 标记字符串对应的值
     * @return TODO
     */
    public static String replaceText(String text, char[] start, char[] end, Map<String, ?> value) {
        if (value == null) {
            return text;
        }

        return replaceText(text, start, end, name -> {
            Object val = eval(name, value);
            return val == null ? "" : val.toString();
        });
    }

    public static String replaceText(String text, char[] start, char[] end, Function<String, String> nameToValue) {
        if (nameToValue == null) {
            return text;
        }

        int slen = start.length, elen = end.length;
        if (text == null || text.length() < slen + elen) {
            return text;
        }
        StringBuilder sb = null;
        int i = 0, j = 0, k = 0, offset = 0, len = text.length(), mark = -1;
        while (i < len) {
            char c = text.charAt(i);
            if (mark > -1) {
                if (end[k] == c) {
                    if (++k == elen) {
                        String param = text.substring(mark + slen, i - elen + 1);// 字段
                        String v = nameToValue.apply(param);
                        if (v != null) {
                            if (sb == null) {
                                sb = new StringBuilder(text);
                            }
                            sb.replace(mark + offset, i + 1 + offset, v);
                            int dis = i - mark + 1;// 所替换字符的长度
                            offset += v.length() - dis;// 替换后字符的偏移量
                        }
                        mark = -1;
                        k = 0;
                    }
                } else {
                    k = (end[0] == c ? 1 : 0);
                }
            } else {
                if (start[j] == c) {
                    if (++j == slen) {
                        mark = i - j + 1;
                        j = 0;
                    }
                } else {
                    if (j > 0) {
                        j = (start[0] == c ? 1 : 0);
                    }
                }
            }
            i++;
        }
        return sb == null ? text : sb.toString();
    }

}
