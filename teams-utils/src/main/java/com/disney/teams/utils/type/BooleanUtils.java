package com.disney.teams.utils.type;

import java.util.Arrays;

/**
 * @author arron.zhou
 * @version 1.0.0
 * @date 2022/12/16
 * Modification  History:
 * Date         Author        Version        Description
 * ------------------------------------------------------
 * 2022/12/16       arron.zhou      1.0.0          create
 */
public abstract class BooleanUtils {

    private BooleanUtils(){}

    public static boolean parse(Object value, boolean defaultValue){
        if(value instanceof Boolean){
            return Boolean.TRUE.equals(value);
        } else if(value instanceof Number){
            return ((Number)value).intValue() == 1;
        } else if(value == null){
            return defaultValue;
        } else {
            String str = value.toString();
            if(StringUtils.isBlank(str)){
                return defaultValue;
            }
            return "true".equalsIgnoreCase(str);
        }
    }

    public static boolean parse(Object value){
        return parse(value, false);
    }

    public static boolean ifNull(Boolean value, boolean defaultValue) {
        return value == null ? defaultValue : value;
    }
    
    public static Boolean valueOf(Object value){
        if(value == null || "null".equals(value)){
            return null;
        } else if(value instanceof String){
            if(StringUtils.isBlank((String)value)){
                return null;
            }
        }
        return parse(value) ? Boolean.TRUE : Boolean.FALSE;
    }

    public static Boolean[] toArray(String data, String split){
        if(data == null || data.length() == 0){
            return null;
        }
        String[] arr = data.split(split);
        int i = 0, len = arr.length;
        Boolean[] dataArr = new Boolean[len];
        for(String str : arr) {
            if(StringUtils.isBlank(str)) {
                continue;
            }
            dataArr[i++] = Boolean.valueOf(str);
        }
        return Arrays.copyOf(dataArr, i);
    }

    public static Boolean and(Boolean a, Boolean b){
        if(a == null){
            return b;
        }
        if(b == null){
            return a;
        }
        return (a && b) ? Boolean.TRUE : Boolean.FALSE;
    }

    public static Boolean or(Boolean a, Boolean b){
        if(a == null){
            return b;
        }
        if(b == null){
            return a;
        }
        return (a || b) ? Boolean.TRUE : Boolean.FALSE;
    }

}
