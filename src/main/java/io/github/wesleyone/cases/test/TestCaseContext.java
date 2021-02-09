package io.github.wesleyone.cases.test;

import java.util.List;

/**
 * 场景上下文
 * be used to connection context of test-case
 *
 * @author http://wesleyone.github.io/
 */
public class TestCaseContext<T> {
    /**
     * 场景名称
     */
    private String caseName;
    /**
     * 场景介绍
     */
    private String caseDesc;
    /**
     * 当前用例的关联的参数方法名
     */
    private List<String> paramNames;
    /**
     * 业务上下文
     */
    private T context;

    public TestCaseContext() {
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getCaseDesc() {
        return caseDesc;
    }

    public void setCaseDesc(String caseDesc) {
        this.caseDesc = caseDesc;
    }

    public List<String> getParamNames() {
        return paramNames;
    }

    public void setParamNames(List<String> paramNames) {
        this.paramNames = paramNames;
    }

    public T getContext() {
        return context;
    }

    public void setContext(T context) {
        this.context = context;
    }

    @Override
    public String toString() {
        return "TestCaseContext{" +
                "caseName='" + caseName + '\'' +
                ", caseDesc='" + caseDesc + '\'' +
                ", paramNames=" + paramNames +
                ", context=" + context +
                '}';
    }
}
