package io.github.wesleyone.cases.test;

import io.github.wesleyone.cases.test.params.ParamC;

import java.util.Arrays;
import java.util.List;

/**
 * @author http://wesleyone.github.io/
 */
public class ParamCImpl implements IParam{

    @Override
    public Class<?> getParamType() {
        return ParamC.class;
    }

    public List<ParamC> _Beijing() {
        ParamC p1 = new ParamC();
        p1.setName("work");
        ParamC p2 = new ParamC();
        p2.setName("study");
        return Arrays.asList(p1,p2);
    }

    public List<ParamC> _Hangzhou() {
        ParamC p1 = new ParamC();
        p1.setName("business");
        ParamC p2 = new ParamC();
        p2.setName("teach");
        return Arrays.asList(p1,p2);
    }

}
