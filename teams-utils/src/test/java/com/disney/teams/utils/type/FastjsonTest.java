package com.disney.teams.utils.type;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.junit.Test;

import java.io.Serializable;
import java.lang.reflect.Type;

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
public class FastjsonTest {

//    public static abstract class Obj<T extends Serializable> {
//
//        private T number;
//
//        public T getNumber() {
//            return number;
//        }
//
//        public void setNumber(T number) {
//            this.number = number;
//        }
//
//        public T parse(String text) {
//            Type type = new TypeReference<T>(){}.getType();
//            return JSON.parseObject(text, type);
//        }
//    }
//
//    public static class LE<T extends Serializable> extends GenericEntity<T> {
//
//        private static final long serialVersionUID = 3463940519487661103L;
//        private T id;
//
//        @Override
//        public T getId() {
//            return id;
//        }
//
//        @Override
//        public void setId(T id) {
//            this.id = id;
//        }
//    }
//
//    public static class RealObj<ID extends Serializable> extends Obj<LE<ID>> {
//
//    }
//
//    @Test
//    public void fastjsonTest() {
////        Obj<Long> obj = new Obj<Long>(){};
//        RealObj<Long> obj = new RealObj<>();
//        String text = "{\"id\":\"111\"}";
//        GenericEntity<Long> parse = obj.parse(text);
//        System.out.println(JSON.toJSONString(parse, SerializerFeature.PrettyFormat));
//
////        System.out.println(JSON.toJSONString(ClassUtils.convert(JSON.parseObject(text), obj.getClass())));
////        Type[] type = ClassUtils.getActualType(obj.getClass(), Obj.class);
////        System.out.println(type);
//    }

}
