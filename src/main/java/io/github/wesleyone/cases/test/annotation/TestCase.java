package io.github.wesleyone.cases.test.annotation;

import java.lang.annotation.*;

/**
 * 场景用接口
 * Interface is used to specify a case
 *
 * @author http://wesleyone.github.io/
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface TestCase {

    /**
     * 场景名称
     * @return 场景名称
     */
    String name();

    /**
     * 场景描述
     * @return 场景描述
     */
    String desc() default "";

    /**
     * 场景关联的参数条件
     * @return 参数条件
     */
    TestParam[] params() default {};
}
