package com.disney.teams.common.dao.anno;


import com.disney.teams.common.dao.criteria.GenericCriteria;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 此注解作用在{@link GenericCriteria}的子类属性上，使用该注解表示该属性不用作查询参数
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface NoQuery {
}
