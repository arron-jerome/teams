package com.disney.teams.utils.type;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.junit.Assert;
import org.junit.Test;

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
public class ClassUtilsTest {

    private class Bean {
        Integer a;
        Integer b;

        public Bean(Integer a, Integer b) {
            this.a = a;
            this.b = b;
        }
    }

    @Test
    public void assiged(){
        Assert.assertEquals(true, Number.class.isAssignableFrom(Integer.class));
    }

    @Test
    public void convert(){
        Assert.assertEquals(true, Number.class.isAssignableFrom(Integer.class));
    }

//    @Test
//    public void enumTest() {
//        System.out.println(JSON.toJSONString(MatchMode.class.getEnumConstants(), SerializerFeature.PrettyFormat));
//        System.out.println(ClassUtils.convert(11.1, MatchMode.class));
//    }

    @Test
    public void copyProperties() throws Exception {
        Bean t = new Bean(1, 2);
        Bean t1 = new Bean(11, 22);
        Bean t2 = new Bean(12, null);

        ClassUtils.copyProperties(t1, t, ClassUtils.CopyCallback.COPY_IGNORE_NULL, "a");
        ClassUtils.copyProperties(t2, t1, ClassUtils.CopyCallback.COPY_IGNORE_NULL, "a", "b");
        Assert.assertEquals(Integer.valueOf(11), t.a);
        Assert.assertEquals(Integer.valueOf(2), t.b);
        Assert.assertEquals(Integer.valueOf(12), t1.a);
        Assert.assertEquals(Integer.valueOf(22), t1.b);
    }
}
