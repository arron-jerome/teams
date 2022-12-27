package com.disney.teams.utils.type;

import org.junit.Assert;
import org.junit.Test;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

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
public class MethodUtilsTest {

//    public static class User  {
//
//        public static final String[] fname = {"a", "c", "f", "b"};
//        public static final String[] mname = {"getA", "getF", "setA", "setB", "setC", "setF", "getC", "getB"};
//
//
//        private int c;
//
//        @MemberOrder
//        private byte b;
//
//        private char f;
//
//        private String a;
//
//        @MemberOrder
//        public int getC() {
//            return c;
//        }
//
//        public void setC(int c) {
//            this.c = c;
//        }
//
//        @MemberOrder(1)
//        public byte getB() {
//            return b;
//        }
//
//        public void setB(byte b) {
//            this.b = b;
//        }
//
//        public char getF() {
//            return f;
//        }
//
//        public void setF(char f) {
//            this.f = f;
//        }
//
//        public String getA() {
//            return a;
//        }
//
//        public void setA(String a) {
//            this.a = a;
//        }
//    }
//
//    public static interface Itf<ID extends Serializable, PO extends GenericEntity<ID>> {
//        ID save(PO po);
//        ID save(Long po);
//    }
//
//    public static class Impl implements Itf<Long, LongEntity>{
//
//        @Override
//        public Long save(LongEntity longEntity) {
//            return null;
//        }
//
//        @Override
//        public Long save(Long po) {
//            return null;
//        }
//    }
//
//
//    @Test
//    public void isSame() throws Exception {
//        List<Method> methodList = MethodUtils.getMethods(Impl.class);
//        Assert.assertEquals(2, CollectionUtils.size(methodList));
//    }
//
//    @Test
//    public void testOrder() {
//        List<Field> fields = FieldUtils.getFields(User.class, Boolean.FALSE);
//        MemberUtils.sort(fields);
//        Assert.assertEquals(User.fname.length, CollectionUtils.size(fields));
//        for(int i = 0; i < User.fname.length; ++i) {
//            Field field = fields.get(i);
//            Assert.assertEquals(String.format("%s is not equals name with %s", User.fname[i], field), User.fname[i], field.getName());
//        }
//
//        List<Method> methods = MethodUtils.getMethods(User.class);
//        MemberUtils.sort(methods);
//        Assert.assertEquals(User.mname.length, CollectionUtils.size(methods));
//        for(int i = 0; i < User.mname.length; ++i) {
//            Method method = methods.get(i);
//            Assert.assertEquals(String.format("%s is not equals name with %s", User.mname[i], method), User.mname[i], method.getName());
//        }
//
//    }

    @Test
    public void test() {
        System.out.println((Object)MethodUtils.invokeStaticMethod(StringUtils.class, "getUuid"));
        System.out.println((Object)MethodUtils.invokeStaticMethod(StringUtils.class, "hasBlank") + "--" + StringUtils.hasBlank());
        System.out.println((Object)MethodUtils.invokeStaticMethod(StringUtils.class, "hasBlank", "a", "b") + "--" + StringUtils.hasBlank("a", "b"));
        System.out.println((Object)MethodUtils.invokeStaticMethod(StringUtils.class, "hasBlank", "a") + "--" + StringUtils.hasBlank("a"));
        System.out.println((Object)MethodUtils.invokeStaticMethod(StringUtils.class, "hasBlank", "") + "--" + StringUtils.hasBlank(""));
        System.out.println((Object)MethodUtils.invokeStaticMethod(ArrayUtils.class, "length", new int[]{1}) + "--" + ArrayUtils.length(new int[]{1}));
    }
}
