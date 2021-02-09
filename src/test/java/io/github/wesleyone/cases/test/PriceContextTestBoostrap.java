package io.github.wesleyone.cases.test;

import io.github.wesleyone.cases.test.annotation.TestCase;
import io.github.wesleyone.cases.test.annotation.TestParam;
import io.github.wesleyone.cases.test.biz.params.GoodInfo;
import io.github.wesleyone.cases.test.biz.params.PriceContext;
import io.github.wesleyone.cases.test.biz.params.PriceRequest;
import org.junit.Test;

import java.util.List;

/**
 * @author http://wesleyone.github.io/
 */
public class PriceContextTestBoostrap extends TestBootstrap<PriceContext> {

    @Test
    public void test() {
        run();
    }

    /* 用户自定义自动装配相关方法 */

    @Override
    public PriceContext newInstance() {
        return new PriceContext();
    }

    @Override
    public void setProperties(PriceContext bizContext, String tag, Object value) {
        PriceRequest priceRequest = bizContext.getPriceRequest();
        if (priceRequest == null) {
            priceRequest = new PriceRequest();
            bizContext.setPriceRequest(priceRequest);
        }

        if (tag.equals(IsNewUserParam.class.getTypeName())) {
            priceRequest.setNewUser((Boolean) value);
        } else if (tag.equals(GoodInfoParam.class.getTypeName())) {
            priceRequest.setGoodInfoList((List<GoodInfo>) value);
        }
    }

    @Override
    public void handle(PriceContext bizContext) {
        System.out.println("业务处理逻辑："+bizContext);
    }

    /* 构建测试用场景 */

    /**
     * 前置处理方法，可以没有
     * 命名要求如下：
     * before_{对应后置处理方法}
     *
     * @param context 场景上下文
     */
    public void before_newUserAllGoods(TestCaseContext<PriceContext> context) {
        System.out.println("前置处理，可以在此处初始化MOCK数据等");
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
        System.out.println("后置处理，可以在此处断言校验等");
    }
}
