package io.github.wesleyone.cases.test;

import io.github.wesleyone.cases.test.annotation.TestCase;
import io.github.wesleyone.cases.test.annotation.TestCases;
import io.github.wesleyone.cases.test.annotation.TestParam;
import io.github.wesleyone.cases.test.params.DemoBizContext;
import io.github.wesleyone.cases.test.params.ParamA;
import io.github.wesleyone.cases.test.params.ParamB;
import io.github.wesleyone.cases.test.params.ParamC;
import org.junit.Test;

import java.util.List;

/**
 * @author http://wesleyone.github.io/
 */
public class DemoTestBootstrap extends TestBootstrap<DemoBizContext> {

    @Override
    public DemoBizContext newInstance() {
        return new DemoBizContext();
    }

    @Override
    public void handle(DemoBizContext context) {
        System.out.println("handle..."+context);
    }

    @Override
    public void setProperties(DemoBizContext bizContext, Class<?> propertyClass, String tag, Object value) {
        if (value instanceof ParamA) {
            bizContext.setParamA((ParamA)value);
        } else if (tag.equals(ParamBImpl.class.getTypeName())) {
            bizContext.setParamB((ParamB)value);
        } else if (tag.equals(ParamCImpl.class.getTypeName())) {
            bizContext.setParamC((List<ParamC>)value);
        }
    }

    public void before_jack_what_hangzhou(TestCaseContext<DemoBizContext> context) {
        System.out.println("before..."+context);
    }

    @TestCases(
            params = {@TestParam(clazz = ParamAImpl.class, in = {"_Jack"})},
            cases = {@TestCase(name = "Jack_*_Hangzhou", desc = "test", params = {
                    @TestParam(clazz = ParamBImpl.class, in = {"*"}),
                    @TestParam(clazz = ParamCImpl.class, in = {"_Hangzhou"})
            })})
    public void jack_what_hangzhou(TestCaseContext<DemoBizContext> context) {
        System.out.println("after..."+context);
    }

    @Test
    public void test() {
        run();
    }
}
