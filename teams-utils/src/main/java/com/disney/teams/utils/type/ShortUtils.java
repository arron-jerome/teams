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
public abstract class ShortUtils {

    private ShortUtils(){}

    public static final short ZERO = 0;
    public static final short ONE = 1;

    public static short parse(Object value){
        if(value == null){
            throw new NullPointerException();
        }
        if(value instanceof Number){
            return ((Number)value).shortValue();
        }else{
            if(value instanceof Boolean){
                return (((Boolean)value) ? ONE : ZERO);
            } else {
                String str = value.toString();
                if(StringUtils.isBlank(str)){
                    return ZERO;
                }
                return Short.parseShort(str);
            }
        }
    }
    
    public static short parse(Object value, short defaultValue){
        if(value == null){
            return defaultValue;
        }
        return parse(value);
    }

    public static short ifNull(Short value, short defaultValue) {
        return value == null ? defaultValue : value;
    }
    
    public static Short valueOf(Object value){
        if(value == null || "null".equals(value)){
            return null;
        } else if(value instanceof Short){
            return (Short)value;
        } else if(value instanceof String){
            if(StringUtils.isBlank((String)value)){
                return null;
            }
        }
        return parse(value);
    }

    public static Short[] toArray(String data, String split){
        if(data == null || data.length() == 0){
            return null;
        }
        String[] arr = data.split(split);
        int i, len = arr.length;
        Short[] dataArr = new Short[len];
        for(i = 0; i < len; ++i){
            dataArr[i] = Short.valueOf(arr[i]);
        }
        return dataArr;
    }

    public static boolean gt(Short source, short target){
        return source != null && source > target;
    }

    public static boolean gt(short source, Short target){
        return target == null || source > target;
    }

    public static boolean gte(Short source, short target){
        return source == null || source >= target;
    }

    public static boolean gte(short source, Short target){
        return target != null && source >= target;
    }

    public static boolean lt(Short source, short target){
        return source == null || source < target;
    }

    public static boolean lt(short source, Short target){
        return target != null && source < target;
    }

    public static boolean lte(Short source, short target){
        return source == null || source <= target;
    }

    public static boolean lte(short source, Short target){
        return target != null && source <= target;
    }

    public static boolean gt0(Short source){
        return gt(source, ZERO);
    }

    public static boolean gt1(Short source){
        return gt(source, ONE);
    }

    public static boolean gte0(Short source){
        return gte(source, ZERO);
    }

    public static boolean gte1(Short source){
        return gte(source, ONE);
    }

    public static boolean lt0(Short source){
        return lt(source, ZERO);
    }
    public static boolean lt1(Short source){
        return lt(source, ONE);
    }
    public static boolean lte0(Short source){
        return lte(source, ZERO);
    }
    public static boolean lte1(Short source){
        return lte(source, ONE);
    }

    public static boolean between(short source, short min, short max){
        return min <= source && source <= max;
    }

    public static boolean between(Short source, short min, short max){
        return lte(min, source) && lte(source, max);
    }

    public static boolean between(Short source, Short min, Short max){
        return ObjectUtils.between(source, min, max);
    }
}
