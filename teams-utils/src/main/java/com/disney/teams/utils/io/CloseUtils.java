package com.disney.teams.utils.io;

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
public class CloseUtils {
    public static void close(AutoCloseable c) {
        if (c != null) {
            try {
                c.close();
            } catch (Exception e) {
            }
        }
    }

    public static void close(AutoCloseable... cs) {
        for (AutoCloseable c : cs) {
            if (c != null) {
                try {
                    c.close();
                } catch (Exception e) {
                }
            }
        }
    }
}
