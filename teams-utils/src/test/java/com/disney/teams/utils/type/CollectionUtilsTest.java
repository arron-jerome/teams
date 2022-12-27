package com.disney.teams.utils.type;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
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
public class CollectionUtilsTest {

    @Test
    public void reverseSelf() throws Exception {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
        List<Integer> newList = CollectionUtils.reverse(list);
        System.out.println(newList);
        CollectionUtils.reverseSelf(list);
        CollectionUtils.reverseSelf(null);
        CollectionUtils.reverseSelf(Arrays.asList());
        System.out.println(list);
    }

    @Test
    public void max() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, null);
        List<Integer> list1 = Arrays.asList(null, null);
        Assert.assertEquals(Integer.valueOf(7), CollectionUtils.max(list));
        Assert.assertEquals(null, CollectionUtils.max(list1));
    }

    @Test
    public void min(){
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, null);
        List<Integer> list1 = Arrays.asList(null, null);
        Assert.assertEquals(Integer.valueOf(1), CollectionUtils.min(list));
        Assert.assertEquals(null, CollectionUtils.min(list1));
    }

    @Test
    public void groupSize() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
        for(int i = 1; i <= 8; i++) {
            System.out.println("Group by " + i + " -> " + CollectionUtils.groupBySize(list, i));
        }
    }

    @Test
    public void eqIgSep() throws Exception {
        List<Integer> src = Arrays.asList(1, 2, 3, 5, 5, 6, 7);
        List<Integer> dest = Arrays.asList(1, 5, 2, 3, 5, 6, 7);
        Assert.assertTrue(CollectionUtils.eqIgSep(src, dest));
        Assert.assertFalse(CollectionUtils.eq(src, dest));

        src = dest = null;
        Assert.assertTrue(CollectionUtils.eqIgSep(src, dest));
        Assert.assertTrue(CollectionUtils.eq(src, dest));

        src = null;
        dest = new ArrayList<>();
        Assert.assertTrue(CollectionUtils.eqIgSep(src, dest));
        Assert.assertFalse(CollectionUtils.eq(src, dest));
    }
}
