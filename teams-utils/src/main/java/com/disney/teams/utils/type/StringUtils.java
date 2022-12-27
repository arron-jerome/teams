package com.disney.teams.utils.type;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;

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
public abstract class StringUtils {

    private static Pattern SCRIPT_PATTERN = Pattern.compile("<script.*>.*<\\/script\\s*> |.*[<>\"';+].*");

    private static final String FOLDER_SEPARATOR = "/";
    private static final String WINDOWS_FOLDER_SEPARATOR = "\\";
    private static final String TOP_PATH = "..";
    private static final String CURRENT_PATH = ".";
    private static final char EXTENSION_SEPARATOR = '.';

    public static final String EMPTY = "";

    /**
     * QQ账号正则表达式，首位不为0，4至12位数字
     */
    public static final String QQ_NO_REGEX = "[1-9][0-9]{3,12}";

    /**
     * 手机号码正则表达式，以1开头的11位数字
     */
    public static final String PHONE_NO_REGEX = "1[0-9]{10}";

    private StringUtils(){}
    /**
     * 判断字符串中包含指定字符的个数
     * @param text
     * @param chr
     * @return
     */
    public static int containCount(String text, char chr){
        int count = 0;
        if(isEmpty(text)){
            return count;
        }
        for(int i = 0, len = text.length(); i < len; ++i){
            if(text.charAt(i) == chr){
                ++count;
            }
        }
        return count;
    }

    /**
     * 判断字符串中包含指定字符的个数
     * @param text
     * @param str
     * @return
     */
    public static int containCount(String text, String str){
        int count = 0;
        if(isEmpty(text) || isEmpty(str)){
            return count;
        }
        int index = 0;
        int strLen = str.length();
        while((index = text.indexOf(str, index)) >= 0){
            ++count;
            index += strLen;
        }
        return count;
    }

    public static boolean contains(String text, String str){
        if(str == null || text == null){
            return false;
        }
        return text.contains(str);
    }

    public static int length(CharSequence text) {
        return text == null ? 0 : text.length();
    }

    public static int byteLength(CharSequence text) {
        int len = length(text);
        if(len == 0) {
            return len;
        }
        for(int i = 0, tlen = len; i < tlen; ++i) {
            char c = text.charAt(i);
            if(c > 128) {
                ++len;
            }
        }
        return len;
    }

    /**
     * 判断字符串是否为空
     *
     * @param val
     * @return
     */
    public static boolean isEmpty(Object val) {
        if (val == null) {
            return true;
        }
        if (val instanceof CharSequence) {
            return isEmpty((CharSequence)val);
        } else {
            return false;
        }
    }

    /**
     * 判断字符串是否不为空
     *
     * @param text
     * @return
     */
    public static boolean isNotEmpty(Object text) {
        return !isEmpty(text);
    }

    /**
     * 判断字符串是否为空
     *
     * @param text
     * @return
     */
    public static boolean isBlank(Object text) {
        if (text == null) {
            return true;
        }
        if (text instanceof CharSequence) {
            return isBlank((CharSequence)text);
        } else {
            return false;
        }
    }
    /**
     * 判断字符串是否不为空
     *
     * @param text
     * @return
     */
    public static boolean isNotBlank(Object text) {
        return !isBlank(text);
    }

    /**
     * 判断字符串是否为数字类型
     *
     * @param text
     * @return
     */
    public static boolean isNumber(String text) {
        if(isBlank(text)){
            return false;
        }
        for(int i = 0, len = text.length(); i < len; ++i){
            char c = text.charAt(i);
            if(c < '0' || c > '9'){
                return false;
            }
        }
        return true;
    }

    /**
     * 获取uuid数值
     *
     * @return
     */
    public static String getUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 将普通文本的字符串转换成html文本格式的字符串
     *
     * @param target
     * @return
     */
    public static String htmlEncode(String target) {
        if (target == null){
            return null;
        }
        StringBuilder sb = new StringBuilder();
        int i, len = target.length();
        for (i = 0; i < len; i++) {
            char c = target.charAt(i);
            switch (c) {
                case 60:
                    sb.append("&lt;");
                    break;
                case 62:
                    sb.append("&gt;");
                    break;
                case 38:
                    sb.append("&amp;");
                    break;
                case 34:
                    sb.append("&quot;");
                    break;
                case 169:
                    sb.append("&copy;");
                    break;
                case 174:
                    sb.append("&reg;");
                    break;
                case 165:
                    sb.append("&yen;");
                    break;
                case 8364:
                    sb.append("&euro;");
                    break;
                case 8482:
                    sb.append("&#153;");
                    break;
                case 13:
                    if (i < len - 1 && target.charAt(i + 1) == 10) {
                        sb.append("<br>");
                        i++;
                    } else {
                        sb.append("<br>");
                    }
                    break;
                case 10:
                    sb.append("<br>");
                    break;
                case 32:
                    if (i < len - 1 && target.charAt(i + 1) == ' ') {
                        sb.append(" &nbsp;");
                        i++;
                        break;
                    }
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }



    public static String replaceText(String text, String key, Object value) {
        return replaceText(text, new char[] {'$', '{'}, new char[] {'}'}, key, value);
    }

    /**
     * 将文本中，被${}包围的参数替换成指定数值，如果对应的参数数据不存在，则不替换
     *
     * @see #replaceText(String, char[], char[], Map)
     * @param text 文本
     * @param value 参数名称对应数值的Map
     * @return
     */
    public static String replaceText(String text, Map<String, ?> value) {
        return replaceText(text, new char[] {'$', '{'}, new char[] {'}'}, value);
    }

    public static String replaceText(String text, Function<String, String> nameToValue) {
        return replaceText(text, new char[] {'$', '{'}, new char[] {'}'}, nameToValue);
    }

    public static String replaceText(String text, Map<String, ?> value, DecimalFormat decimalFormat, DateFormat dateFormat) {
        return replaceText(text, new char[] {'$', '{'}, new char[] {'}'}, value, decimalFormat);
    }


    public static String replaceText(String text, char start, char end, String key, Object value) {
        return replaceText(text, new char[] {start}, new char[] {end}, key, value);
    }

    /**
     * 替换字符串中的特定字符串<br>
     * 如<code>text</code>="AAA${_descr}BBB",<code>start</code>="${",end="}",<code>value</code>=
     * {"_descr":"正常"}<br>
     * 替换后字符串为AAA正常BBB
     *
     * @param text 需要替换的字符串
     * @param start 标记字符串的头
     * @param end 标记字符串的头
     * @param value 标记字符串对应的值
     * @return TODO
     */
    public static String replaceText(String text, char start, char end, Map<String, ?> value) {
        return replaceText(text, new char[] {start}, new char[] {end}, value);
    }

    public static String replaceText(String text, char start, char end, Map<String, ?> value, DecimalFormat decimalFormat) {
        return replaceText(text, new char[]{start}, new char[]{end}, value, decimalFormat);
    }

    /**
     * 替换字符串中的特定字符串<br>
     * 如<code>text</code>="AAA${_descr}BBB",<code>start</code>="${",end="}",<code>value</code>=
     * {"_descr":"正常"}<br>
     * 替换后字符串为AAA正常BBB
     *
     * @param text 需要替换的字符串
     * @param start 标记字符串的头
     * @param end 标记字符串的头
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

    private static String format(Object value, DecimalFormat decimalFormat){
        if(value == null){
            return "";
        }
        if(value instanceof Number){
            if(decimalFormat != null) {
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

    /**
     * 替换字符串中的特定字符串<br>
     * 如<code>text</code>="AAA${_descr}BBB",<code>start</code>="${",end="}",<code>value</code>=
     * {"_descr":"正常"}<br>
     * 替换后字符串为AAA正常BBB
     *
     * @param text 需要替换的字符串
     * @param start 标记字符串的头
     * @param end 标记字符串的头
     * @param value 标记字符串对应的值
     * @return TODO
     */
    public static String replaceText(String text, char[] start, char[] end, Map<String, ?> value) {
        return replaceText(text, start, end, value, null);
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
                            if(sb == null){
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
                    // ---非单词字符重新匹配
                    if ((c < 'a' || c > 'z') && (c < 'A' || c > 'Z') && c != '_'
                        && (c < '0' || c > '9')) {
                        mark = -1;
                        j = (start[0] == c ? 1 : 0);
                    }
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

    public static String replaceText(String text, char[] start, char[] end, Map<String, ?> value, DecimalFormat decimalFormat) {
        if (value == null || value.isEmpty()) {
            return text;
        }
        return replaceText(text, start, end, name -> {
            Object v = value.get(name);
            return format(v, decimalFormat);
        });

//
//        int slen = start.length, elen = end.length;
//        if (text == null || text.length() < slen + elen) {
//            return text;
//        }
//        StringBuilder sb = null;
//        int i = 0, j = 0, k = 0, offset = 0, len = text.length(), mark = -1;
//        while (i < len) {
//            char c = text.charAt(i);
//            if (mark > -1) {
//                if (end[k] == c) {
//                    if (++k == elen) {
//                        String param = text.substring(mark + slen, i - elen + 1);// 字段
//                        Object val = value.get(param);
//                        if (val != null) {
//                            String v = format(val, decimalFormat);
//                            if(sb == null){
//                                sb = new StringBuilder(text);
//                            }
//                            sb.replace(mark + offset, i + 1 + offset, v);
//                            int dis = i - mark + 1;// 所替换字符的长度
//                            offset += v.length() - dis;// 替换后字符的偏移量
//                        }
//                        mark = -1;
//                        k = 0;
//                    }
//                } else {
//                    k = (end[0] == c ? 1 : 0);
//                    // ---非单词字符重新匹配
//                    if ((c < 'a' || c > 'z') && (c < 'A' || c > 'Z') && c != '_'
//                        && (c < '0' || c > '9')) {
//                        mark = -1;
//                        j = (start[0] == c ? 1 : 0);
//                    }
//                }
//            } else {
//                if (start[j] == c) {
//                    if (++j == slen) {
//                        mark = i - j + 1;
//                        j = 0;
//                    }
//                } else {
//                    if (j > 0) {
//                        j = (start[0] == c ? 1 : 0);
//                    }
//                }
//            }
//            i++;
//        }
//        return sb == null ? text : sb.toString();
    }
    /**
     * 字符串中所有大写字母转小写，在前面加个间隔符，字符串中第一个字母如果大写则不加间隔符
     *
     * @example firstUpperToLowerWithFrontGap("userName", ',') = "user_name"
     * @param text 字符串
     * @param gap 间隔符
     * @return
     */
    public static String wordFirstUpperToLower(String text, char gap) {
        if (text == null || text.length() == 0) {
            return text;
        }
        int i, len = text.length();
        StringBuilder sb = new StringBuilder();
        char c = text.charAt(0);
        sb.append(c >= 'A' && c <= 'Z' ? Character.toLowerCase(c) : c);
        for (i = 1; i < len; ++i) {
            c = text.charAt(i);
            if (c >= 'A' && c <= 'Z') {
                sb.append(gap).append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
    /**
     * 字符串编码 默认以UTF-8编码
     *
     * @param str 待编码字符串
     * @param charsetName 编码类型
     * @return 编码后字符串
     */
    public static String urlEncode(String str, String charsetName) {
        if(str == null) {
            return str;
        }
        try {
            if (charsetName == null) {
                return URLEncoder.encode(str, "UTF-8");
            } else {
                return URLEncoder.encode(str, charsetName);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            // result="Change charset failed!";
            return str;
        }
    }

    /**
     * 使用指定字符集对字符串进行url解码
     *
     * @param str 待解码字符串
     * @param charsetName 字符集
     * @return 解码后的字符串
     */
    public static String urlDecode(String str, String charsetName) {
        if(str == null) {
            return str;
        }
        try {
            if (charsetName == null) {
                return URLDecoder.decode(str, "UTF-8");
            } else {
                return URLDecoder.decode(str, charsetName);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            // result="Change charset failed!";
            return str;
        }
    }

    /**
     * 使用utf-8字符集对字符串进行url编码
     *
     * @param str 待编码字符串
     * @return 编码后的字符串
     */
    public static String urlEncode(String str) {
        return urlEncode(str, null);
    }

    /**
     * 使用utf-8字符集对字符串进行url解码
     *
     * @param str 待解码字符串
     * @return 解码后的字符串
     */
    public static String urlDecode(String str) {
        return urlDecode(str, null);
    }

    /**
     * 按字节位截取字符串
     *
     * @param src 待截字符串
     * @param byteBegin 开始字节位
     * @param len 字节位长度
     * @return
     */
    public static String byteSubstring(String src, int byteBegin, int len) {
        if (src == null) {
            return null;
        }

        if (byteBegin < 0) {
            byteBegin = 0;
        }

        // 字符串大小与原串相同,直接返回,减少拷贝带来的开销
        if(byteBegin == 0 && (src.length() << 1) <= len) {
            return src;
        }

        byte[] bytes = src.getBytes();
        int slen = bytes.length;
        if (byteBegin >= slen) {
            throw new ArrayIndexOutOfBoundsException(byteBegin);
        }

        // 字符串大小与原串相同,直接返回,减少拷贝带来的开销
        if (len >= slen && byteBegin == 0) {
            return src;
        }
        slen -= byteBegin;
        len = (slen < len ? slen : len);
        String text = new String(bytes, byteBegin, len);


        //尾部字符截乱码，则删掉
        int lastIndex = text.length() - 1;
        if(lastIndex < 0) {
            return text;
        }
        if(text.charAt(lastIndex) != src.charAt(byteBegin + lastIndex)) {
            return src.substring(byteBegin, byteBegin + lastIndex);
        }
        return text;
    }

    /**
     * 获取带宽描述，最多保留两位小数<br>
     * 算法：当带宽数值超过某一阶级时(b,K,M,G,T)，除以1000,如果不能整除，除以1024<br>
     * 得到的数值加单位为最终带宽描述
     *
     * @param bandwidth 带宽比特数
     * @return
     */
    public static String getBandwidth(long bandwidth) {
        String[] units = {"b", "K", "M", "G", "T"};
        int unit = 0;
        long remainder = 0L, divisor = 1000L;
        while (remainder == 0L && bandwidth >= divisor) {
            remainder = bandwidth % divisor;
            if (remainder == 0L) {
                ++unit;
                bandwidth /= divisor;
            } else {
                if (divisor == 1000L)
                    divisor = 1024L;
            }
        }
        if (bandwidth < divisor)
            return bandwidth + units[unit];
        double bw = bandwidth;
        while (bw > divisor) {
            bw /= divisor;
            ++unit;
        }
        return new DecimalFormat("#.##").format(bw) + units[unit];
    }

    /**
     * 手机号码判断，判断标准见正则表达式
     *
     * @see #PHONE_NO_REGEX
     * @param number
     * @return
     */
    public static boolean isPhoneNO(String number) {
        if (number == null) {
            return false;
        }
        return number.matches(PHONE_NO_REGEX);
    }

    /**
     * QQ号码判断，判断标准见正则表达式
     *
     * @see #QQ_NO_REGEX
     * @param number
     * @return
     */
    public static boolean isQQNO(String number) {
        if (number == null) {
            return false;
        }
        return number.matches(QQ_NO_REGEX);
    }

    /**
     * 获取英文单词中的大写字母，第一个大写字母出现之前的首个小写字母将被转成大写字母
     * @param text
     * @return
     */
    public static String getUppers(String text) {
        if (text == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int i, len = text.length();
        boolean first = true;
        for (i = 0; i < len; ++i) {
            char c = text.charAt(i);
            if (first && c >= 'a' && c <= 'z') {
                sb.append((char) (c & 0xdf));
                first = false;
            } else if (c >= 'A' && c <= 'Z') {
                first = false;
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String[] toArray(String data, String split){
        if(data == null || data.length() == 0){
            return null;
        }
        String[] arr = data.split(split);
        return arr;
    }

    public static String toString(Object obj) {
        return obj == null ? null : obj.toString();
    }

    public static <T> String toString(Collection<T> values) {
        return CollectionUtils.toString(values);
    }
    public static <T> String toString(Collection<T> values, char gap) {
        return CollectionUtils.toString(values, gap);
    }

    public static <T> String toString(Collection<T> values, String gap) {
        return CollectionUtils.toString(values, gap);
    }

    public static <T> String toString(T[] values) {
        return ArrayUtils.toString(values);
    }
    public static <T> String toString(T[] values, char gap) {
        return ArrayUtils.toString(values, gap);
    }

    public static <T> String toString(T[] values, String gap) {
        return ArrayUtils.toString(values, gap);
    }

    public static String trimToNull(final String str) {
        if(str == null){
            return null;
        }
        final String ts = str.trim();
        return isEmpty(ts) ? null : ts;
    }

    public static String trimToEmpty(final String str) {
        return str == null ? EMPTY : str.trim();
    }

    public static String upper(Object text){
        if(text == null){
            return null;
        }
        if(text instanceof String){
            return ((String) text).toUpperCase();
        } else {
            return text.toString().toUpperCase();
        }
    }

    public static String lower(Object text){
        if(text == null){
            return null;
        }
        if(text instanceof String){
            return ((String) text).toLowerCase();
        } else {
            return text.toString().toLowerCase();
        }
    }

    /**
     * 判断字符串是否为空
     *
     * @param cs
     * @return
     */
    public static boolean isEmpty(CharSequence cs) {
        return cs == null ||  cs.length() == 0;
    }

    public static boolean isNotEmpty(CharSequence cs) {
        return !isEmpty(cs);
    }
    /**
     * 判断字符串是否为空
     *
     * @param cs
     * @return
     */
    public static boolean isBlank(CharSequence cs) {
        if(cs == null || cs.length() == 0){
            return  true;
        }
        int strLen;
        if(cs != null && (strLen = cs.length()) != 0) {
            for(int i = 0; i < strLen; ++i) {
                if(!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }
            return true;
        }
        return  false;
    }

    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }

    public static boolean isBlankEquals(String src, String target){
        if(isBlank(src)){
            return isBlank(target);
        }
        return src.equals(target);
    }

    public static boolean isAllEmpty(CharSequence... css){
        for(CharSequence cs : css){
            if(StringUtils.isNotEmpty(cs)){
                return false;
            }
        }
        return true;
    }

    public static boolean isAllBlank(CharSequence... css){
        for(CharSequence cs : css){
            if(StringUtils.isNotBlank(cs)){
                return false;
            }
        }
        return true;
    }

    public static boolean hasEmpty(CharSequence... css){
        for(CharSequence cs : css){
            if(StringUtils.isEmpty(cs)){
                return true;
            }
        }
        return false;
    }

    public static boolean hasBlank(CharSequence... css){
        for(CharSequence cs : css){
            if(StringUtils.isBlank(cs)){
                return true;
            }
        }
        return false;
    }

    /**
     * Check that the given CharSequence is neither <code>null</code> nor of length 0.
     * Note: Will return <code>true</code> for a CharSequence that purely consists of whitespace.
     * <p><pre>
     * StringUtils.hasLength(null) = false
     * StringUtils.hasLength("") = false
     * StringUtils.hasLength(" ") = true
     * StringUtils.hasLength("Hello") = true
     * </pre>
     * @param str the CharSequence to check (may be <code>null</code>)
     * @return <code>true</code> if the CharSequence is not null and has length
     * @see #hasText(String)
     */
    public static boolean hasLength(CharSequence str) {
        return str != null && str.length() > 0;
    }

    /**
     * Check that the given String is neither <code>null</code> nor of length 0.
     * Note: Will return <code>true</code> for a String that purely consists of whitespace.
     * @param str the String to check (may be <code>null</code>)
     * @return <code>true</code> if the String is not null and has length
     * @see #hasLength(CharSequence)
     */
    public static boolean hasLength(String str) {
        return hasLength((CharSequence)str);
    }

    /**
     * Check whether the given CharSequence has actual text.
     * More specifically, returns <code>true</code> if the string not <code>null</code>,
     * its length is greater than 0, and it contains at least one non-whitespace character.
     * <p><pre>
     * StringUtils.hasText(null) = false
     * StringUtils.hasText("") = false
     * StringUtils.hasText(" ") = false
     * StringUtils.hasText("12345") = true
     * StringUtils.hasText(" 12345 ") = true
     * </pre>
     * @param str the CharSequence to check (may be <code>null</code>)
     * @return <code>true</code> if the CharSequence is not <code>null</code>,
     * its length is greater than 0, and it does not contain whitespace only
     * @see Character#isWhitespace
     */
    public static boolean hasText(CharSequence str) {
        if(!hasLength(str)) {
            return false;
        } else {
            int strLen = str.length();

            for(int i = 0; i < strLen; ++i) {
                if(!Character.isWhitespace(str.charAt(i))) {
                    return true;
                }
            }

            return false;
        }
    }

    /**
     * Check whether the given String has actual text.
     * More specifically, returns <code>true</code> if the string not <code>null</code>,
     * its length is greater than 0, and it contains at least one non-whitespace character.
     * @param str the String to check (may be <code>null</code>)
     * @return <code>true</code> if the String is not <code>null</code>, its length is
     * greater than 0, and it does not contain whitespace only
     * @see #hasText(CharSequence)
     */
    public static boolean hasText(String str) {
        return hasText((CharSequence) str);
    }

    /**
     * Check whether the given CharSequence contains any whitespace characters.
     * @param str the CharSequence to check (may be <code>null</code>)
     * @return <code>true</code> if the CharSequence is not empty and
     * contains at least 1 whitespace character
     * @see Character#isWhitespace
     */
    public static boolean containsWhitespace(CharSequence str) {
        if(!hasLength(str)) {
            return false;
        } else {
            int strLen = str.length();

            for(int i = 0; i < strLen; ++i) {
                if(Character.isWhitespace(str.charAt(i))) {
                    return true;
                }
            }

            return false;
        }
    }

    /**
     * Check whether the given String contains any whitespace characters.
     * @param str the String to check (may be <code>null</code>)
     * @return <code>true</code> if the String is not empty and
     * contains at least 1 whitespace character
     * @see #containsWhitespace(CharSequence)
     */
    public static boolean containsWhitespace(String str) {
        return containsWhitespace((CharSequence) str);
    }

    public static boolean isWhitespace(char c) {
        if(c == 160) {
            return true;
        }
        return Character.isWhitespace(c);
    }

    /**
     * Trim leading and trailing whitespace from the given String.
     * @param str the String to check
     * @return the trimmed String
     * @see Character#isWhitespace
     */
    public static String trimWhitespace(String str) {
        if(!hasLength(str)) {
            return str;
        } else {
            int len = str.length(), start = 0, end = len - 1;

            while(start < end && isWhitespace(str.charAt(start))) {
                ++start;
            }

            while(start < end && isWhitespace(str.charAt(end))) {
                --end;
            }

            return str.substring(start, end + 1);
        }
    }

    public static void trimWhitespace(String[] strs) {
        if(strs == null) {
            return;
        }
        for (int i = 0; i < strs.length; i++) {
            strs[i] = trimWhitespace(strs[i]);
        }
    }

    /**
     * Trim <i>all</i> whitespace from the given String:
     * leading, trailing, and inbetween characters.
     * @param str the String to check
     * @return the trimmed String
     * @see Character#isWhitespace
     */
    public static String trimAllWhitespace(String str) {
        if(!hasLength(str)) {
            return str;
        } else {
            int len = str.length();
            StringBuilder sb = new StringBuilder(str.length());

            for(int i = 0; i < len; ++i) {
                char c = str.charAt(i);
                if(!isWhitespace(c)) {
                    sb.append(c);
                }
            }

            return sb.toString();
        }
    }

    /**
     * Trim leading whitespace from the given String.
     * @param str the String to check
     * @return the trimmed String
     * @see Character#isWhitespace
     */
    public static String trimLeadingWhitespace(String str) {
        if(!hasLength(str)) {
            return str;
        } else {
            StringBuilder sb = new StringBuilder(str);

            while(sb.length() > 0 && isWhitespace(sb.charAt(0))) {
                sb.deleteCharAt(0);
            }

            return sb.toString();
        }
    }

    /**
     * Trim trailing whitespace from the given String.
     * @param str the String to check
     * @return the trimmed String
     * @see Character#isWhitespace
     */
    public static String trimTrailingWhitespace(String str) {
        if(!hasLength(str)) {
            return str;
        } else {
            StringBuilder sb = new StringBuilder(str);

            while(sb.length() > 0 && Character.isWhitespace(sb.charAt(sb.length() - 1))) {
                sb.deleteCharAt(sb.length() - 1);
            }

            return sb.toString();
        }
    }

    /**
     * Trim all occurences of the supplied leading character from the given String.
     * @param str the String to check
     * @param leadingCharacter the leading character to be trimmed
     * @return the trimmed String
     */
    public static String trimLeadingCharacter(String str, char leadingCharacter) {
        if(!hasLength(str)) {
            return str;
        } else {
            StringBuilder sb = new StringBuilder(str);

            while(sb.length() > 0 && sb.charAt(0) == leadingCharacter) {
                sb.deleteCharAt(0);
            }

            return sb.toString();
        }
    }

    /**
     * Trim all occurences of the supplied trailing character from the given String.
     * @param str the String to check
     * @param trailingCharacter the trailing character to be trimmed
     * @return the trimmed String
     */
    public static String trimTrailingCharacter(String str, char trailingCharacter) {
        if(!hasLength(str)) {
            return str;
        } else {
            StringBuilder sb = new StringBuilder(str);

            while(sb.length() > 0 && sb.charAt(sb.length() - 1) == trailingCharacter) {
                sb.deleteCharAt(sb.length() - 1);
            }

            return sb.toString();
        }
    }

    /**
     * Test if the given String starts with the specified prefix,
     * ignoring upper/lower case.
     * @param str the String to check
     * @param prefix the prefix to look for
     * @see String#startsWith
     */
    public static boolean startsWithIgnoreCase(String str, String prefix) {
        if(str != null && prefix != null) {
            if(str.startsWith(prefix)) {
                return true;
            } else if(str.length() < prefix.length()) {
                return false;
            } else {
                String lcStr = str.substring(0, prefix.length()).toLowerCase();
                String lcPrefix = prefix.toLowerCase();
                return lcStr.equals(lcPrefix);
            }
        } else {
            return false;
        }
    }

    public static boolean startsWith(String str, String prefix) {
        return str == null || prefix == null ? false : str.startsWith(prefix);
    }

    public static boolean endsWith(String str, String prefix) {
        return str == null || prefix == null ? false : str.endsWith(prefix);
    }

    /**
     * Test if the given String ends with the specified suffix,
     * ignoring upper/lower case.
     * @param str the String to check
     * @param suffix the suffix to look for
     * @see String#endsWith
     */
    public static boolean endsWithIgnoreCase(String str, String suffix) {
        if(str != null && suffix != null) {
            if(str.endsWith(suffix)) {
                return true;
            } else if(str.length() < suffix.length()) {
                return false;
            } else {
                String lcStr = str.substring(str.length() - suffix.length()).toLowerCase();
                String lcSuffix = suffix.toLowerCase();
                return lcStr.equals(lcSuffix);
            }
        } else {
            return false;
        }
    }

    /**
     * Test whether the given string matches the given substring
     * at the given index.
     * @param str the original string (or StringBuilder)
     * @param index the index in the original string to start matching against
     * @param substring the substring to match at the given index
     */
    public static boolean substringMatch(CharSequence str, int index, CharSequence substring) {
        for(int j = 0; j < substring.length(); ++j) {
            int i = index + j;
            if(i >= str.length() || str.charAt(i) != substring.charAt(j)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Count the occurrences of the substring in string s.
     * @param str string to search in. Return 0 if this is null.
     * @param sub string to search for. Return 0 if this is null.
     */
    public static int countOccurrencesOf(String str, String sub) {
        if(str != null && sub != null && str.length() != 0 && sub.length() != 0) {
            int count = 0;

            int idx;
            for(int pos = 0; (idx = str.indexOf(sub, pos)) != -1; pos = idx + sub.length()) {
                ++count;
            }

            return count;
        } else {
            return 0;
        }
    }

    /**
     * Replace all occurences of a substring within a string with
     * another string.
     * @param inString String to examine
     * @param oldPattern String to replace
     * @param newPattern String to insert
     * @return a String with the replacements
     */
    public static String replace(String inString, String oldPattern, String newPattern) {
        if(hasLength(inString) && hasLength(oldPattern) && newPattern != null) {
            StringBuilder sb = new StringBuilder();
            int pos = 0;
            int index = inString.indexOf(oldPattern);

            for(int patLen = oldPattern.length(); index >= 0; index = inString.indexOf(oldPattern, pos)) {
                sb.append(inString.substring(pos, index));
                sb.append(newPattern);
                pos = index + patLen;
            }

            sb.append(inString.substring(pos));
            return sb.toString();
        } else {
            return inString;
        }
    }

    /**
     * Delete all occurrences of the given substring.
     * @param inString the original String
     * @param pattern the pattern to delete all occurrences of
     * @return the resulting String
     */
    public static String delete(String inString, String pattern) {
        return replace(inString, pattern, "");
    }

    /**
     * Delete any character in a given String.
     * @param inString the original String
     * @param charsToDelete a set of characters to delete.
     * E.g. "az\n" will delete 'a's, 'z's and new lines.
     * @return the resulting String
     */
    public static String deleteAny(String inString, String charsToDelete) {
        if(hasLength(inString) && hasLength(charsToDelete)) {
            StringBuilder sb = new StringBuilder();

            for(int i = 0; i < inString.length(); ++i) {
                char c = inString.charAt(i);
                if(charsToDelete.indexOf(c) == -1) {
                    sb.append(c);
                }
            }

            return sb.toString();
        } else {
            return inString;
        }
    }

    public static String deleteAll(String text, String... deleteTexts) {
        if(text == null || ArrayUtils.isEmpty(deleteTexts)) {
            return text;
        }
        String newText = text;
        for(String deleteText : deleteTexts) {
            if(deleteText == null) {
                continue;
            }
            if(newText.length() < deleteText.length()) {
                continue;
            }
            newText = newText.replace(deleteText, "");
        }
        return newText;
    }

    public static String deleteAllByRegex(String text, String... deleteRegexs) {
        if(text == null || ArrayUtils.isEmpty(deleteRegexs)) {
            return text;
        }
        String newText = text;
        for(String deleteRegex : deleteRegexs) {
            if(deleteRegex == null) {
                continue;
            }
            if(newText.length() < deleteRegex.length()) {
                continue;
            }
            newText = newText.replaceAll(deleteRegex, "");
        }
        return newText;
    }

    //---------------------------------------------------------------------
    // Convenience methods for working with formatted Strings
    //---------------------------------------------------------------------

    /**
     * Quote the given String with single quotes.
     * @param str the input String (e.g. "myString")
     * @return the quoted String (e.g. "'myString'"),
     * or <code>null<code> if the input was <code>null</code>
     */
    public static String quote(String str) {
        return (str != null ? "'" + str + "'" : null);
    }

    /**
     * Turn the given Object into a String with single quotes
     * if it is a String; keeping the Object as-is else.
     * @param obj the input Object (e.g. "myString")
     * @return the quoted String (e.g. "'myString'"),
     * or the input object as-is if not a String
     */
    public static Object quoteIfString(Object obj) {
        return (obj instanceof String ? quote((String) obj) : obj);
    }

    /**
     * Unqualify a string qualified by a '.' dot character. For example,
     * "this.name.is.qualified", returns "qualified".
     * @param qualifiedName the qualified name
     */
    public static String unqualify(String qualifiedName) {
        return unqualify(qualifiedName, '.');
    }

    /**
     * Unqualify a string qualified by a separator character. For example,
     * "this:name:is:qualified" returns "qualified" if using a ':' separator.
     * @param qualifiedName the qualified name
     * @param separator the separator
     */
    public static String unqualify(String qualifiedName, char separator) {
        return qualifiedName.substring(qualifiedName.lastIndexOf(separator) + 1);
    }

    /**
     * Capitalize a <code>String</code>, changing the first letter to
     * upper case as per {@link Character#toUpperCase(char)}.
     * No other letters are changed.
     * @param str the String to capitalize, may be <code>null</code>
     * @return the capitalized String, <code>null</code> if null
     */
    public static String capitalize(String str) {
        return changeFirstCharacterCase(str, true);
    }

    /**
     * Uncapitalize a <code>String</code>, changing the first letter to
     * lower case as per {@link Character#toLowerCase(char)}.
     * No other letters are changed.
     * @param str the String to uncapitalize, may be <code>null</code>
     * @return the uncapitalized String, <code>null</code> if null
     */
    public static String uncapitalize(String str) {
        return changeFirstCharacterCase(str, false);
    }

    private static String changeFirstCharacterCase(String str, boolean capitalize) {
        if(str != null && str.length() != 0) {
            StringBuilder sb = new StringBuilder(str.length());
            if(capitalize) {
                sb.append(Character.toUpperCase(str.charAt(0)));
            } else {
                sb.append(Character.toLowerCase(str.charAt(0)));
            }

            sb.append(str.substring(1));
            return sb.toString();
        } else {
            return str;
        }
    }

    /**
     * Extract the filename from the given path,
     * e.g. "mypath/myfile.txt" -> "myfile.txt".
     * @param path the file path (may be <code>null</code>)
     * @return the extracted filename, or <code>null</code> if none
     */
    public static String getFilename(String path) {
        if(path == null) {
            return null;
        } else {
            int separatorIndex = path.lastIndexOf("/");
            return separatorIndex != -1?path.substring(separatorIndex + 1):path;
        }
    }

    /**
     * Extract the filename extension from the given path,
     * e.g. "mypath/myfile.txt" -> "txt".
     * @param path the file path (may be <code>null</code>)
     * @return the extracted filename extension, or <code>null</code> if none
     */
    public static String getFilenameExtension(String path) {
        if(path == null) {
            return null;
        } else {
            int extIndex = path.lastIndexOf(46);
            if(extIndex == -1) {
                return null;
            } else {
                int folderIndex = path.lastIndexOf("/");
                return folderIndex > extIndex?null:path.substring(extIndex + 1);
            }
        }
    }

    /**
     * Strip the filename extension from the given path,
     * e.g. "mypath/myfile.txt" -> "mypath/myfile".
     * @param path the file path (may be <code>null</code>)
     * @return the path with stripped filename extension,
     * or <code>null</code> if none
     */
    public static String stripFilenameExtension(String path) {
        if(path == null) {
            return null;
        } else {
            int extIndex = path.lastIndexOf(46);
            if(extIndex == -1) {
                return path;
            } else {
                int folderIndex = path.lastIndexOf("/");
                return folderIndex > extIndex?path:path.substring(0, extIndex);
            }
        }
    }

    /**
     * Apply the given relative path to the given path,
     * assuming standard Java folder separation (i.e. "/" separators).
     * @param path the path to start from (usually a full file path)
     * @param relativePath the relative path to apply
     * (relative to the full file path above)
     * @return the full file path that results from applying the relative path
     */
    public static String applyRelativePath(String path, String relativePath) {
        int separatorIndex = path.lastIndexOf("/");
        if(separatorIndex != -1) {
            String newPath = path.substring(0, separatorIndex);
            if(!relativePath.startsWith("/")) {
                newPath = newPath + "/";
            }

            return newPath + relativePath;
        } else {
            return relativePath;
        }
    }

    /**
     * Normalize the path by suppressing sequences like "path/.." and
     * inner simple dots.
     * <p>The result is convenient for path comparison. For other uses,
     * notice that Windows separators ("\") are replaced by simple slashes.
     * @param path the original path
     * @return the normalized path
     */
    public static String cleanPath(String path) {
        if(path == null) {
            return null;
        } else {
            String pathToUse = replace(path, "\\", "/");
            int prefixIndex = pathToUse.indexOf(":");
            String prefix = "";
            if(prefixIndex != -1) {
                prefix = pathToUse.substring(0, prefixIndex + 1);
                if(prefix.contains("/")) {
                    prefix = "";
                } else {
                    pathToUse = pathToUse.substring(prefixIndex + 1);
                }
            }

            if(pathToUse.startsWith("/")) {
                prefix = prefix + "/";
                pathToUse = pathToUse.substring(1);
            }

            String[] pathArray = delimitedListToStringArray(pathToUse, "/");
            LinkedList pathElements = new LinkedList();
            int tops = 0;

            int i;
            for(i = pathArray.length - 1; i >= 0; --i) {
                String element = pathArray[i];
                if(!".".equals(element)) {
                    if("..".equals(element)) {
                        ++tops;
                    } else if(tops > 0) {
                        --tops;
                    } else {
                        pathElements.add(0, element);
                    }
                }
            }

            for(i = 0; i < tops; ++i) {
                pathElements.add(0, "..");
            }

            return prefix + collectionToDelimitedString(pathElements, "/");
        }
    }

    /**
     * Compare two paths after normalization of them.
     * @param path1 first path for comparison
     * @param path2 second path for comparison
     * @return whether the two paths are equivalent after normalization
     */
    public static boolean pathEquals(String path1, String path2) {
        return cleanPath(path1).equals(cleanPath(path2));
    }

    /**
     * Parse the given <code>localeString</code> value into a {@link Locale}.
     * <p>This is the inverse operation of {@link Locale#toString Locale's toString}.
     * @param localeString the locale string, following <code>Locale's</code>
     * <code>toString()</code> format ("en", "en_UK", etc);
     * also accepts spaces as separators, as an alternative to underscores
     * @return a corresponding <code>Locale</code> instance
     */
    public static Locale parseLocaleString(String localeString) {
        for (int i = 0; i < localeString.length(); i++) {
            char ch = localeString.charAt(i);
            if (ch != '_' && ch != ' ' && !Character.isLetterOrDigit(ch)) {
                throw new IllegalArgumentException(
                    "Locale value \"" + localeString + "\" contains invalid characters");
            }
        }
        String[] parts = tokenizeToStringArray(localeString, "_ ", false, false);
        String language = (parts.length > 0 ? parts[0] : "");
        String country = (parts.length > 1 ? parts[1] : "");
        String variant = "";
        if (parts.length >= 2) {
            // There is definitely a variant, and it is everything after the country
            // code sans the separator between the country code and the variant.
            int endIndexOfCountryCode = localeString.indexOf(country) + country.length();
            // Strip off any leading '_' and whitespace, what's left is the variant.
            variant = trimLeadingWhitespace(localeString.substring(endIndexOfCountryCode));
            if (variant.startsWith("_")) {
                variant = trimLeadingCharacter(variant, '_');
            }
        }
        return (language.length() > 0 ? new Locale(language, country, variant) : null);
    }

    /**
     * Determine the RFC 3066 compliant language tag,
     * as used for the HTTP "Accept-Language" header.
     * @param locale the Locale to transform to a language tag
     * @return the RFC 3066 compliant language tag as String
     */
    public static String toLanguageTag(Locale locale) {
        return locale.getLanguage() + (hasText(locale.getCountry()) ? "-" + locale.getCountry() : "");
    }

    //---------------------------------------------------------------------
    // Convenience methods for working with String arrays
    //---------------------------------------------------------------------

    /**
     * Append the given String to the given String array, returning a new array
     * consisting of the input array contents plus the given String.
     * @param array the array to append to (can be <code>null</code>)
     * @param str the String to append
     * @return the new array (never <code>null</code>)
     */
    public static String[] addStringToArray(String[] array, String str) {
        if(ArrayUtils.isEmpty(array)) {
            return new String[]{str};
        } else {
            String[] newArr = new String[array.length + 1];
            System.arraycopy(array, 0, newArr, 0, array.length);
            newArr[array.length] = str;
            return newArr;
        }
    }

    /**
     * Concatenate the given String arrays into one,
     * with overlapping array elements included twice.
     * <p>The order of elements in the original arrays is preserved.
     * @param array1 the first array (can be <code>null</code>)
     * @param array2 the second array (can be <code>null</code>)
     * @return the new array (<code>null</code> if both given arrays were <code>null</code>)
     */
    public static String[] concatenateStringArrays(String[] array1, String[] array2) {
        if(ArrayUtils.isEmpty(array1)) {
            return array2;
        } else if(ArrayUtils.isEmpty(array2)) {
            return array1;
        } else {
            String[] newArr = new String[array1.length + array2.length];
            System.arraycopy(array1, 0, newArr, 0, array1.length);
            System.arraycopy(array2, 0, newArr, array1.length, array2.length);
            return newArr;
        }
    }

    /**
     * Merge the given String arrays into one, with overlapping
     * array elements only included once.
     * <p>The order of elements in the original arrays is preserved
     * (with the exception of overlapping elements, which are only
     * included on their first occurrence).
     * @param array1 the first array (can be <code>null</code>)
     * @param array2 the second array (can be <code>null</code>)
     * @return the new array (<code>null</code> if both given arrays were <code>null</code>)
     */
    public static String[] mergeStringArrays(String[] array1, String[] array2) {
        if(ArrayUtils.isEmpty(array1)) {
            return array2;
        } else if(ArrayUtils.isEmpty(array2)) {
            return array1;
        } else {
            ArrayList result = new ArrayList();
            result.addAll(Arrays.asList(array1));
            String[] var3 = array2;
            int var4 = array2.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                String str = var3[var5];
                if(!result.contains(str)) {
                    result.add(str);
                }
            }

            return toStringArray((Collection)result);
        }
    }

    /**
     * Turn given source String array into sorted array.
     * @param array the source array
     * @return the sorted array (never <code>null</code>)
     */
    public static String[] sortStringArray(String[] array) {
        if(ArrayUtils.isEmpty(array)) {
            return new String[0];
        } else {
            Arrays.sort(array);
            return array;
        }
    }

    /**
     * Copy the given Collection into a String array.
     * The Collection must contain String elements only.
     * @param collection the Collection to copy
     * @return the String array (<code>null</code> if the passed-in
     * Collection was <code>null</code>)
     */
    public static String[] toStringArray(Collection<String> collection) {
        return collection == null?null:(String[])collection.toArray(new String[collection.size()]);
    }

    /**
     * Copy the given Enumeration into a String array.
     * The Enumeration must contain String elements only.
     * @param enumeration the Enumeration to copy
     * @return the String array (<code>null</code> if the passed-in
     * Enumeration was <code>null</code>)
     */
    public static String[] toStringArray(Enumeration<String> enumeration) {
        if(enumeration == null) {
            return null;
        } else {
            ArrayList list = Collections.list(enumeration);
            return (String[])list.toArray(new String[list.size()]);
        }
    }

    /**
     * Trim the elements of the given String array,
     * calling <code>String.trim()</code> on each of them.
     * @param array the original String array
     * @return the resulting array (of the same size) with trimmed elements
     */
    public static String[] trimArrayElements(String[] array) {
        if(ArrayUtils.isEmpty(array)) {
            return new String[0];
        } else {
            String[] result = new String[array.length];

            for(int i = 0; i < array.length; ++i) {
                String element = array[i];
                result[i] = element != null?element.trim():null;
            }

            return result;
        }
    }

    /**
     * Remove duplicate Strings from the given array.
     * Also sorts the array, as it uses a TreeSet.
     * @param array the String array
     * @return an array without duplicates, in natural sort order
     */
    public static String[] removeDuplicateStrings(String[] array) {
        if(ArrayUtils.isEmpty(array)) {
            return array;
        } else {
            LinkedHashSet set = new LinkedHashSet();
            String[] var2 = array;
            int var3 = array.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                String element = var2[var4];
                set.add(element);
            }

            return toStringArray((Collection)set);
        }
    }

    /**
     * Split a String at the first occurrence of the delimiter.
     * Does not include the delimiter in the result.
     * @param str the string to split
     * @param separatorChars to split the string up with
     * @return a two element array with index 0 being before the delimiter, and
     * index 1 being after the delimiter (neither element includes the delimiter);
     * or <code>null</code> if the delimiter wasn't found in the given input String
     */
    public static String[] split(String str, String separatorChars) {
        return splitWorker(str, separatorChars, -1, false);
    }

    public static String[] split(String str, String separatorChars, int max) {
        return splitWorker(str, separatorChars, max, false);
    }

    private static String[] splitWorker(String str, String separatorChars, int max, boolean preserveAllTokens) {
        if(str == null) {
            return null;
        } else {
            int len = str.length();
            if(len == 0) {
                return new String[0];
            } else {
                ArrayList list = new ArrayList();
                int sizePlus1 = 1;
                int i = 0;
                int start = 0;
                boolean match = false;
                boolean lastMatch = false;
                if(separatorChars != null) {
                    if(separatorChars.length() != 1) {
                        label87:
                        while(true) {
                            while(true) {
                                if(i >= len) {
                                    break label87;
                                }

                                if(separatorChars.indexOf(str.charAt(i)) >= 0) {
                                    if(match || preserveAllTokens) {
                                        lastMatch = true;
                                        if(sizePlus1++ == max) {
                                            i = len;
                                            lastMatch = false;
                                        }

                                        list.add(str.substring(start, i));
                                        match = false;
                                    }

                                    ++i;
                                    start = i;
                                } else {
                                    lastMatch = false;
                                    match = true;
                                    ++i;
                                }
                            }
                        }
                    } else {
                        char sep = separatorChars.charAt(0);

                        label71:
                        while(true) {
                            while(true) {
                                if(i >= len) {
                                    break label71;
                                }

                                if(str.charAt(i) == sep) {
                                    if(match || preserveAllTokens) {
                                        lastMatch = true;
                                        if(sizePlus1++ == max) {
                                            i = len;
                                            lastMatch = false;
                                        }

                                        list.add(str.substring(start, i));
                                        match = false;
                                    }

                                    ++i;
                                    start = i;
                                } else {
                                    lastMatch = false;
                                    match = true;
                                    ++i;
                                }
                            }
                        }
                    }
                } else {
                    label103:
                    while(true) {
                        while(true) {
                            if(i >= len) {
                                break label103;
                            }

                            if(Character.isWhitespace(str.charAt(i))) {
                                if(match || preserveAllTokens) {
                                    lastMatch = true;
                                    if(sizePlus1++ == max) {
                                        i = len;
                                        lastMatch = false;
                                    }

                                    list.add(str.substring(start, i));
                                    match = false;
                                }

                                ++i;
                                start = i;
                            } else {
                                lastMatch = false;
                                match = true;
                                ++i;
                            }
                        }
                    }
                }

                if(match || preserveAllTokens && lastMatch) {
                    list.add(str.substring(start, i));
                }

                return (String[])((String[])list.toArray(new String[list.size()]));
            }
        }
    }

    /**
     * Take an array Strings and split each element based on the given delimiter.
     * A <code>Properties</code> instance is then generated, with the left of the
     * delimiter providing the key, and the right of the delimiter providing the value.
     * <p>Will trim both the key and value before adding them to the
     * <code>Properties</code> instance.
     * @param array the array to process
     * @param delimiter to split each element using (typically the equals symbol)
     * @return a <code>Properties</code> instance representing the array contents,
     * or <code>null</code> if the array to process was null or empty
     */
    public static Properties splitArrayElementsIntoProperties(String[] array, String delimiter) {
        return splitArrayElementsIntoProperties(array, delimiter, (String)null);
    }

    /**
     * Take an array Strings and split each element based on the given delimiter.
     * A <code>Properties</code> instance is then generated, with the left of the
     * delimiter providing the key, and the right of the delimiter providing the value.
     * <p>Will trim both the key and value before adding them to the
     * <code>Properties</code> instance.
     * @param array the array to process
     * @param delimiter to split each element using (typically the equals symbol)
     * @param charsToDelete one or more characters to remove from each element
     * prior to attempting the split operation (typically the quotation mark
     * symbol), or <code>null</code> if no removal should occur
     * @return a <code>Properties</code> instance representing the array contents,
     * or <code>null</code> if the array to process was <code>null</code> or empty
     */
    public static Properties splitArrayElementsIntoProperties(String[] array, String delimiter, String charsToDelete) {
        if(ArrayUtils.isEmpty(array)) {
            return null;
        } else {
            Properties result = new Properties();
            String[] var4 = array;
            int var5 = array.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                String element = var4[var6];
                if(charsToDelete != null) {
                    element = deleteAny(element, charsToDelete);
                }

                String[] splittedElement = split(element, delimiter);
                if(splittedElement != null) {
                    result.setProperty(splittedElement[0].trim(), splittedElement[1].trim());
                }
            }

            return result;
        }
    }

    /**
     * Tokenize the given String into a String array via a StringTokenizer.
     * Trims tokens and omits empty tokens.
     * <p>The given delimiters string is supposed to consist of any number of
     * delimiter characters. Each of those characters can be used to separate
     * tokens. A delimiter is always a single character; for multi-character
     * delimiters, consider using <code>delimitedListToStringArray</code>
     * @param str the String to tokenize
     * @param delimiters the delimiter characters, assembled as String
     * (each of those characters is individually considered as delimiter).
     * @return an array of the tokens
     * @see StringTokenizer
     * @see String#trim()
     * @see #delimitedListToStringArray
     */
    public static String[] tokenizeToStringArray(String str, String delimiters) {
        return tokenizeToStringArray(str, delimiters, true, true);
    }

    /**
     * Tokenize the given String into a String array via a StringTokenizer.
     * <p>The given delimiters string is supposed to consist of any number of
     * delimiter characters. Each of those characters can be used to separate
     * tokens. A delimiter is always a single character; for multi-character
     * delimiters, consider using <code>delimitedListToStringArray</code>
     * @param str the String to tokenize
     * @param delimiters the delimiter characters, assembled as String
     * (each of those characters is individually considered as delimiter)
     * @param trimTokens trim the tokens via String's <code>trim</code>
     * @param ignoreEmptyTokens omit empty tokens from the result array
     * (only applies to tokens that are empty after trimming; StringTokenizer
     * will not consider subsequent delimiters as token in the first place).
     * @return an array of the tokens (<code>null</code> if the input String
     * was <code>null</code>)
     * @see StringTokenizer
     * @see String#trim()
     * @see #delimitedListToStringArray
     */
    public static String[] tokenizeToStringArray(String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {
        if(str == null) {
            return null;
        } else {
            StringTokenizer st = new StringTokenizer(str, delimiters);
            ArrayList tokens = new ArrayList();

            while(true) {
                String token;
                do {
                    if(!st.hasMoreTokens()) {
                        return toStringArray((Collection)tokens);
                    }

                    token = st.nextToken();
                    if(trimTokens) {
                        token = token.trim();
                    }
                } while(ignoreEmptyTokens && token.length() <= 0);

                tokens.add(token);
            }
        }
    }

    /**
     * Take a String which is a delimited list and convert it to a String array.
     * <p>A single delimiter can consists of more than one character: It will still
     * be considered as single delimiter string, rather than as bunch of potential
     * delimiter characters - in contrast to <code>tokenizeToStringArray</code>.
     * @param str the input String
     * @param delimiter the delimiter between elements (this is a single delimiter,
     * rather than a bunch individual delimiter characters)
     * @return an array of the tokens in the list
     * @see #tokenizeToStringArray
     */
    public static String[] delimitedListToStringArray(String str, String delimiter) {
        return delimitedListToStringArray(str, delimiter, (String)null);
    }

    /**
     * Take a String which is a delimited list and convert it to a String array.
     * <p>A single delimiter can consists of more than one character: It will still
     * be considered as single delimiter string, rather than as bunch of potential
     * delimiter characters - in contrast to <code>tokenizeToStringArray</code>.
     * @param str the input String
     * @param delimiter the delimiter between elements (this is a single delimiter,
     * rather than a bunch individual delimiter characters)
     * @param charsToDelete a set of characters to delete. Useful for deleting unwanted
     * line breaks: e.g. "\r\n\f" will delete all new lines and line feeds in a String.
     * @return an array of the tokens in the list
     * @see #tokenizeToStringArray
     */
    public static String[] delimitedListToStringArray(String str, String delimiter, String charsToDelete) {
        if(str == null) {
            return new String[0];
        } else if(delimiter == null) {
            return new String[]{str};
        } else {
            ArrayList result = new ArrayList();
            int pos;
            if("".equals(delimiter)) {
                for(pos = 0; pos < str.length(); ++pos) {
                    result.add(deleteAny(str.substring(pos, pos + 1), charsToDelete));
                }
            } else {
                int delPos;
                for(pos = 0; (delPos = str.indexOf(delimiter, pos)) != -1; pos = delPos + delimiter.length()) {
                    result.add(deleteAny(str.substring(pos, delPos), charsToDelete));
                }

                if(str.length() > 0 && pos <= str.length()) {
                    result.add(deleteAny(str.substring(pos), charsToDelete));
                }
            }

            return toStringArray((Collection)result);
        }
    }

    /**
     * Convert a CSV list into an array of Strings.
     * @param str the input String
     * @return an array of Strings, or the empty array in case of empty input
     */
    public static String[] commaDelimitedListToStringArray(String str) {
        return delimitedListToStringArray(str, ",");
    }

    /**
     * Convenience method to convert a CSV string list to a set.
     * Note that this will suppress duplicates.
     * @param str the input String
     * @return a Set of String entries in the list
     */
    public static Set<String> commaDelimitedListToSet(String str) {
        LinkedHashSet set = new LinkedHashSet();
        String[] tokens = commaDelimitedListToStringArray(str);
        String[] var3 = tokens;
        int var4 = tokens.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String token = var3[var5];
            set.add(token);
        }

        return set;
    }

    /**
     * Convenience method to return a Collection as a delimited (e.g. CSV)
     * String. E.g. useful for <code>toString()</code> implementations.
     * @param coll the Collection to display
     * @param delim the delimiter to use (probably a ",")
     * @param prefix the String to start each element with
     * @param suffix the String to end each element with
     * @return the delimited String
     */
    public static String collectionToDelimitedString(Collection<?> coll, String delim, String prefix, String suffix) {
        if(CollectionUtils.isEmpty(coll)) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();
            Iterator it = coll.iterator();

            while(it.hasNext()) {
                sb.append(prefix).append(it.next()).append(suffix);
                if(it.hasNext()) {
                    sb.append(delim);
                }
            }

            return sb.toString();
        }
    }

    /**
     * Convenience method to return a Collection as a delimited (e.g. CSV)
     * String. E.g. useful for <code>toString()</code> implementations.
     * @param coll the Collection to display
     * @param delim the delimiter to use (probably a ",")
     * @return the delimited String
     */
    public static String collectionToDelimitedString(Collection<?> coll, String delim) {
        return collectionToDelimitedString(coll, delim, "", "");
    }

    /**
     * Convenience method to return a Collection as a CSV String.
     * E.g. useful for <code>toString()</code> implementations.
     * @param coll the Collection to display
     * @return the delimited String
     */
    public static String collectionToCommaDelimitedString(Collection<?> coll) {
        return collectionToDelimitedString(coll, ",");
    }

    /**
     * Convenience method to return a String array as a delimited (e.g. CSV)
     * String. E.g. useful for <code>toString()</code> implementations.
     * @param arr the array to display
     * @param delim the delimiter to use (probably a ",")
     * @return the delimited String
     */
    public static String arrayToDelimitedString(Object[] arr, String delim) {
        if(ArrayUtils.isEmpty(arr)) {
            return "";
        } else if(arr.length == 1) {
            return ObjectUtils.nullSafeToString(arr[0]);
        } else {
            StringBuilder sb = new StringBuilder();

            for(int i = 0; i < arr.length; ++i) {
                if(i > 0) {
                    sb.append(delim);
                }

                sb.append(arr[i]);
            }

            return sb.toString();
        }
    }

    /**
     * Convenience method to return a String array as a CSV String.
     * E.g. useful for <code>toString()</code> implementations.
     * @param arr the array to display
     * @return the delimited String
     */
    public static String arrayToCommaDelimitedString(Object[] arr) {
        return arrayToDelimitedString(arr, ",");
    }


    public static String defaultIfBlank(String text, String defaultText){
        return isBlank(text) ? defaultText : text;
    }

    public static String defaultIfEmpty(String text, String defaultText){
        return isEmpty(text) ? defaultText : text;
    }

    public static final String NUMBER_REGEX = "\\d+(\\.\\d+)?";
    public static boolean isNumeric(String text){
        return text == null ? false : text.matches(NUMBER_REGEX);
    }

    public static String substringFromFirstChar(String text, char chr){
        if(text == null){
            return text;
        }
        int index = text.indexOf(chr);
        return index < 0 ? "" : text.substring(index);
    }

    public static String substringBeforeFirstChar(String text, char chr){
        if(text == null){
            return text;
        }
        int index = text.indexOf(chr);
        return index < 0 ? "" : text.substring(0, index);
    }

    public static String substringAfterFirstChar(String text, char chr){
        if(text == null){
            return text;
        }
        int index = text.indexOf(chr);
        if(index < 0 || index == text.length() - 1){
            return "";
        }
        return text.substring(index + 1);
    }

    public static String substringFromFirstMatch(String text, String chr){
        if(text == null){
            return text;
        }
        int index = text.indexOf(chr);
        return index < 0 ? "" : text.substring(index);
    }

    public static String substringBeforeFirstMatch(String text, String chr){
        if(text == null || chr == null){
            return text;
        }
        int index = text.indexOf(chr);
        return index < 0 ? "" : text.substring(0, index);
    }

    public static String substringEndFirstMatch(String text, String chr){
        if(text == null || chr == null){
            return text;
        }
        int index = text.indexOf(chr);
        return index < 0 ? "" : text.substring(0, index + chr.length());
    }

    public static String substringAfterFirstMatch(String text, String chr){
        if(text == null || chr == null){
            return text;
        }
        int index = text.indexOf(chr);
        if(index < 0 || index == text.length() - chr.length()){
            return "";
        }
        return text.substring(index + chr.length());
    }

    public static String substringIfFromFirstChar(String text, char chr){
        if(text == null){
            return text;
        }
        int index = text.indexOf(chr);
        return index < 0 ? text : text.substring(index);
    }

    public static String substringIfBeforeFirstChar(String text, char chr){
        if(text == null){
            return text;
        }
        int index = text.indexOf(chr);
        return index < 0 ? text : text.substring(0, index);
    }

    public static String substringIfEndFirstChar(String text, char chr){
        if(text == null){
            return text;
        }
        int index = text.indexOf(chr);
        return index < 0 ? text : text.substring(0, index + 1);
    }

    public static String substringIfAfterFirstChar(String text, char chr){
        if(text == null){
            return text;
        }
        int index = text.indexOf(chr);
        if(index < 0 || index == text.length() - 1){
            return text;
        }
        return text.substring(index + 1);
    }

    public static String substringIfFromFirstMatch(String text, String chr){
        if(text == null){
            return text;
        }
        int index = text.indexOf(chr);
        return index < 0 ? text : text.substring(index);
    }

    public static String substringIfEndFirstMatch(String text, String chr){
        if(text == null || chr == null){
            return text;
        }
        int index = text.indexOf(chr);
        return index < 0 ? text : text.substring(0, index + chr.length());
    }

    public static String substringIfBeforeFirstMatch(String text, String chr){
        if(text == null || chr == null){
            return text;
        }
        int index = text.indexOf(chr);
        return index < 0 ? text : text.substring(0, index);
    }

    public static String substringIfAfterFirstMatch(String text, String chr){
        if(text == null || chr == null){
            return text;
        }
        int index = text.indexOf(chr);
        if(index < 0 || index == text.length() - chr.length()){
            return text;
        }
        return text.substring(index + chr.length());
    }

    /**
     * 跨站脚本注入攻击判断
     * @param text
     * @return
     */
    public static boolean hasScriptlet(final String text){
        if(isEmpty(text)){
            return false;
        }
        return SCRIPT_PATTERN.matcher(text).find();
    }

    public static boolean equalsAfterTrim(String src, String dest){
        if(isBlank(src)){
            return isBlank(dest);
        }
        if(isBlank(dest)){
            return false;
        }
        return src.trim().equals(dest.trim());
    }

    public static boolean containsOne(String text, String... containTexts) {
        if(text == null) {
            return ObjectUtils.hasNull(containTexts);
        }

        for(String containText : containTexts) {
            if(text.contains(containText)) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsOne(String text, boolean ignoreCase, String... strs){
        if(text == null) {
            return ObjectUtils.hasNull(strs);
        }

        if(ignoreCase) {
            text = text.toLowerCase();
        }
        for(String str : strs) {
            if(str == null) {
                continue;
            }
            if(ignoreCase) {
                str = str.toLowerCase();
            }
            if(text.contains(str.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsAll(String text, String... containTexts) {
        if(text == null) {
            return ObjectUtils.isAllNull(containTexts);
        }

        for(String containText : containTexts) {
            if(!text.contains(containText)) {
                return false;
            }
        }
        return true;
    }

    public static int findEndWithoutEscape(String text, int start, char c) {
        if(text == null || start >= text.length()) {
            return -1;
        }
        if(text.charAt(start) == c) {
            return start;
        }
        int i = start;
        while((i= text.indexOf(c, ++i)) > 0 ) {
            int k = i;
            while(--k > -1 && text.charAt(k) == '\\');
            //前面有偶数个'\'表示字符没有转义
            if(((i - k) & 1) == 1) {
                return i;
            }
        }
        return -1;
    }

    /**
     * if texts has a null value, return empty string ""
     * else return concat all texts
     * @param texts
     * @return
     */
    public static String concatNoNull(String... texts) {
        int len = 0;
        for(String text : texts) {
            if(text == null) {
                return "";
            }
            len += text.length();
        }
        StringBuilder sb = new StringBuilder(len);
        for(String text : texts) {
            sb.append(text);
        }
        return sb.toString();
    }

    public static String betweenText(String text, String startText, String firstEndText) {
        if(isEmpty(text)) {
            return "";
        }
        if(hasEmpty(startText, firstEndText)) {
            return "";
        }
        int start = text.indexOf(startText);
        if(start < 0) {
            return "";
        }
        int from = start + startText.length();
        if(from >= text.length()) {
            return "";
        }
        int end = text.indexOf(firstEndText, from);
        return end < 0 ? "" : text.substring(from, end);
    }

    public static boolean containsBySplitChar(String text, String subText, char splitChar) {
        if(text == null) {
            return subText == null;
        } else if(subText == null) {
            return true;
        }

        int index = text.indexOf(subText);
        if(index < 0) {
            return false;
        }
        int end = index + subText.length();
        return (index == 0 || text.charAt(index - 1) == splitChar) &&
            (end == text.length() || text.charAt(end) == splitChar);
    }

    public static String substring(String text, int start) {
        return text == null ? text : substring(text, start, text.length());
    }

    public static String substring(String text, int start, int end) {
        if(text == null) {
            return text;
        }
        int len = text.length();
        if(len < start) {
            return null;
        } else if(len == start) {
            return "";
        }
        return text.substring(start, len > end ? end : len);
    }

    private static boolean hasCharBetween(String text, char start, char end) {
        for(int i = 0, len = text.length(); i < len; ++i) {
            char c = text.charAt(i);
            if(start <= c && c <= end) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasChinese(String text) {
        if(isEmpty(text)) {
            return false;
        }
        return hasCharBetween(text, '\u4E00', '\u9FA5');
    }

    public static boolean hasJapanese(String text) {
        if(isEmpty(text)) {
            return false;
        }
        //\u3000\u3001\u3002分别与中文空格、中文顿号、中文句号相对应，因此排除此判断
        if(hasCharBetween(text, '\u3003', '\u30FF')) {
            return true;
        } else if(hasCharBetween(text, '\u2E80', '\u2FD5')) {
            return true;
        } else if(hasCharBetween(text, '\uFF5F', '\uFF9F')) {
            return true;
        }
        return false;
    }

    /**
     * 该方法只适用于等长替换，即一个一个字符替换，不能多个字符替换成一个字符，或者一个字符替换成多个字符
     * @param builder
     * @param replacement
     * @param replaceEveryChar
     * @return
     */
    public static boolean replaceWithSameLength(StringBuilder builder, String replacement, char replaceEveryChar) {
        if(isEmpty(builder) || isEmpty(replacement)) {
            return false;
        }

        int index = builder.indexOf(replacement);
        if(index < 0) {
            return false;
        }

        do {
            for(int start = index, end = index + replacement.length(); start < end; ++start) {
                builder.setCharAt(start, replaceEveryChar);
            }
            index = builder.indexOf(replacement, index);
        } while (index > -1);
        return true;
    }

    /**
     *
     * @param next
     * @return 返回0~next间的随机数的16进制表示
     */
    public static String random(int next) {
        int random = (int) (Math.random() * next);
        return Integer.toHexString(random);
    }

    /**
     *
     * @param next
     * @return 返回0~next间的随机数的16进制表示
     */
    public static String random(long next) {
        long random = (long) (Math.random() * next);
        return Long.toHexString(random);
    }

    /**
     * 比较两个字符串的数值大小，如果a>b返回1, a<b返回-1, a==b返回0
     * @param a
     * @param b
     * @return
     */
    public static int compareNumber(String a, String b) {
        if(a == null) {
            return b == null ? 0 : -1;
        }
        if(b == null) {
            return 1;
        }

        int sa = a.lastIndexOf('.');
        int sb = b.lastIndexOf('.');
        if(sa < 0) {
            sa = a.length();
        }

        int startA = -1;
        for(int i = 0; i < sa; ++i) {
            if(a.charAt(i) == '0') {
                startA = i;
            } else {
                break;
            }
        }

        int startB = -1;
        for(int i = 0; i < sa; ++i) {
            if(b.charAt(i) == '0') {
                startB = i;
            } else {
                break;
            }
        }

        if(sb < 0) {
            sb = b.length();
        }

        startA++; startB++;

        sa -= startA; //去掉左侧0
        sb -= startB; //去掉左侧0

        if(sa > sb) {
            return 1;
        } else if(sa < sb) {
            return -1;
        }

        return a.substring(startA).compareTo(b.substring(startB));
    }


    public static void toLowerCase(String[] texts) {
        if(texts == null) {
            return;
        }
        for (int i = 0; i < texts.length; i++) {
            if(texts[i] != null) {
                texts[i] = texts[i].toLowerCase();
            }
        }
    }

    public static void toUpperCase(String[] texts) {
        if(texts == null) {
            return;
        }
        for (int i = 0; i < texts.length; i++) {
            if(texts[i] != null) {
                texts[i] = texts[i].toUpperCase();
            }
        }
    }
}
