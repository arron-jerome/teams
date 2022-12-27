package com.disney.teams.utils.type;

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
public class ExceptionUtilsTest {

    @Test
    public void toString1() throws Exception {
        try {
            Integer i = null;
            i += 1;
            return;
        } catch (Exception e) {
            System.out.println(ExceptionUtils.toString(e));
        }
    }
}
