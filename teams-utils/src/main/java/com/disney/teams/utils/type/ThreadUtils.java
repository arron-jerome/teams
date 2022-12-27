package com.disney.teams.utils.type;


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
public class ThreadUtils {
    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
//            e.printStackTrace();
        }
    }
}
