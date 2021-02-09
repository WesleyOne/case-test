package io.github.wesleyone.cases.test;

import io.github.wesleyone.cases.test.biz.params.GoodInfo;
import io.github.wesleyone.cases.test.biz.params.PriceRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * GoodInfoList对应的属性值对象
 *
 * @see PriceRequest#setGoodInfoList(List)
 * @author http://wesleyone.github.io/
 */
public class GoodInfoParam implements IParam<List<GoodInfo>>{

    public List<GoodInfo> goods101_2() {
        GoodInfo goodInfo101 = new GoodInfo();
        goodInfo101.setId(101L);
        goodInfo101.setNumber(2);
        return Collections.singletonList(goodInfo101);
    }

    public List<GoodInfo> goods101_909() {
        GoodInfo goodInfo101 = new GoodInfo();
        goodInfo101.setId(101L);
        goodInfo101.setNumber(1);
        GoodInfo goodInfo909 = new GoodInfo();
        goodInfo909.setId(909L);
        goodInfo909.setNumber(1);
        return Arrays.asList(goodInfo101, goodInfo909);
    }
}
