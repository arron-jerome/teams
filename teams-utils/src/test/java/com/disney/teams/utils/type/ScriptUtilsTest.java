package com.disney.teams.utils.type;

import com.disney.teams.utils.ScriptUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
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
public class ScriptUtilsTest {

    @Test
    public void replaceText() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("key", "aa%saa%s");
        map.put("v", "b");

        String[] texts = {
                "${String.format(key, v, \"b\\\"b\")}"
        };
        String[] values = {
                "aabaab\"b"
        };
        for (int i = 0; i < values.length; ++i) {
            String v = ScriptUtils.replaceText(texts[i], map);
            Assert.assertEquals(values[i], v);
        }
    }
}
