package com.disney.teams.common.dao.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字段或方法排序位，用于设置字段的先后顺序，加上此注解的字段或方法将按照定义的顺序返回
 * 方法和字段默认排在未加注解的后面
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface MemberOrder {

    /**
     * 排序位
     *
     * @return
     */
    int value() default 0;

}
