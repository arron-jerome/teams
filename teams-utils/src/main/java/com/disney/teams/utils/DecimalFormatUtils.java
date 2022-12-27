package com.disney.teams.utils;

import com.disney.teams.exception.BasicRuntimeException;

import java.text.DecimalFormat;
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
public class DecimalFormatUtils {

    private static final ThreadLocal<Map<Integer, DecimalFormat>> formatCache = new ThreadLocal<>();

    private static String format(Number number, final int keepFraction, char keep) {
        if (number == null) {
            return null;
        }
        StringBuilder format = new StringBuilder("0");
        int kf = Math.abs(keepFraction);
        if (kf > 0) {
            format.append('.');
            while (--kf > -1) {
                format.append(keep);
            }
        }
        Map<Integer, DecimalFormat> dfMap = formatCache.get();
        if (dfMap == null) {
            dfMap = new HashMap<>();
            formatCache.set(dfMap);
        }
        DecimalFormat df = dfMap.get(keepFraction);
        if (df == null) {
            df = new DecimalFormat(format.toString());
            dfMap.put(keepFraction, df);
        }
        return df.format(number);
    }

    public static String format(Number number, int keepFraction) {
        if (keepFraction < 0) {
            throw new BasicRuntimeException("keepFraction is must greater equal than 0");
        }
        return format(number, keepFraction, '#');
    }

    public static String formatWithZero(Number number, int keepFraction) {
        if (keepFraction < 0) {
            throw new BasicRuntimeException("keepFraction is must greater equal than 0");
        }
        return format(number, -keepFraction, '0');
    }

    public static String formatTwoFraction(Number number) {
        return format(number, 2);
    }

    public static String formatTwoFractionWithZero(Number number) {
        return formatWithZero(number, 2);
    }
}
