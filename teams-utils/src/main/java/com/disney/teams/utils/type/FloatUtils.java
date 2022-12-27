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
public abstract class FloatUtils {

    public static final float ZERO = 0f;
    public static final float ONE = 1f;

    private FloatUtils(){}

    public static float parse(Object value){
        if(value == null){
            throw new NullPointerException();
        }
        if(value instanceof Number){
            return ((Number)value).floatValue();
        }else{
            if(value instanceof Boolean){
                return (((Boolean)value) ? ONE : ZERO);
            } else {
                String str = value.toString();
                if(StringUtils.isBlank(str)){
                    return ZERO;
                }
                return Float.parseFloat(str);
            }
        }
    }
    
    public static float parse(Object value, float defaultValue){
        if(value == null){
            return defaultValue;
        }
        return parse(value);
    }

    public static float ifNull(Float value, float defaultValue) {
        return value == null ? defaultValue : value;
    }
    
    public static Float valueOf(Object value){
        if(value == null || "null".equals(value)){
            return null;
        } else if(value instanceof Float){
            return (Float)value;
        } else if(value instanceof String){
            if(StringUtils.isBlank((String)value)){
                return null;
            }
        }
        return parse(value);
    }

    public static Float[] toArray(String data, String split){
        if(data == null || data.length() == 0){
            return null;
        }
        String[] arr = data.split(split);
        int i = 0, len = arr.length;
        Float[] dataArr = new Float[len];
        for(String str : arr) {
            if(StringUtils.isBlank(str)) {
                continue;
            }
            dataArr[i++] = Float.valueOf(str);
        }
        return Arrays.copyOf(dataArr, i);
    }

    public static boolean gt(Float source, float target){
        return source != null && source > target;
    }

    public static boolean gt(float source, Float target){
        return target == null || source > target;
    }

    public static boolean gt0(Float source){
        return gt(source, ZERO);
    }

    public static boolean lt(Float source, float target){
        return source == null || source < target;
    }

    public static boolean lt(float source, Float target){
        return target != null && source < target;
    }

    public static boolean lte(Float source, float target){
        return source == null || source <= target;
    }

    public static boolean lte(float source, Float target){
        return target != null && source <= target;
    }

    public static boolean lt0(Float source){
        return lt(source, ZERO);
    }
    public static boolean lt1(Float source){
        return lt(source, ONE);
    }
    public static boolean lte0(Float source){
        return lte(source, ZERO);
    }
    public static boolean lte1(Float source){
        return lte(source, ONE);
    }

    public static boolean between(float source, float min, float max){
        return min <= source && source <= max;
    }

    public static boolean between(Float source, float min, float max){
        return lte(min, source) && lte(source, max);
    }

    public static boolean between(Float source, Float min, Float max){
        return ObjectUtils.between(source, min, max);
    }
}
