package com.disney.teams.utils.type;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.junit.Test;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.*;
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
public class TypeUtilsTest {

    public static class Obj<T> {

        private T number;

        public T getNumber() {
            return number;
        }

        public void setNumber(T number) {
            this.number = number;
        }

        public Obj<T> parse(String text) {
            Type type = new TypeReference<Obj<T>>(){}.getType();
            return JSON.parseObject(text, type);
        }
    }

    public static class Condtion {
        private String name;
        private Object value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }

//    @Test
//    public void fastjsonTest() {
//        Obj<Condition> obj = new Obj<>();
//        String text = "{\"number\":{\"name\":\"aaa\",\"value\":1}}";
//        Obj<Condition> parse = obj.parse(text);
//        System.out.println(JSON.toJSONString(parse, SerializerFeature.PrettyFormat));
//    }

    public static class Bean<ID extends Serializable> {
        private ID id;
        private String name; //Date
        private Collection<Bean<ID>> childrenList; //Bean<ID>[]
        private Bean<ID>[][] childrenArray; //Collection<Bean<ID>[]>
        private Map<ID, Bean<ID>> childrenMap;

        public Bean(){}

        public ID getId() {
            return id;
        }

        public void setId(ID id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Collection<Bean<ID>> getChildrenList() {
            return childrenList;
        }

        public void setChildrenList(Collection<Bean<ID>> childrenList) {
            this.childrenList = childrenList;
        }

        public Map<ID, Bean<ID>> getChildrenMap() {
            return childrenMap;
        }

        public void setChildrenMap(Map<ID, Bean<ID>> childrenMap) {
            this.childrenMap = childrenMap;
        }

        public Bean<ID>[][] getChildrenArray() {
            return childrenArray;
        }

        public void setChildrenArray(Bean<ID>[][] childrenArray) {
            this.childrenArray = childrenArray;
        }

        @Override
        public String toString() {
            return "Bean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", childrenList=" + childrenList +
                ", childrenArray=" + Arrays.toString(childrenArray) +
                ", childrenMap=" + childrenMap +
                '}';
        }
    }

    @Test
    public void testBeanConvert() throws Exception {
        Bean<Long> bean = new Bean<>();
        bean.setId(100L);
        bean.setName("parent");

        Bean<Long> children = new Bean<>();
        children.setId(101L);
        children.setName("children");

        Bean<Long> children2 = new Bean<>();
//        children2.setId(102L);
        children2.setName("children2");

        Bean[][] beans = {
            { children },
            { children, children2 }
        };
        bean.setChildrenArray(beans);

        bean.setChildrenList(ArrayUtils.toList(children, children2));
        Map<Long, Bean<Long>> map = new HashMap<>(2);
        map.put(children.getId(), children);
        map.put(children2.getId(), children2);
        bean.setChildrenMap(map);

        String json = JSON.toJSONString(bean);
        JSONObject jo = JSON.parseObject(json);

        Bean<Long> convertBean = ClassUtils.convert(jo, Bean.class, Long.class);
//        Bean<Long> convertBean = ClassUtils.convert(jo, Bean.class);
        System.out.println(convertBean);
    }

    @Test
    public void testMapConvert(){
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "2016-04-21 12:12:12");
        map.put(3, Long.toString(System.currentTimeMillis()));

        Map<String, Date> dateMap = MapUtils.convert(map, TreeMap.class, String.class, Date.class);
        for(Map.Entry<String, Date> entry : dateMap.entrySet()){
            String key = entry.getKey();
            Date value = entry.getValue();
            System.out.println(key + "|-|" + DateUtils.formatNormalDateTime(value));
        }
    }

    @Test
    public void testCollectionConvert(){
        Collection<String> coll = new TreeSet<>();
        coll.add("2016-04-21 12:12:12");
        coll.add(Long.toString(System.currentTimeMillis()));

        HashSet<Date> targetColl = CollectionUtils.convert(coll, HashSet.class, Date.class);
        for(Date date : targetColl){
            System.out.println(date);
        }

        String[] src = {"2016-04-21 12:12:12", Long.toString(System.currentTimeMillis())};

        TreeSet<Date> targetTreeSet = CollectionUtils.convert(coll, TreeSet.class, Date.class);
        for(Date date : targetTreeSet){
            System.out.println(date);
        }

        System.out.println(StringUtils.urlEncode("{\"linesId\":\"37\",\"platformName\":\"ralphlauren.com\",\"productCategory\":\"Men›All Brands›Shorts & Swim›Shorts›Short\",\"productCount\":\"1\",\"productId\":\"70474976\",\"productImage\":\"http://www.ralphlauren.com/graphics/product_images/pPOLO2-22101958_lifestyle_t240.jpg\",\"productPrice\":\"85.00\",\"productPriceType\":\"$\",\"productShipper\":\"ralphlauren\",\"productSkuId\":\"22102132\",\"productSkuInfo\":\"SIZE:Tall 38;COLOR:CLASSIC STONE;\",\"productStatus\":\"1\",\"productTitle\":\"Suffield Short\",\"productUrl\":\"http://www.ralphlauren.com/product/index.jsp?productId=70474976\",\"productWeight\":\"\",\"productWeightType\":\"\"}"));
    }
}
