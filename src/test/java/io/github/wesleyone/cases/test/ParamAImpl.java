package io.github.wesleyone.cases.test;

import io.github.wesleyone.cases.test.params.ParamA;

/**
 * @AUTHOR: http://wesleyone.github.io/
 */
public class ParamAImpl implements IParam{

    @Override
    public Class<?> getParamType() {
        return ParamA.class;
    }

    public ParamA _Jack() {
        ParamA p = new ParamA();
        p.setName("Jack");
        return p;
    }

    public ParamA _John() {
        ParamA p = new ParamA();
        p.setName("John");
        return p;
    }

}
