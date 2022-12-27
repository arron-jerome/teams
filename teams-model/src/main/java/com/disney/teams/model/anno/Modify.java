package com.disney.teams.model.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author arron.zhou
 * @version 1.0.0
 * @date 2022/12/16
 * Description:字段修改注解，使用在数据库实体的字段上，此注解用于字段的更新参数设置
 * Modification  History:
 * Date         Author        Version        Description
 * ------------------------------------------------------
 * 2022/12/16       arron.zhou      1.0.0          create
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface Modify {

    /**
     * 默认false，针对null数据不做更新,true更新null值数据
     */
    public boolean nullable() default false;
}
