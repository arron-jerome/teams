package com.disney.teams.utils;

import com.disney.teams.utils.type.CollectionUtils;
import com.disney.teams.utils.type.ObjectUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class RegexUtils {

    public static int[] findPosition(String text, String regex){
        return findPosition(text, regex, 0);
    }

    public static int[] findPosition(String text, String regex, int flags){
        return findPosition(text, regex, 0, text == null ? 0 : text.length(), flags);
    }

    public static int[] findPosition(String text, String regex, int start, int end) {
        return findPosition(text, regex, start, end, 0);
    }

    public static int[] findPosition(String text, String regex, int start, int end, int flags) {
        if(text == null) {
            return null;
        }
        Pattern p = Pattern.compile(regex, flags);
        Matcher m = p.matcher(text);
        m.region(start, end);
        while(m.find()){
            int len = m.groupCount();
            if(len > 0){
                return new int[]{m.start(1), m.end(1)};
            }
        }
        return null;
    }

    /**
     *
     * @param text 待匹配文本
     * @param regex 正则表达式
     * @return 返回第一个匹配结果
     */
    public static String findMatch(String text, String regex){
        return findMatch(text, regex, 0);
    }

    public static String findMatch(String text, String regex, int flags){
        return findMatch(text, regex, 0, text == null ? 0 : text.length(), flags);
    }

    public static String findMatch(String text, String regex, int start, int end) {
        return findMatch(text, regex, start, end, 0);
    }

    public static String findMatch(String text, String regex, int start, int end, int flags) {
        if(text == null) {
            return null;
        }
        Pattern p = Pattern.compile(regex, flags);
        Matcher m = p.matcher(text);
        m.region(start, end);
        while(m.find()){
            int len = m.groupCount();
            if(len > 0){
                return m.group(1);
            }
        }
        return null;
    }

    /**
     *
     * @param text 待匹配文本
     * @param regex 正则表达式
     * @return 返回第一次匹配结果
     */
    public static List<String> findFirstMatch(String text, String regex){
        return findFirstMatch(text, regex, 0);
    }

    public static List<String> findFirstMatch(String text, String regex, int flags){
        return findFirstMatch(text, regex, 0, text == null ? 0 : text.length(), flags);
    }

    public static List<String> findFirstMatch(String text, String regex, int start, int end){
        return findFirstMatch(text, regex, start, end, 0);
    }

    public static List<String> findFirstMatch(String text, String regex, int start, int end, int flags){
        if(text == null){
            return null;
        }
        Pattern p = Pattern.compile(regex, flags);
        Matcher m = p.matcher(text);
        m.region(start, end);
        while(m.find()){
            int len = m.groupCount();
            if(len > 0){
                List<String> matches = new ArrayList<>(len);
                for(int i = 1; i <= len; ++i){
                    matches.add(m.group(i));
                }
                return matches;
            }
        }
        return null;
    }

    /**
     *
     * @param text 待匹配文本
     * @param regex 正则表达式
     * @return 返回所有匹配结果的集合
     */
    public static List<String> findMatches(String text, String regex){
        return findMatches(text, regex, 0);
    }

    public static List<String> findMatches(String text, String regex, int flags){
        return findMatches(text, regex, 0, text == null ? 0 : text.length(), flags);
    }

    public static List<String> findMatches(String text, String regex, int start, int end){
        return findMatches(text, regex, start, end, 0);
    }

    public static List<String> findMatches(String text, String regex, int start, int end, int flags){
        if(text == null){
            return null;
        }
        Pattern p = Pattern.compile(regex, flags);
        Matcher m = p.matcher(text);
        m.region(start, end);
        List<String> results = new ArrayList<>();
        while(m.find()){
            int len = m.groupCount();
            for(int i = 1; i <= len; ++i){
                results.add(m.group(i));
            }
        }
        return results;
    }

    /**
     *
     * @param text 待匹配文本
     * @param regex 正则表达式, 必须包含偶数个捕获组
     * @return 每两个依次相邻的匹配结果做为key value存入map中
     */
    public static LinkedHashMap<String, String> findMatchesMap(String text, String regex){
        return findMatchesMap(text, regex, 0);
    }

    public static LinkedHashMap<String, String> findMatchesMap(String text, String regex, int flags){
        return findMatchesMap(text, regex, false, flags);
    }

    /**
     *
     * @param text 待匹配文本
     * @param regex 正则表达式, 必须包含偶数个捕获组
     * @param reverse 是否将匹配到的key value进行反转存入map中
     * @return 每两个依次相邻的匹配结果做为key value存入map中, 如果配置reverse为<code>true</code>, 将value key存入map
     */
    public static LinkedHashMap<String, String> findMatchesMap(String text, String regex, boolean reverse){
        return findMatchesMap(text, regex, reverse, 0);
    }

    public static LinkedHashMap<String, String> findMatchesMap(String text, String regex, boolean reverse, int flags){
        return findMatchesMap(text, regex, reverse, 0, text == null ? 0 : text.length(), flags);
    }

    public static LinkedHashMap<String, String> findMatchesMap(String text, String regex, boolean reverse, int start, int end){
        return findMatchesMap(text, regex, reverse, start, end, 0);
    }

    public static LinkedHashMap<String, String> findMatchesMap(String text, String regex, boolean reverse, int start, int end, int flags){
        List<String> matchList = findMatches(text, regex, start, end, flags);
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        int size = CollectionUtils.size(matchList);
        if(size % 2 != 0) {
            throw new IllegalArgumentException("正则表达式" + regex + "捕获组为偶数个");
        }
        Iterator<String> itr = matchList.iterator();
        while(itr.hasNext()){
            String key = itr.next();
            String value = itr.next();
            if(reverse) {
                map.put(value, key);
            } else {
                map.put(key, value);
            }
        }
        return map;
    }

    public static boolean hasMatchIgnoreCase(String regex, String text) {
        return hasMatch(regex, text, Pattern.CASE_INSENSITIVE);
    }

    public static boolean hasMatch(String regex, String text) {
        return hasMatch(regex, text, 0);
    }

    public static boolean hasMatch(String regex, String text, int flags) {
        if(text == null) {
            return regex == null;
        }
        if(regex == null) {
            return false;
        }
        Matcher m = Pattern.compile(regex, flags).matcher(text);
        return m.find();
    }

    /**
     * 正则子串匹配
     * @param regex
     * @param texts
     * @return
     */
    public static boolean isAllHasMatches(String regex, String... texts) {
        return isAllHasMatches(regex, 0, texts);
    }

    public static boolean isAllHasMatchesIgnoreCase(String regex, String... texts) {
        return isAllHasMatches(regex, Pattern.CASE_INSENSITIVE, texts);
    }

    public static boolean isAllHasMatches(String regex, int flags, String... texts) {
        for(String text : texts) {
            if(!hasMatch(regex, text, flags)) {
                return false;
            }
        }
        return false;
    }

    /**
     * 正则全串匹配
     * @param regex
     * @param texts
     * @return
     */
    public static boolean isAllMatches(String regex, String... texts) {
        if(regex == null) {
            return ObjectUtils.isAllNull(texts);
        }

        for(String text : texts) {
            if(text == null) {
                return false;
            }
            if(!text.matches(regex)) {
                return false;
            }
        }
        return true;
    }

    public static String replaceFirst(String text, String regex, String replaceText) {
        return text == null ? text : text.replaceFirst(regex, replaceText);
    }

    public static String replaceAll(String text, String regex, String replaceText) {
        return text == null ? text : text.replaceAll(regex, replaceText);
    }

    public static String replace(String text, String regex, ReplaceCallback callback) {
        StringBuffer sb = new StringBuffer();
        Matcher m = Pattern.compile(regex).matcher(text);
        while(m.find()){
            int len = m.groupCount();
            if(len < 1) {
                continue;
            }
            int start = m.start();
            StringBuilder group = new StringBuilder(m.group(0));
            for(int i = len; i > 0; --i){
                String t = callback.toReplacement(m.group(i), i);
                if(t == null) {
                    t = "";
                } else {
                    t = t.replace("\\", "\\\\").replace("$", "\\$");
                }
                group.replace(m.start(i) - start, m.end(i) - start, t);
            }
            m.appendReplacement(sb, group.toString());
        }
        m.appendTail(sb);
        return sb.toString();
    }

    public static interface ReplaceCallback {

        String toReplacement(String groupText, int groupIndex);

    }
}
