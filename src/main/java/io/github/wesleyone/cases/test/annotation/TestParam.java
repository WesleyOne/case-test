package io.github.wesleyone.cases.test.annotation;

import io.github.wesleyone.cases.test.IParam;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 参数条件
 * Interface is used to specify conditions of parameter
 *
 * @AUTHOR: http://wesleyone.github.io/
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface TestParam {

    /**
     * 执行参数类型
     * @see IParam 参数类型必须实现该接口
     *
     * @return 参数类型
     */
    Class<? extends IParam> clazz();

    /**
     * 参数类型类里的需要包含的方法名称
     * 例如{"method1","method2"}
     *
     * @return 包含的方法名称
     */
    String[] in() default {};
}
