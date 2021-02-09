package io.github.wesleyone.cases.test.util;

import io.github.wesleyone.cases.test.GoodInfoParam;
import io.github.wesleyone.cases.test.IParam;
import org.junit.Test;

/**
 * @author http://wesleyone.github.io/
 */
public class GenericsUtilTest {

    @Test
    public void getInterfaceGenericsTypeName() {
        GoodInfoParam goodInfoParam = new GoodInfoParam();
        String goodInfoParamTypeName = GenericsUtil.getInterfaceGenericsTypeName(goodInfoParam, IParam.class, 0);
        System.out.println(goodInfoParamTypeName);
    }
}
