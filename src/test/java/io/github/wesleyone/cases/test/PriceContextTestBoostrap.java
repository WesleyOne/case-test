package io.github.wesleyone.cases.test;

import io.github.wesleyone.cases.test.biz.params.GoodInfo;
import io.github.wesleyone.cases.test.biz.params.PriceContext;
import io.github.wesleyone.cases.test.biz.params.PriceRequest;
import org.junit.Test;

import java.util.List;

/**
 * 实现定价通用的实现
 * @author http://wesleyone.github.io/
 */
public class PriceContextTestBoostrap extends TestBootstrap<PriceContext> {

    @Test
    public void test() {
        run();
    }

    /* 用户自定义自动装配相关方法 */

    @Override
    public PriceContext newBizContext() {
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

}
