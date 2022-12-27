package com.disney.teams.common.dao.anno;


import com.disney.teams.common.dao.criteria.MatchMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.text.SimpleDateFormat;

/**
 * 字段查询方式注解，作用于实体bean的属性字段
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface Query {
	
    /**
     * @description 实体字段名, 默认与注解的字段名一致
     * @return
     */
	String field() default "";

	/**
	 * @description 对应匹配方式
	 * @return
	 */
	MatchMode matchMode() default MatchMode.EQ;
	
	/**
	 * 是否忽略null值查询条件
	 * @return
	 */
	boolean ignoreNull() default true;
	
	/**
	 * 数据转化格式, 如果字段类型为Date类型
	 * @see {@link SimpleDateFormat}
	 * @return
	 * @deprecated 此参数已经无效, 代码逻辑中会自动转换标准的日期格式
	 */
	@Deprecated
	String format() default "";
	
	/**
	 * 指定查询字段的目标数据类型
	 * @return
	 * @deprecated 此参数已经无效, 代码逻辑中会自动转换数据类型
     */
    @Deprecated
	Class<?> targetType() default Object.class;
}
