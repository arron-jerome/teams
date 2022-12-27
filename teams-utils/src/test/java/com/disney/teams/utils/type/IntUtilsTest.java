package com.disney.teams.utils.type;

import org.junit.Assert;
import org.junit.Test;

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
public class IntUtilsTest {

    @Test
    public void toArray() throws Exception {
        String[] aa = {
            ",1,2,3,",
            ",1,2,3",
            "1,2,3,",
            "1,2,,3,",
            "1,2,3"
        };
        Integer[] excepted = {1, 2, 3};
        for(String a : aa) {
            Assert.assertArrayEquals(excepted, IntUtils.toArray(a, ","));
        }
    }

}
