package io.github.wesleyone.cases.test;

import io.github.wesleyone.cases.test.annotation.TestCase;
import io.github.wesleyone.cases.test.annotation.TestParam;
import io.github.wesleyone.cases.test.biz.params.PriceContext;

/**
 * 测试场景构建
 * @author http://wesleyone.github.io/
 */
public class MallPriceContextTestBoostrap extends PriceContextTestBoostrap {

    /* 构建测试用场景 */

    /**
     * 前置处理方法，可以没有
     * 命名要求如下：
     * before_{对应后置处理方法}
     *
     * @param context 场景上下文
     */
    public void before_newUserAllGoods(TestCaseContext<PriceContext> context) {
        System.out.println("newUserAllGoods前置处理，可以在此处初始化MOCK数据等");
    }

    /**
     * 后置处理方法
     * 注解部分指定入参的属性范围
     * 方法内容实现具体的后置处理
     *
     * @param context 场景上下文
     */
    @TestCase(name = "新用户购买所有商品", desc = "使用新人价",params = {
            @TestParam(clazz = IsNewUserParam.class, in = "newUser"),
            @TestParam(clazz = GoodInfoParam.class),
    })
    public void newUserAllGoods(TestCaseContext<PriceContext> context) {
        System.out.println("newUserAllGoods后置处理，可以在此处断言校验等");
    }


    public void before_oldUserAllGoods(TestCaseContext<PriceContext> context) {
        System.out.println("oldUserAllGoods前置处理，可以在此处初始化MOCK数据等");
    }

    @TestCase(name = "老用户购买所有商品", desc = "老用户优惠",params = {
            @TestParam(clazz = IsNewUserParam.class, in = "oldUser"),
            @TestParam(clazz = GoodInfoParam.class),
    })
    public void oldUserAllGoods(TestCaseContext<PriceContext> context) {
        System.out.println("oldUserAllGoods后置处理，可以在此处断言校验等");
    }
}
