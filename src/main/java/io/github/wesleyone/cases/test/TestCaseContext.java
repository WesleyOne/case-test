package io.github.wesleyone.cases.test;

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
     * 上下文拼接名称
     */
    private String contextName;
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

    public String getContextName() {
        return contextName;
    }

    public void setContextName(String contextName) {
        this.contextName = contextName;
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
                ", contextName='" + contextName + '\'' +
                ", context=" + context +
                '}';
    }
}
