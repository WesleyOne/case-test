package io.github.wesleyone.cases.test;

import io.github.wesleyone.cases.test.params.ParamA;
import io.github.wesleyone.cases.test.params.ParamB;

/**
 * @AUTHOR: http://wesleyone.github.io/
 */
public class ParamBImpl implements IParam{

    @Override
    public Class<?> getParamType() {
        return ParamB.class;
    }

    public ParamB _Beijing() {
        ParamB p = new ParamB();
        p.setName("Beijing");
        return p;
    }

    public ParamB _Hangzhou() {
        ParamB p = new ParamB();
        p.setName("Hangzhou");
        return p;
    }

}
