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
    private final String caseName;
    /**
     * 场景介绍
     */
    private final String caseDesc;
    /**
     * 业务上下文
     */
    private final T context;

    public TestCaseContext(String caseName ,String caseDesc, T context) {
        this.caseName = caseName;
        this.caseDesc = caseDesc;
        this.context = context;
    }

    public String getCaseName() {
        return caseName;
    }

    public String getCaseDesc() {
        return caseDesc;
    }

    public T getContext() {
        return context;
    }

    @Override
    public String toString() {
        return "TestCaseContext{" +
                "caseName='" + caseName + '\'' +
                ", caseDesc='" + caseDesc + '\'' +
                ", context=" + context +
                '}';
    }
}
