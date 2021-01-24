package io.github.wesleyone.cases.test;

/**
 * 业务上下文用接口
 * Interface is used to specify <bold>YOUR BUSINESS CONTEXT</bold>
 *
 * @AUTHOR: http://wesleyone.github.io/
 */
public interface IBizContext<T> {

    /**
     * new 业务上下文
     * INSTANCE <bold>YOUR BUSINESS CONTEXT</bold>
     * @return 业务上下文对象 <bold>YOUR BUSINESS CONTEXT</bold>
     */
    T newInstance();

    /**
     * 设置属性值
     * set Properties of <bold>YOUR BUSINESS CONTEXT</bold>
     * @param bizContext    业务上下文对象 <bold>YOUR BUSINESS CONTEXT</bold>
     * @param propertyClazz 属性类型
     * @param tag           参数标记 {@link IParam#getTag()}
     * @param value         属性值
     */
    void setProperties(T bizContext, Class<?> propertyClazz, String tag, Object value);

    /**
     * 业务处理
     * handle <bold>YOUR BUSINESS CONTEXT</bold>
     * @param bizContext 业务上下文对象 <bold>YOUR BUSINESS CONTEXT</bold>
     */
    void handle(T bizContext);
}
