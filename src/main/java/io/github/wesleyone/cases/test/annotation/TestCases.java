package io.github.wesleyone.cases.test.annotation;

import java.lang.annotation.*;

/**
 * 场景集合用接口
 * Interface is used to specify cases
 *
 * @author http://wesleyone.github.io/
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface TestCases {

    /**
     * 集合下所有场景的公共参数条件
     * @return 参数条件
     */
    TestParam[] params() default {};

    /**
     * 集合下所有场景
     * @return 场景
     */
    TestCase[] cases();
}
