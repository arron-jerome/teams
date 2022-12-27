package com.disney.teams.utils.type;

import com.alibaba.fastjson.JSON;
import com.disney.teams.utils.ScriptUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

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
public class StringUtilsTest {


    @Test
    public void containsBySplitChar() throws Exception {
        String text = "aa,bcb,cd";
        String[] subText = {"a", "b", "aa", "bcb", "cd", "aa,bcb,cd", "bcb,cd", "abdgd"};
        boolean[] expect = {false, false, true, true, true, true, true, false};
        int i = 0;
        for (String sub : subText) {
            Assert.assertEquals(expect[i++], StringUtils.containsBySplitChar(text, sub, ','));
        }
    }

    @Test
    public void replaceText() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("key", "aabaab");
        map.put("v", "b");

        String[] texts = {
                "${key}-${v}"
        };
        String[] values = {
                "aabaab-b"
        };
        for (int i = 0; i < values.length; ++i) {
            String v = ScriptUtils.replaceText(texts[i], map);
            Assert.assertEquals(values[i], v);
        }
    }

    @Test
    public void random() {
        String randomId = StringUtils.random(100000);
        System.out.println(randomId);
    }

    @Test
    public void byteSubstring() {
        String[] tests = {"1海淘", "1海淘", "yht", "", null};
        int[] testIndex = {4, 3, 3, 3, 3};
        String[] expects = {"1海", "1", "yht", "", null};

        for (int i = 0; i < tests.length; i++) {
            String real = StringUtils.byteSubstring(tests[i], 0, testIndex[i]);
            Assert.assertEquals(expects[i], real);
        }
    }

    @Test
    public void compareNumber() throws Exception {
        String[][] tests = {
                {"0", "0", "0"},
                {"0", "1", "-1"},
                {"1", "0", "1"},
                {"11.11", "111.1", "-1"},
                {"111.11", "111.1", "1"},
                {"111.11", "111.111", "-1"},
                {"111.11", "111.11", "0"},
                {"111", "123", "-1"},
                {"111", "111", "0"},
                {"0111", "111", "0"}
        };
        for (String[] test : tests) {
            Assert.assertEquals(JSON.toJSONString(test), test[2], Integer.toString(StringUtils.compareNumber(test[0], test[1])));
        }

    }
}
