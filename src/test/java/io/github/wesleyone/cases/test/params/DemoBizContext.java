package io.github.wesleyone.cases.test.params;

import java.util.List;

/**
 * @author http://wesleyone.github.io/
 */
public class DemoBizContext {

    private ParamA paramA;
    private List<ParamB> paramB;
    private ParamC paramC;

    public ParamA getParamA() {
        return paramA;
    }

    public void setParamA(ParamA paramA) {
        this.paramA = paramA;
    }

    public List<ParamB> getParamB() {
        return paramB;
    }

    public void setParamB(List<ParamB> paramB) {
        this.paramB = paramB;
    }

    public ParamC getParamC() {
        return paramC;
    }

    public void setParamC(ParamC paramC) {
        this.paramC = paramC;
    }

    @Override
    public String toString() {
        return "DemoBizContext{" +
                "paramA=" + paramA +
                ", paramB=" + paramB +
                ", paramC=" + paramC +
                '}';
    }
}
