package io.github.wesleyone.cases.test;

import io.github.wesleyone.cases.test.params.ParamB;

import java.util.Arrays;
import java.util.List;

/**
 * @author http://wesleyone.github.io/
 */
public class ParamBImpl implements IParam{

    @Override
    public Class<?> getParamType() {
        return ParamB.class;
    }

    public List<ParamB> _work_or_study_in() {
        ParamB p1 = new ParamB();
        p1.setName("work");
        ParamB p2 = new ParamB();
        p2.setName("study");
        return Arrays.asList(p1,p2);
    }

    public List<ParamB> _business_or_teach_in() {
        ParamB p1 = new ParamB();
        p1.setName("business");
        ParamB p2 = new ParamB();
        p2.setName("teach");
        return Arrays.asList(p1,p2);
    }

}
