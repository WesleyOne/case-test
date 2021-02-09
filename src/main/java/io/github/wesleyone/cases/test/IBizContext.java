package io.github.wesleyone.cases.test;

/**
 * 业务上下文用接口
 * Interface is used to specify YOUR BUSINESS CONTEXT
 *
 * @author http://wesleyone.github.io/
 */
public interface IBizContext<T> {

    /**
     * new 业务上下文
     * INSTANCE YOUR BUSINESS CONTEXT
     *
     * 由用户处理，以适用于复杂构造方法
     *
     * @return 业务上下文对象 YOUR BUSINESS CONTEXT
     */
    T newBizContext();

    /**
     * 设置属性值
     * set Properties of YOUR BUSINESS CONTEXT
     *
     * 由用户处理，以适用于复杂属性设置方法
     *
     * @param bizContext    业务上下文对象 YOUR BUSINESS CONTEXT
     * @param tag           参数标记 {@link IParam#getTag()}
     * @param value         属性值
     */
    void setProperties(T bizContext, String tag, Object value);

    /**
     * 业务处理
     * handle YOUR BUSINESS CONTEXT
     * @param bizContext 业务上下文对象 YOUR BUSINESS CONTEXT
     */
    void handle(T bizContext);
}
