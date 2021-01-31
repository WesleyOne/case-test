package io.github.wesleyone.cases.test;

import io.github.wesleyone.cases.test.params.ParamC;

/**
 * @author http://wesleyone.github.io/
 */
public class ParamCImpl implements IParam{

    @Override
    public Class<?> getParamType() {
        return ParamC.class;
    }

    public ParamC _Beijing() {
        ParamC p = new ParamC();
        p.setName("Beijing");
        return p;
    }

    public ParamC _Hangzhou() {
        ParamC p = new ParamC();
        p.setName("Hangzhou");
        return p;
    }

}
