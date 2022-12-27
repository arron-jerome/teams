package com.disney.teams.utils;

import com.disney.teams.exception.BasicRuntimeException;
import com.disney.teams.utils.type.ArrayUtils;
import com.disney.teams.utils.type.CollectionUtils;
import com.disney.teams.utils.type.MapUtils;
import com.disney.teams.utils.type.StringUtils;

import java.util.Collection;
import java.util.Map;

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
public class AssertUtils {

    private static final String ASSERT_FAILED = "assert failed";

    public static void isTrue(boolean expression) {
        isTrue(expression, AssertUtils.ASSERT_FAILED);
    }

    public static void isTrue(boolean expression, String error) {
        if (!expression) {
            throw new BasicRuntimeException(error);
        }
    }

    public static void isNull(Object object) {
        isNull(object, AssertUtils.ASSERT_FAILED);
    }

    public static void isNull(Object object, String error) {
        if (object != null) {
            throw new BasicRuntimeException(error);
        }
    }

    public static void notNull(Object object) {
        notNull(object, AssertUtils.ASSERT_FAILED);
    }

    public static void notNull(Object object, String error) {
        if (object == null) {
            throw new BasicRuntimeException(error);
        }
    }

    public static void hasLength(String text) {
        hasLength(text, AssertUtils.ASSERT_FAILED);
    }

    public static void hasLength(String text, String error) {
        if (!StringUtils.hasLength(text)) {
            throw new BasicRuntimeException(error);
        }
    }

    public static void hasText(String text) {
        hasText(text, AssertUtils.ASSERT_FAILED);
    }

    public static void hasText(String text, String error) {
        if (!StringUtils.hasText(text)) {
            throw new BasicRuntimeException(error);
        }
    }

    public static void notContain(String textToSearch, String substring, String error) {
        if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring) && textToSearch
                .contains(substring)) {
            throw new BasicRuntimeException(error);
        }
    }

    public static void notContain(String textToSearch, char ch, String error) {
        if (textToSearch.indexOf(ch) != -1)
            throw new BasicRuntimeException(error);
    }

    public static void notEmpty(Object[] array) {
        notEmpty(array, AssertUtils.ASSERT_FAILED);
    }

    public static void notEmpty(Object[] array, String error) {
        if (ArrayUtils.isEmpty(array)) {
            throw new BasicRuntimeException(error);
        }
    }

    public static void notEmpty(Collection<?> collection) {
        notEmpty(collection, AssertUtils.ASSERT_FAILED);
    }

    public static void notEmpty(Collection<?> collection, String error) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new BasicRuntimeException(error);
        }
    }

    public static void notEmpty(Map<?, ?> map) {
        notEmpty(map, AssertUtils.ASSERT_FAILED);
    }

    public static void notEmpty(Map<?, ?> map, String error) {
        if (MapUtils.isEmpty(map)) {
            throw new BasicRuntimeException(error);
        }
    }

    public static void isInstanceOf(Class<?> clazz, Object obj, String error) {
        notNull(clazz, AssertUtils.ASSERT_FAILED);
        if (!clazz.isInstance(obj)) {
            throw new BasicRuntimeException(error);
        }
    }

    public static void isAssignable(Class<?> superType, Class<?> subType, String error) {
        notNull(superType, AssertUtils.ASSERT_FAILED);
        if (subType == null || !superType.isAssignableFrom(subType)) {
            throw new BasicRuntimeException(error);
        }
    }
}
