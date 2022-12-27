package com.disney.teams.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.disney.teams.utils.type.*;

import java.util.List;
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
public class FastJsonUtils {

    private static final String indexRegex = "\\[(\\d+)\\]";
    public static final String MAPPING_KEY_NAME = "mapping";
    public static final String AFTER_MAPPING_KEY_NAME = "afterMapping";
    public static final String TARGET_KEY_NAME = "_target";
    public static final String FORMAT_KEY_NAME = "format";
    public static final String JSON_PARSE_KEY_NAME = "json_parse_key";
    public static final String VALUE_KEY_NAME = "_value";

    public static boolean isEmpty(Object json) {
        if (json == null) {
            return true;
        } else if (json instanceof JSONObject) {
            return ((JSONObject) json).size() < 1;
        } else if (json instanceof JSONArray) {
            return ((JSONArray) json).size() < 1;
        } else {
            return true;
        }
    }

    private static JSONArray get(JSONArray array, int i) {
        if(CollectionUtils.isEmpty(array)) {
            return null;
        }
        return array.size() > i ? array.getJSONArray(i) : null;
    }

    private static <T> T getBySingleKey(Object json, String key, Class<T> clazz) {
        if (StringUtils.isBlank(key)) {
            return null;
        }

        if (!(json instanceof JSON)) {
            json = JSON.toJSON(json);
        }

        if (isEmpty(json)) {
            return null;
        }

        List<String> indexs = RegexUtils.findMatches(key, indexRegex);
        if (CollectionUtils.isEmpty(indexs)) {
            return ((JSONObject) json).getObject(key, clazz);
        }

        int start = key.indexOf('[');
        JSONArray ja;
        if (start < 1) {
            ja = (JSONArray) json;
        } else {
            ja = ((JSONObject) json).getJSONArray(key.substring(0, start));
            if (CollectionUtils.isEmpty(ja)) {
                return null;
            }
        }
        int last = indexs.size() - 1;
        for (int j = 0; j < last; ++j) {
            ja = get(ja, Integer.parseInt(indexs.get(j)));
            if (CollectionUtils.isEmpty(ja)) {
                return null;
            }
        }
        return ja.getObject(last, clazz);
    }

    private static JSONObject getJSONObjectBySingleKey(JSON json, String key) {
        if (StringUtils.isBlank(key) || isEmpty(json)) {
            return null;
        }

        List<String> indexs = RegexUtils.findMatches(key, indexRegex);
        if (CollectionUtils.isEmpty(indexs)) {
            return ((JSONObject) json).getJSONObject(key);
        }

        int start = key.indexOf('[');
        JSONArray ja;
        if (start < 1) {
            ja = (JSONArray) json;
        } else {
            ja = ((JSONObject) json).getJSONArray(key.substring(0, start));
            if (CollectionUtils.isEmpty(ja)) {
                return null;
            }
        }
        int last = indexs.size() - 1;
        for (int j = 0; j < last; ++j) {
            ja = get(ja, Integer.parseInt(indexs.get(j)));
            if (CollectionUtils.isEmpty(ja)) {
                return null;
            }
        }
        return ja.getJSONObject(last);
    }

    private static Object getBySingleKey(Object json, String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }

        if (!(json instanceof JSON)) {
            json = JSON.toJSON(json);
        }

        if (isEmpty(json)) {
            return null;
        }

        List<String> indexs = RegexUtils.findMatches(key, indexRegex);
        if (CollectionUtils.isEmpty(indexs)) {
            return ((JSONObject) json).get(key);
        }

        int start = key.indexOf('[');
        JSONArray ja;
        if (start < 1) {
            ja = (JSONArray) json;
        } else {
            ja = ((JSONObject) json).getJSONArray(key.substring(0, start));
            if (CollectionUtils.isEmpty(ja)) {
                return null;
            }
        }
        int last = indexs.size() - 1;
        for (int j = 0; j < last; ++j) {
            ja = get(ja, Integer.parseInt(indexs.get(j)));
            if (CollectionUtils.isEmpty(ja)) {
                return null;
            }
        }

        int lastIndex = Integer.parseInt(indexs.get(last));
        return CollectionUtils.size(ja) > lastIndex ? ja.get(lastIndex) : null;
    }

    private static Object getPreJSON(Object json, String[] keyArray, int last) {
        for (int i = 0; i < last; ++i) {
            json = getBySingleKey(json, keyArray[i]);
        }
        return json;
    }

    public static String getStringByKeys(JSONObject json, String keys) {
        if (keys == null) {
            return null;
        }
        String[] keyArray = keys.split("\\.");
        return getString(json, keyArray);
    }

    public static String getString(JSON json, String... keys) {
        Object obj = get(json, keys);
        return TypeUtils.castToString(obj);
    }

    public static Object getByKeys(JSON json, String keys) {
        if (keys == null) {
            return null;
        }
        String[] keyArray = keys.split("\\.");
        return get(json, keyArray);
    }

    public static Object get(JSON json, String... keys) {
        if (ArrayUtils.isEmpty(keys)) {
            return null;
        }
        int ilast = keys.length - 1;
        Object rs = json;
        if (ilast > 0) {
            rs = getPreJSON(json, keys, ilast);
        }
        return getBySingleKey(rs, keys[ilast]);
    }

    public static <T> T getByKeys(JSONObject json, String keys, Class<T> clazz) {
        if (keys == null) {
            return null;
        }
        String[] keyArray = keys.split("\\.");
        return get(json, clazz, keyArray);
    }

    public static <T> T get(JSON json, Class<T> clazz, String... keys) {
        if (ArrayUtils.isEmpty(keys)) {
            return null;
        }
        int ilast = keys.length - 1;
        Object rs = json;
        if (ilast > 0) {
            rs = getPreJSON(json, keys, ilast);
        }

        return getBySingleKey(rs, keys[ilast], clazz);
    }

    public static JSONObject getJSONObject(JSONObject json, String... keys) {
        return (JSONObject) get(json, keys);
    }

    public static JSONArray getJSONArray(JSONObject json, String... keys) {
        return (JSONArray) get(json, keys);
    }

    public static <T> List<T> getList(JSONObject json, Class<T> clazz, String... keys) {
        JSONArray array = (JSONArray) get(json, keys);
        return array == null ? null : array.toJavaList(clazz);
    }

    public static Object filter(JSON json, String fieldMappingJson) {
        if (StringUtils.isBlank(fieldMappingJson)) {
            return json;
        }
        Object mapping = JSON.parse(fieldMappingJson);
        return filter(json, mapping);
    }

    public static <T> T filter(Object json, String fieldMappingJson, Class<T> clazz) {
        if (StringUtils.isBlank(fieldMappingJson)) {
            return ClassUtils.convert(json, clazz);
        }
        Object mapping = JSON.parse(fieldMappingJson);
        return filter(json, mapping, clazz);
    }

    public static <T> T filter(Object json, Object fieldMappingJson, Class<T> clazz) {
        if (fieldMappingJson == null) {
            return ClassUtils.convert(json, clazz);
        }
        Object obj = filter(json, fieldMappingJson);
        return ClassUtils.convert(obj, clazz);
    }

    private static Object parseJsonByKey(Object value, String jsonParseKey) {
        if (StringUtils.isBlank(jsonParseKey)) {
            return value;
        }
        if(!(value instanceof JSON)) {
            return null;
        }
        value = getByKeys((JSON) value, jsonParseKey);
        if (value instanceof String) {
            return JSON.parse((String) value);
        } else {
            return value;
        }
    }

    private static Object filterMapping(JSON json, JSONObject mapping) {
        String jsonParse = mapping.getString(JSON_PARSE_KEY_NAME);
        Object parsedJson = parseJsonByKey(json, jsonParse);

        String targetKey = mapping.getString(TARGET_KEY_NAME);
        String format = mapping.getString(FORMAT_KEY_NAME);
        Object map = mapping.get(MAPPING_KEY_NAME);

        boolean targetBlank = StringUtils.isBlank(targetKey), formatBlank = StringUtils.isBlank(format);
        if (targetBlank && formatBlank && map == null) {
            return filter((JSONObject) parsedJson, mapping);
        }
        Object target;
        if(StringUtils.isBlank(targetKey)) {
            target = filter(parsedJson, map);
        } else {
            Object targetJson = getByKeys((JSON) parsedJson, targetKey);
            target = filter(targetJson, map);
        }

        Object afterMapping = mapping.get(AFTER_MAPPING_KEY_NAME);
        if(afterMapping instanceof JSONObject) {
            target = filter(target, afterMapping);
        } else if(afterMapping  instanceof JSONArray) {
            for(Object obj : (JSONArray) afterMapping) {
                target = filter(target, obj);
            }
        }

        if (formatBlank) {
            return target;
        }

        if (target instanceof JSONObject) {
            return ScriptUtils.replaceText(format, (JSONObject) target);
        } else {
            return ScriptUtils.replaceText(format, MapUtils.newHashMap(StringUtils.isBlank(targetKey) ?
                VALUE_KEY_NAME : targetKey, target));
        }
    }

    private static Object filter(Object json, Object fieldMappingJson) {
        if (fieldMappingJson instanceof JSONArray) {
            json = JSON.toJSON(json);
            if(json instanceof JSONArray) {
                return filter((JSONArray) json, (JSONArray) fieldMappingJson);
            } else {
                JSONArray array = null;
                if(json != null) {
                    array = new JSONArray(1);
                    array.add(json);
                }
                return filter(array, (JSONArray) fieldMappingJson);
            }
        } else if (fieldMappingJson instanceof JSONObject) {
            JSONObject mapping = (JSONObject) fieldMappingJson;
            return filterMapping((JSON) JSON.toJSON(json), mapping);
        } else if (fieldMappingJson instanceof String){
            json = JSON.toJSON(json);
            Map<String, Object> valueMap;
            if(json instanceof JSONObject) {
                valueMap = (JSONObject) json;
            } else {
                valueMap = MapUtils.newHashMap(VALUE_KEY_NAME, json);
            }
            return ScriptUtils.eval(fieldMappingJson.toString(), valueMap);
        } else {
            return json;
        }
    }

    public static JSONArray filter(JSONArray json, JSONArray fieldMappingJson) {
        if (CollectionUtils.isEmpty(json) || CollectionUtils.isEmpty(fieldMappingJson)) {
            return json;
        }

        Object mapping = fieldMappingJson.get(0);
        int len = json.size();
        if (mapping instanceof JSONArray) {
            JSONArray array = new JSONArray(len);
            JSONArray arrayMapping = (JSONArray) mapping;
            for (int i = 0; i < len; ++i) {
                array.add(filter(json.getJSONArray(i), arrayMapping));
            }
            return array;
        } else if (mapping instanceof JSONObject) {
            JSONArray array = new JSONArray(len);
            JSONObject objectMapping = (JSONObject) mapping;
            for (int i = 0; i < len; ++i) {
                Object val = json.get(i);
                //如果想把常量数组转对象数组，可以用${i}获取数组元素
                if(!(val instanceof JSONObject)) {
                    JSONObject object = new JSONObject();
                    object.put("i", val);
                    val = object;
                }
                array.add(filter((JSONObject) val, objectMapping));
            }
            return array;
        } else if (mapping instanceof String) {
            JSONArray array = new JSONArray(len);
            String objectMapping = (String) mapping;
            for (int i = 0; i < len; ++i) {
                Object val = json.get(i);
                if(!(val instanceof JSONObject)) {
                    JSONObject object = new JSONObject();
                    object.put("i", val);
                    val = object;
                }
                array.add(StringUtils.replaceText(objectMapping, (JSONObject) val));
            }
            return array;
        } else {
            return json;
        }
    }

    public static JSONObject filter(JSONObject json, JSONObject fieldMappingJson) {
        if (MapUtils.isEmpty(json) || MapUtils.isEmpty(fieldMappingJson)) {
            return json;
        }
        final JSONObject mapping = new JSONObject(fieldMappingJson.size());
        for (String key : fieldMappingJson.keySet()) {
            Object obj = fieldMappingJson.get(key);
            if (obj instanceof String) {
                mapping.put(key, getByKeys(json, StringUtils.defaultIfBlank((String) obj, key)));
            } else if (obj instanceof JSONObject) {
                JSONObject map = (JSONObject) obj;
                Object target = filterMapping(json, map);
                mapping.put(key, target);
            } else {
                throw new IllegalArgumentException(key + "只能对应字符串或对象");
            }
        }
        return mapping;
    }
}
