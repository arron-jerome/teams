package com.disney.teams.common.dao.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字段修改注解，使用在数据库实体的字段上，此注解用于字段的更新参数设置
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface Modify{
    
    /**
     * 默认false，针对null数据不做更新,true更新null值数据
     * @return
     */
    public boolean nullable() default false;
}
