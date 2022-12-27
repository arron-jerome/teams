package com.disney.teams.common.dao.anno;



import com.disney.teams.common.dao.criteria.Direction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字段排序方式设置注解，作用于实体bean的字段上
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface QueryOrder {
    /**
     * @return
     * @description 排序方式, 默认升序
     */
    public Direction type() default Direction.ASC;

    /**
     * @return
     * @description 字段名称, 默认与注解字段名称一致
     */
    public String value() default "";

    /**
     * @return
     * @description 排序位
     */
    public int index() default 0;
}
