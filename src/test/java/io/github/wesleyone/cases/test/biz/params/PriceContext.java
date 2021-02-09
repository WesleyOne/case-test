package io.github.wesleyone.cases.test.biz.params;

/**
 * 模拟定价接口上下文
 * @author http://wesleyone.github.io/
 */
public class PriceContext {
    /**
     * 入参
     */
    private PriceRequest priceRequest;
    /**
     * 返回值
     */
    private PriceResponse priceResponse;

    public PriceRequest getPriceRequest() {
        return priceRequest;
    }

    public void setPriceRequest(PriceRequest priceRequest) {
        this.priceRequest = priceRequest;
    }

    public PriceResponse getPriceResponse() {
        return priceResponse;
    }

    public void setPriceResponse(PriceResponse priceResponse) {
        this.priceResponse = priceResponse;
    }

    @Override
    public String toString() {
        return "PriceContext{" +
                "priceRequest=" + priceRequest +
                ", priceResponse=" + priceResponse +
                '}';
    }
}
