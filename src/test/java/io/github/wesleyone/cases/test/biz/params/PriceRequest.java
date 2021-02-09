package io.github.wesleyone.cases.test.biz.params;

import java.util.List;

/**
 * 模拟定价接口入参
 * @author http://wesleyone.github.io/
 */
public class PriceRequest {

    /**
     * 是否新用户
     */
    private boolean isNewUser;
    /**
     * 商品id列表
     */
    private List<GoodInfo> goodInfoList;

    public boolean isNewUser() {
        return isNewUser;
    }

    public void setNewUser(boolean newUser) {
        isNewUser = newUser;
    }

    public List<GoodInfo> getGoodInfoList() {
        return goodInfoList;
    }

    public void setGoodInfoList(List<GoodInfo> goodInfoList) {
        this.goodInfoList = goodInfoList;
    }

    @Override
    public String toString() {
        return "PriceRequest{" +
                "isNewUser=" + isNewUser +
                ", goodInfoList=" + goodInfoList +
                '}';
    }
}
