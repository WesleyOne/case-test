package io.github.wesleyone.cases.test.params;

import java.util.List;

/**
 * @author http://wesleyone.github.io/
 */
public class DemoBizContext {

    private ParamA paramA;
    private ParamB paramB;
    private List<ParamC> paramC;

    public ParamA getParamA() {
        return paramA;
    }

    public void setParamA(ParamA paramA) {
        this.paramA = paramA;
    }

    public ParamB getParamB() {
        return paramB;
    }

    public void setParamB(ParamB paramB) {
        this.paramB = paramB;
    }

    public List<ParamC> getParamC() {
        return paramC;
    }

    public void setParamC(List<ParamC> paramC) {
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
