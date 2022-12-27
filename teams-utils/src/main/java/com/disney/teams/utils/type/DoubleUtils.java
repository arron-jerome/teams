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
public abstract class DoubleUtils {

    public static final double ZERO = 0d;
    public static final double ONE = 1d;

    private DoubleUtils(){}

    public static double parse(Object value){
        if(value == null){
            throw new NullPointerException();
        }
        if(value instanceof Number){
            return ((Number)value).doubleValue();
        }else{
            if(value instanceof Boolean){
                return (((Boolean)value) ? ONE : ZERO);
            } else {
                String str = value.toString();
                if(StringUtils.isBlank(str)){
                    return ZERO;
                }
                return Double.parseDouble(str);
            }
        }
    }
    
    public static double parse(Object value, double defaultValue){
        if(value == null){
            return defaultValue;
        }
        return parse(value);
    }

    public static double ifNull(Double value, double defaultValue) {
        return value == null ? defaultValue : value;
    }
    
    public static Double valueOf(Object value){
        if(value == null || "null".equals(value)){
            return null;
        } else if(value instanceof Double){
            return (Double)value;
        } else if(value instanceof String){
            if(StringUtils.isBlank((String)value)){
                return null;
            }
        }
        return parse(value);
    }

    public static Double[] toArray(String data, String split){
        if(data == null || data.length() == 0){
            return null;
        }
        String[] arr = data.split(split);
        int i = 0, len = arr.length;
        Double[] dataArr = new Double[len];
        for(String str : arr) {
            if(StringUtils.isBlank(str)) {
                continue;
            }
            dataArr[i++] = Double.valueOf(str);
        }
        return Arrays.copyOf(dataArr, i);
    }

    public static boolean gt(Double source, double target){
        return source != null && source > target;
    }

    public static boolean gt(double source, Double target){
        return target == null || source > target;
    }

    public static boolean gte(Double source, double target){
        return source == null || source >= target;
    }

    public static boolean gte(double source, Double target){
        return target != null && source >= target;
    }

    public static boolean lt(Double source, double target){
        return source == null || source < target;
    }

    public static boolean lt(double source, Double target){
        return target != null && source < target;
    }

    public static boolean lte(Double source, double target){
        return source == null || source <= target;
    }

    public static boolean lte(double source, Double target){
        return target != null && source <= target;
    }

    public static boolean gt0(Double source){
        return gt(source, ZERO);
    }

    public static boolean gt1(Double source){
        return gt(source, ONE);
    }

    public static boolean gte0(Double source){
        return gte(source, ZERO);
    }

    public static boolean gte1(Double source){
        return gte(source, ONE);
    }

    public static boolean lt0(Double source){
        return lt(source, ZERO);
    }
    public static boolean lt1(Double source){
        return lt(source, ONE);
    }
    public static boolean lte0(Double source){
        return lte(source, ZERO);
    }
    public static boolean lte1(Double source){
        return lte(source, ONE);
    }

    public static boolean between(double source, double min, double max){
        return min <= source && source <= max;
    }

    public static boolean between(Double source, double min, double max){
        return lte(min, source) && lte(source, max);
    }

    public static boolean between(Double source, Double min, Double max){
        return ObjectUtils.between(source, min, max);
    }
}
