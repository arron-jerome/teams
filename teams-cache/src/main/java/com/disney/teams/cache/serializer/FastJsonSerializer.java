package com.disney.teams.cache.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.disney.teams.utils.type.ArrayUtils;
import com.disney.teams.utils.type.ClassUtils;
import com.disney.teams.utils.type.StringUtils;

/**
 * @author arron.zhou
 * @version 1.0.0
 * @date 2022/12/20
 * Description:对象序列化接口,对象序列化为fast json字符流
 * Modification  History:
 * Date         Author        Version        Description
 * ------------------------------------------------------
 * 2022/12/20       arron.zhou      1.0.0          create
 */
public class FastJsonSerializer extends AbstractStringSerializer {

    static {
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
    }

    private static final Class<?>[] PARSE_CLASSES = {
            boolean[].class,
            char[].class,
            byte[].class,
            short[].class,
            int[].class,
            long[].class,
            float[].class,
            double[].class
    };

    @Override
    public <T> T unSerialize(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        Object v = JSON.parse(value);
        if (v == null) {
            return null;
        }
        return (T) v;
    }

    @Override
    public <T> T unSerialize(String value, Class<T> clazz) {
        if (StringUtils.isBlank(value)) {
            return ClassUtils.convert(null, clazz);
        }
        if (String.class.equals(clazz)) {
            try {
                return JSON.parseObject(value, clazz);
            } catch (JSONException e) {
                return (T) value;
            }
        } else if (ArrayUtils.in(clazz, PARSE_CLASSES)) {
            return JSON.parseObject(value, clazz);
        }

        Object obj = JSON.parse(value);
        if (obj instanceof JSONObject) {
            JSONObject json = (JSONObject) obj;
            final String typeKey = JSON.DEFAULT_TYPE_KEY;
            String className = json.getString(typeKey);
            //类型不同时，去掉类型
            if (clazz != null && !clazz.getName().equals(className)) {
                json.remove(typeKey);
            }
            return JSON.toJavaObject(json, clazz);
        } else if (obj instanceof JSON) {
            return JSON.toJavaObject((JSON) obj, clazz);
        } else {
            return ClassUtils.convert(obj, clazz);
        }
    }

    @Override
    public <T> String serialize(T value) {
        if (value == null) {
            return null;
        }
        return JSON.toJSONString(value, SerializerFeature.WriteClassName);
    }
}
