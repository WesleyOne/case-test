package io.github.wesleyone.cases.test;

/**
 * 参数对象用接口
 * Interface is used to discover parameters
 *
 * @author http://wesleyone.github.io/
 */
public interface IParam<T> {

    /**
     * 根据参数标记，可用于自定义设置参数
     * get unique tag of parameter
     *
     * @return 参数标记
     */
    default String getTag() {
        return this.getClass().getTypeName();
    }
}
