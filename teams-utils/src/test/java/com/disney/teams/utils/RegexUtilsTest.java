package com.disney.teams.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


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
public class RegexUtilsTest {

    @Test
    public void findPosition() throws Exception {
        String text = "    private Integer name;";
        String regex = "^\\s*private\\s*\\w+\\s*(\\w+)\\s*;\\s*$";
        int[] result = RegexUtils.findPosition(text, regex);
        Assert.assertEquals(text.indexOf("name"), result[0]);
        Assert.assertEquals(text.indexOf("name") + 4, result[1]);
    }

    @Test
    public void findMatch() throws Exception {
        String text = "    private Integer name;";
        String regex = "^\\s*private\\s*\\w+\\s*(\\w+)\\s*;\\s*$";
        String result = RegexUtils.findMatch(text, regex);
        Assert.assertEquals("name", result);
    }

    @Test
    public void hasMatch() throws Exception {
        String text = "    private Integer name;";
        String regex = "[，。、~]";
        boolean hasMatch = RegexUtils.hasMatch(regex, text);
        Assert.assertEquals(true, hasMatch);
    }

    @Test
    public void findMatches() throws Exception {
        String text = "    private Integer name;";
        String regex = "^\\s*private\\s*\\w+\\s*(\\w+)\\s*;\\s*$";
        List<String> result = RegexUtils.findMatches(text, regex);
        Assert.assertEquals("name", result.get(0));
    }

    @Test
    public void findMatchesMap() throws Exception {
        String text = "{\"key\":\"value\"}";

        String regex = "\"(\\w+)\":\"(\\S+)\"";
        Map<String, String> map = RegexUtils.findMatchesMap(text, regex);
        Assert.assertEquals("value", map.get("key"));

        Map<String, String> reverseMap = RegexUtils.findMatchesMap(text, regex, true);
        Assert.assertEquals("key", reverseMap.get("value"));

        String regexIllegal = "\"\\w+\":\"(\\S+)\"";
        try {
            map = RegexUtils.findMatchesMap(text, regexIllegal);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void replace() {
        final String[] replacement = {"aa", "bb", "cc"};
        String text = "abc938fgx936";
        String t = RegexUtils.replace(text, "(9)(3)(8)", (groupText, groupIndex) -> groupText + replacement[groupIndex - 1]);
        System.out.println(t);
    }

    @Test
    public void test(){

    }


}
