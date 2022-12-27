package com.disney.teams.utils.timer;

import com.alibaba.fastjson.JSON;
import com.disney.teams.utils.thread.AutoRemove;
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
public class TimeCountUtilsTest {

    @Test
    public void start() throws Exception {
        AutoRemove ignored = TimeCountUtils.start();
        try {

        } finally {
            System.out.println(JSON.toJSONString(ignored.closeAndGet()));
        }
    }
}
