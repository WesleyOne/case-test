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
    public void setProperties(DemoBizContext bizContext, Class<?> propertyClass, String tag, Object value) {
        if (tag.equals(ParamAImpl.class.getTypeName())) {
            bizContext.setParamA((ParamA)value);
        } else if (tag.equals(ParamBImpl.class.getTypeName())) {
            bizContext.setParamB((List<ParamB>)value);
        } else if (tag.equals(ParamCImpl.class.getTypeName())) {
            bizContext.setParamC((ParamC) value);
        }
    }

    @Override
    public void handle(DemoBizContext context) {
        System.out.println("handle..."+context);
    }

    @Test
    public void test() {
        run();
    }

    public void before_jack_what_hangzhou(TestCaseContext<DemoBizContext> context) {
        System.out.println("before..."+context);
    }

    public void before_john_what_beijing(TestCaseContext<DemoBizContext> context) {
        System.out.println("before..."+context);
    }

    @TestCases(
            params = {@TestParam(clazz = ParamAImpl.class, in = {"_Jack"})},
            cases = {@TestCase(name = "Jack_*_Hangzhou", desc = "test", params = {
                    @TestParam(clazz = ParamBImpl.class),
                    @TestParam(clazz = ParamCImpl.class, in = {"_Hangzhou"})
            })})
    public void jack_what_hangzhou(TestCaseContext<DemoBizContext> context) {
        System.out.println("after..."+context);
    }

    @TestCase(name = "John_*_Beijing", desc = "test", params = {
            @TestParam(clazz = ParamAImpl.class, in = {"_John"}),
            @TestParam(clazz = ParamBImpl.class),
            @TestParam(clazz = ParamCImpl.class, in = {"_Beijing"}),
    })
    public void john_what_beijing(TestCaseContext<DemoBizContext> context) {
        System.out.println("after..."+context);
    }
}
