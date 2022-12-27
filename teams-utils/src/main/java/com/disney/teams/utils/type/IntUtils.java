package com.disney.teams.utils.type;

import java.util.Arrays;

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
public abstract class IntUtils {

    public static final int ZERO = 0;
    public static final int ONE = 1;

    private IntUtils(){}

    public static int parse(Object value){
        if(value == null){
            throw new NullPointerException();
        }
        if(value instanceof Number){
            return ((Number)value).intValue();
        }else{
            if(value instanceof Boolean){
                return ((Boolean)value) ? ONE : ZERO;
            } else {
                String str = value.toString();
                if(StringUtils.isBlank(str)){
                    return ZERO;
                }
                return Integer.parseInt(str);
            }
        }
    }

    public static int parse(Object value, int defaultValue){
        if(value == null){
            return defaultValue;
        }
        return parse(value);
    }

    public static int ifNull(Integer value, int defaultValue) {
        return value == null ? defaultValue : value;
    }
    
    public static Integer valueOf(Object value){
        if(value == null || "null".equals(value)){
            return null;
        } else if(value instanceof Integer){
            return (Integer)value;
        } else if(value instanceof String){
            if(StringUtils.isBlank((String)value)){
                return null;
            }
        }
        return parse(value);
    }

    public static Integer[] toArray(String data, String split){
        if(data == null || data.length() == 0){
            return null;
        }
        String[] arr = data.split(split);
        int i = 0, len = arr.length;
        Integer[] dataArr = new Integer[len];
        for(String str : arr) {
            if(StringUtils.isBlank(str)) {
                continue;
            }
            dataArr[i++] = Integer.valueOf(str);
        }
        return Arrays.copyOf(dataArr, i);
    }

    public static boolean gt(Integer source, int target){
        return source != null && source > target;
    }

    public static boolean gt(int source, Integer target){
        return target == null || source > target;
    }

    public static boolean gte(Integer source, int target){
        return source == null || source >= target;
    }

    public static boolean gte(int source, Integer target){
        return target != null && source >= target;
    }

    public static boolean lt(Integer source, int target){
        return source == null || source < target;
    }

    public static boolean lt(int source, Integer target){
        return target != null && source < target;
    }

    public static boolean lte(Integer source, int target){
        return source == null || source <= target;
    }

    public static boolean lte(int source, Integer target){
        return target != null && source <= target;
    }

    public static boolean gt0(Integer source){
        return gt(source, ZERO);
    }

    public static boolean gt1(Integer source){
        return gt(source, ONE);
    }

    public static boolean gte0(Integer source){
        return gte(source, ZERO);
    }

    public static boolean gte1(Integer source){
        return gte(source, ONE);
    }

    public static boolean lt0(Integer source){
        return lt(source, ZERO);
    }
    public static boolean lt1(Integer source){
        return lt(source, ONE);
    }
    public static boolean lte0(Integer source){
        return lte(source, ZERO);
    }
    public static boolean lte1(Integer source){
        return lte(source, ONE);
    }

    public static boolean between(int source, int min, int max){
        return min <= source && source <= max;
    }

    public static boolean between(Integer source, int min, int max){
        return lte(min, source) && lte(source, max);
    }

    public static boolean between(Integer source, Integer min, Integer max){
        return ObjectUtils.between(source, min, max);
    }

    public static int toInt(String text, int defaultInt){
        return text == null ? defaultInt : Integer.parseInt(text);
    }

    public static int toInteger(String text, Integer defaultInteger){
        return text == null ? defaultInteger : Integer.valueOf(text);
    }
}
